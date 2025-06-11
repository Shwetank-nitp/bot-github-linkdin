package org.expensly.authService.utils.interfaces;

public interface TokenService {
    String generateToken(String username);
    String getUsername(String token);
    boolean validateToken(String token);
}
