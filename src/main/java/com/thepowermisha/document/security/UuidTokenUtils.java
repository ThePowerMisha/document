package com.thepowermisha.document.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@UtilityClass
public class UuidTokenUtils {
    private static final String SECRET = "my-super-secret-key-at-least-32-chars-long!!!";
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String uuidToToken(UUID uuid) {
        return Jwts.builder()
                .subject(uuid.toString())
                .issuedAt(new Date())
                .signWith(key)
                .compact();
    }

    public UUID tokenToUuid(String token) {
        String uuidStr = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        return UUID.fromString(uuidStr);
    }
}
