package org.expensly.authService.utils.implementations;

import org.expensly.authService.utils.interfaces.TokenService;
import org.expensly.authService.utils.interfaces.TokenStrategy;

public class TokenServiceImp<T extends TokenStrategy> implements TokenService {
    private final T tokenStrategy;

    public TokenServiceImp(T tokenStrategy) {
        this.tokenStrategy = tokenStrategy;
    }

    @Override
    public String generateToken(String username) {
        return tokenStrategy.createToken(username);
    }

    @Override
    public String getUsername(String token) {
        return tokenStrategy.getUsername(token);
    }

    @Override
    public boolean validateToken(String token) {
        return !tokenStrategy.isExpired(token);
    }
}
