package org.expensly.authService.service;

import org.expensly.authService.entity.TokenEntity;
import org.expensly.authService.entity.UserEntity;
import org.expensly.authService.repository.TokenRepository;
import org.expensly.authService.utils.errors.ExceptionFactory;
import org.expensly.authService.utils.implementations.TokenServiceImp;
import org.expensly.authService.utils.strategy.RefreshTokenStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService extends TokenServiceImp<RefreshTokenStrategy> {
    private final TokenRepository tokenRepository;

    public RefreshTokenService(
            @Autowired RefreshTokenStrategy refreshTokenService,
            @Autowired TokenRepository tokenRepository
    ) {
        super(refreshTokenService);
        this.tokenRepository = tokenRepository;
    }

    public TokenEntity getRefreshToken(UserEntity user) {
        return tokenRepository.findByUserEntity(user).orElseThrow(
                () -> ExceptionFactory.notFoundException("No refresh token found for the userId")
        );
    }

    public void createOrUpdate(UserEntity userEntity, String token) {
        TokenEntity tokenEntity = tokenRepository.findByUserEntity(userEntity).orElse(
                TokenEntity
                        .builder()
                        .refreshToken(token)
                        .userEntity(userEntity)
                        .build()
        );

        tokenEntity.setRefreshToken(token);
        tokenRepository.save(tokenEntity);
    }

    public void updateToken(TokenEntity token) {
        tokenRepository.save(token);
    }
}
