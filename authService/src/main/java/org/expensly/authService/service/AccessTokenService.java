package org.expensly.authService.service;

import org.expensly.authService.utils.implementations.TokenServiceImp;
import org.expensly.authService.utils.strategy.AccessTokenStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenService extends TokenServiceImp<AccessTokenStrategy> {
    public AccessTokenService(@Autowired AccessTokenStrategy accessTokenStrategy) {
        super(accessTokenStrategy);
    }
}
