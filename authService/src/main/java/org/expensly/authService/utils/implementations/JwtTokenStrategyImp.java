package org.expensly.authService.utils.implementations;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.expensly.authService.utils.interfaces.TokenStrategy;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

public class JwtTokenStrategyImp implements TokenStrategy {
    private final String secret;
    private final long exp_time;

    public JwtTokenStrategyImp(String secret, long exp_time) {
        this.secret = secret;
        this.exp_time = exp_time;
    }

    public Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    @Override
    public String createToken(String username) {
        HashMap<String, Object> claims = new HashMap<>();
        return Jwts
                .builder()
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + exp_time))
                .setSubject(username)
                .compact();
    }

    public Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T getClaim(String token, Function<Claims, T> expression) {
        return expression.apply(getAllClaims(token));
    }

    @Override
    public String getUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    private Date getExpDate(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    @Override
    public boolean isExpired(String token) {
        return getExpDate(token).before(new Date());
    }
}
