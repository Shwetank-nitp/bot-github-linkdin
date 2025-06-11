package org.expensly.authService.utils.strategy;

import org.expensly.authService.utils.implementations.JwtTokenStrategyImp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenStrategy extends JwtTokenStrategyImp {
    private static final long EXP_TIME = 1000L * 60L * 60L * 24L * 60L;
    public RefreshTokenStrategy(@Value("${jwt.refresh.token}") String  secret) {
        super(secret, EXP_TIME);
    }
}
