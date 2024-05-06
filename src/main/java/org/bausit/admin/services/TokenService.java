package org.bausit.admin.services;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.bausit.admin.dtos.SecurityUser;
import org.bausit.admin.dtos.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class TokenService {
    private final String jwtSecret;
    private final KeyPair keyPair;

    private final int jwtExpirationSecond;
    
    public TokenService(@Value("${security.jwt.secret}") String jwtSecret,
                        @Value("${security.jwt.expirationSecond}") int jwtExpirationSecond) throws Exception {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationSecond = jwtExpirationSecond;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        this.keyPair = keyPairGenerator.generateKeyPair();
    }

    public TokenResponse generateToken(SecurityUser user) {
        List<String> roles = user.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());
        var jwt = Jwts.builder()
            .setSubject((user.getUsername()))
            .setIssuer("baus.org")
            .claim("roles", roles)
            .claim("id", user.getParticipant().getId())
            .claim("name", user.getParticipant().getEnglishName())
            .claim("email", user.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(Date.from(Instant.now().plusSeconds(jwtExpirationSecond)))
            .signWith(SignatureAlgorithm.RS512, keyPair.getPrivate())
            .compact();

        log.info("Created user token: {}", jwt);

        return TokenResponse.builder()
            .token(jwt)
            .build();
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parser()
            .setSigningKey(keyPair.getPrivate())
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                .setSigningKey(keyPair.getPrivate())
                .parseClaimsJws(authToken);
            return true;
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

    public String getPublicKey() {
        return keyPair.getPublic().toString();
    }
}
