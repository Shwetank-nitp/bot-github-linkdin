package org.expensly.authService.utils.strategy;

import org.expensly.authService.utils.implementations.JwtTokenStrategyImp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenStrategy extends JwtTokenStrategyImp {
    private static final long EXP_TIME = 1000 * 60 * 60 * 4;
    public AccessTokenStrategy (@Value("${jwt.access.token}") String secret) {
        super(secret, EXP_TIME);
    }
}
