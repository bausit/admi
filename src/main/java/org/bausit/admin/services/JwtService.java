package org.bausit.admin.services;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.bausit.admin.dtos.SecurityUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@Log4j2
public class JwtService {
    private final String jwtSecret;

    private final int jwtExpirationSecond;
    
    public JwtService(@Value("${security.jwt.secret}") String jwtSecret,
                      @Value("${security.jwt.expirationSecond}") int jwtExpirationSecond) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationSecond = jwtExpirationSecond;
    }

    public String generateToken(Authentication authentication) {

        SecurityUser userPrincipal = (SecurityUser) authentication.getPrincipal();

        return Jwts.builder()
            .setSubject((userPrincipal.getUsername()))
            .setIssuedAt(new Date())
            .setExpiration(Date.from(Instant.now().plusSeconds(jwtExpirationSecond)))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
