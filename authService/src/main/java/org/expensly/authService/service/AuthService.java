package org.expensly.authService.service;

import lombok.extern.slf4j.Slf4j;
import org.expensly.authService.dto.request.UserDto;
import org.expensly.authService.dto.response.SignupDto;
import org.expensly.authService.dto.response.TokenDto;
import org.expensly.authService.entity.RoleEntity;
import org.expensly.authService.entity.TokenEntity;
import org.expensly.authService.entity.UserEntity;
import org.expensly.authService.model.CustomUserDetails;
import org.expensly.authService.model.UserHeaders;
import org.expensly.authService.model.UserInfoMessageKafka;
import org.expensly.authService.repository.UserRepository;
import org.expensly.authService.utils.enums.Role;
import org.expensly.authService.utils.errors.ExceptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AppCacheService cacheService;

    @Autowired
    private ExecutorService executorService;

    public TokenDto login(String username, String password) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        Future<String> futureAccessToken = executorService.submit(
                () -> accessTokenService.generateToken(username)
        );
        Future<String> futureRefreshToken = executorService.submit(
                () -> refreshTokenService.generateToken(username)
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        try {
            refreshTokenService.createOrUpdate(userDetails, futureRefreshToken.get());
            return TokenDto
                    .builder()
                    .accessToken(futureAccessToken.get())
                    .refreshToken(futureRefreshToken.get())
                    .build();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public SignupDto signup(UserDto userDto) {
        if (userRepository.findByUserNameOrEmail(userDto.getUserName(), userDto.getEmail()).isPresent()) {
            throw ExceptionFactory.databaseException("user_name or email already exists");
        }

        UserEntity userEntity = UserEntity
                .builder()
                .userName(userDto.getUserName())
                .password(encoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .roleEntities(new HashSet<>())
                .build();

        userEntity
                .getRoleEntities().add(
                        cacheService.getRole(Role.USER.name())
                );

        UserEntity user = userRepository.save(userEntity);

        UserInfoMessageKafka messageKafka = UserInfoMessageKafka.castInto(userDto, user.getId());
        messageService.send(messageKafka);

        return SignupDto
                .builder()
                .userName(user.getUserName())
                .roles(user.getRoleEntities().stream().map(RoleEntity::getRole).toList())
                .build();

    }

    public TokenDto generateNewToken(String refToken) {
        String username;

        try {
            username = refreshTokenService.getUsername(refToken);
        } catch (Exception ex) {
            throw ExceptionFactory.tokenException("Malicious token!");
        }

        try {

            Future<CustomUserDetails> user = executorService.submit(
                    () -> (CustomUserDetails) userDetailsService.loadUserByUsername(username)
            );
            Future<TokenEntity> tokenEntity = executorService.submit(
                    () -> refreshTokenService.getRefreshToken(user.get())
            );
            Future<String> accessToken = executorService.submit(
                    () -> accessTokenService.generateToken(username)
            );
            Future<String> refreshToken = executorService.submit(
                    () -> refreshTokenService.generateToken(username)
            );

            if (refreshTokenService.validateToken(refToken) && refToken.equals(tokenEntity.get().getRefreshToken())) {
                tokenEntity.get().setRefreshToken(refreshToken.get());
                refreshTokenService.updateToken(tokenEntity.get());

                return TokenDto
                        .builder()
                        .accessToken(accessToken.get())
                        .refreshToken(refreshToken.get())
                        .build();
            } else {
                throw new Exception();
            }
        } catch (ExecutionException e) {
            log.error("Execution error", e);
            throw ExceptionFactory.tokenException("Token execution failed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw ExceptionFactory.tokenException("Token operation was interrupted");
        } catch (Exception ex) {
            throw ExceptionFactory.tokenException("token is invalid or expired");
        }
    }

    public UserHeaders getHeader() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String roles = customUserDetails.getRoleEntities().stream().
                map(RoleEntity::getRole)
                .map(Role::name)
                .collect(Collectors.joining(","));

        return UserHeaders.builder()
                .userId(String.valueOf(customUserDetails.getId()))
                .userName(customUserDetails.getUserName())
                .roles(roles)
                .build();
    }
}

