package org.expensly.authService.utils.interfaces;

public interface TokenStrategy {
    String createToken(String username);
    String getUsername(String token);
    boolean isExpired(String token);
}
