package org.expensly.authService.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.expensly.authService.service.AccessTokenService;
import org.expensly.authService.utils.errors.ExceptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        boolean isAuthenticated = false;

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.replace("Bearer", "").trim();
                username = accessTokenService.getUsername(token);
            }
        } catch (Exception ex) {
            response.setHeader("x-isAuthenticated", "false");
            throw ExceptionFactory.tokenException("Malicious token detected!");
        }

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (username != null && auth == null) {
                if (accessTokenService.validateToken(token)) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    isAuthenticated = true;
                }
            } else if (auth != null && auth.getName().equals(username)) {
                isAuthenticated = true;
            }

            response.setHeader("isAuthenticated", Boolean.toString(isAuthenticated));
        } catch (Exception ex) {
            log.error("JWT Filter exception: ", ex);
            response.setHeader("x-isAuthenticated", "false");
            throw ExceptionFactory.tokenException("Token is expired or invalid");
        }

        filterChain.doFilter(request, response);
    }
}
