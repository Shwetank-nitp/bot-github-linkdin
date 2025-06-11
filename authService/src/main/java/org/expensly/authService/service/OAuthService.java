package org.expensly.authService.service;

import org.expensly.authService.dto.request.UserDto;
import org.expensly.authService.dto.response.TokenDto;
import org.expensly.authService.entity.UserEntity;
import org.expensly.authService.model.UserInfoMessageKafka;
import org.expensly.authService.repository.UserRepository;
import org.expensly.authService.utils.errors.ExceptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OAuthService {
    @Value("${oauth.github.client.id}")
    private String clientId;

    @Value("${oauth.github.client.secret}")
    private String clientSecret;

    @Value("${oauth.github.callback.url}")
    private String callbackUrl;

    @Value("${oauth.github.exchange-uri}")
    private String oauthUri;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AppCacheService appCacheService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageService messageService;

    @Autowired
    AccessTokenService accessTokenService;

    @Autowired
    RefreshTokenService refreshTokenService;

    public TokenDto oauth2GitHubAuth(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("GitHub authorization code must not be null or blank.");
        }

        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> tokenRequest = new HashMap<>();
        tokenRequest.put("client_id", clientId);
        tokenRequest.put("client_secret", clientSecret);
        tokenRequest.put("redirect_uri", callbackUrl);
        tokenRequest.put("code", code);

        HttpEntity<Map<String, Object>> tokenEntity = new HttpEntity<>(tokenRequest, tokenHeaders);

        ResponseEntity<Map> tokenResponse;
        try {
            tokenResponse = restTemplate.postForEntity(oauthUri, tokenEntity, Map.class);
        } catch (Exception e) {
            throw ExceptionFactory.oauthCodeException("Failed to exchange code with GitHub");
        }

        var tokenBody = tokenResponse.getBody();
        if (tokenBody == null || tokenBody.get("access_token") == null) {
            throw new RuntimeException("GitHub did not return a valid access token.");
        }

        String accessToken = (String) tokenBody.get("access_token");

        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(accessToken);
        userHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> userEntity = new HttpEntity<>(userHeaders);

        ResponseEntity<Map> userResponse;
        try {
            userResponse = restTemplate.exchange(
                    "https://api.github.com/user",
                    HttpMethod.GET,
                    userEntity,
                    Map.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user info from GitHub: " + e.getMessage(), e);
        }

        var userBody = userResponse.getBody();
        if (userBody == null || userBody.get("login") == null) {
            throw new RuntimeException("GitHub user information is incomplete.");
        }

        String name = (String) userBody.get("name");
        String email = (String) userBody.get("email");
        String login = (String) userBody.get("login");

        var role = appCacheService.getRole("USER");

        if (name == null) {
            name = "Github User";
        }

        UserEntity isUserExist = userRepository.findByUserName(login).orElse(null);

        UserEntity user = UserEntity.builder()
                .email(email)
                .userName(login)
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .roleEntities(Set.of(role))
                .build();

        if (isUserExist != null) {
            user.setId(isUserExist.getId());
        }
        UserEntity createdUser = userRepository.save(user);

        if (isUserExist == null) {
            UserDto userDto = UserDto.builder()
                    .firstName(name.split(" ")[0])
                    .fullName(name)
                    .build();
            UserInfoMessageKafka messageKafka = UserInfoMessageKafka.castInto(userDto, createdUser.getId());
            messageService.send(messageKafka);
        }

        String accessTokenForClient = accessTokenService.generateToken(createdUser.getUserName());
        String refreshTokenForClient = refreshTokenService.generateToken(createdUser.getUserName());

        return TokenDto.builder()
                .refreshToken(refreshTokenForClient)
                .accessToken(accessTokenForClient)
                .build();
    }
}
