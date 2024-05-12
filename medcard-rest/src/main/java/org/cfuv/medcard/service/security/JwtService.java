package org.cfuv.medcard.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class JwtService {

    private final String jwtSecret;
    private final int defaultLifetime;

    public JwtService(@Value("${spring.security.jwt.secret-key}") String jwtSecret,
                      @Value("${spring.security.jwt.lifetime}") int defaultLifetime) {
        this.jwtSecret = jwtSecret;
        this.defaultLifetime = defaultLifetime;
    }

    public String generateSharedToken(String userEmail, Map<String, String> claims) {
        // 2d
        return generateToken(2280, userEmail, claims);
    }

    public String generateToken(String email, boolean rememberMe) {
        return generateToken(rememberMe ? defaultLifetime * 5 : defaultLifetime, email, null);
    }

    public String generateToken(String email) {
        return generateToken(email, false);
    }

    public String generateToken(int lifeTimeInMinutes, String email, Map<String, String> claims) {
        var now = Instant.now();
        JwtBuilder jwtBuilder = buildJwt(lifeTimeInMinutes, email, now);
        if (claims != null) {
            jwtBuilder.claims(claims);
        }
        return jwtBuilder.compact();
    }

    private JwtBuilder buildJwt(int lifeTimeInMinutes, String email, Instant now) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(lifeTimeInMinutes, ChronoUnit.MINUTES)))
                .signWith(generateSecretKey());
    }

    private SecretKey generateSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String extractUsername(String token) {
        return getTokenBody(token).getSubject();
    }

    private Claims getTokenBody(String token) {
        return Jwts
                .parser()
                .verifyWith(generateSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Map<String, String> getClaimsValues(String token, List<String> claimsName) {
        Claims claims = getTokenBody(token);
        Map<String, String> claimValues = new HashMap<>();
        for (String name : claimsName) {
            claimValues.put(name, claims.get(name, String.class));
        }
        return claimValues;
    }

}
