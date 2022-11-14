package com.rm.toolkit.auth.util;

import com.rm.toolkit.auth.exception.unauthorized.ExpiredRefreshTokenException;
import com.rm.toolkit.auth.security.AuthorityType;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class JwtUtil {

    @Value("${authentication.token.key}")
    private String key;

    public static boolean isRefreshToken(Claims claims) {
        return !claims.containsKey("authorities");
    }

    public static String getTokenHash(String token) {
        return DigestUtils.sha3_256Hex(token.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @param subject      id пользователя, которому делается токен
     * @param issuer       стандартный JWT claims
     * @param issuedAt     время содания токена
     * @param expiration   время, когда токен будет считаться просроченным
     * @param authorities  список прав пользователя, который будет храниться внутри токена
     * @param rolePriority приоритет прав, который будет храниться внутри токена
     * @return access-токен
     */
    public String generateAccessToken(String subject, String issuer, Date issuedAt, Date expiration,
                                      AuthorityType[] authorities, Integer rolePriority) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setIssuer(issuer);
        jwtBuilder.setSubject(subject);
        jwtBuilder.setIssuedAt(issuedAt);
        jwtBuilder.setExpiration(expiration);

        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("authorities", authorities);
        tokenData.put("rolePriority", rolePriority);
        jwtBuilder.addClaims(tokenData);

        return jwtBuilder.signWith(SignatureAlgorithm.HS512, key).compact();
    }

    /**
     * @param subject    id пользователя, которому делается токен
     * @param issuer     стандартный JWT claims
     * @param issuedAt   время содания токена
     * @param expiration время, когда токен будет считаться просроченным
     * @return refresh-токен
     */
    public String generateRefreshToken(String subject, String issuer, Date issuedAt, Date expiration) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setIssuer(issuer);
        jwtBuilder.setSubject(subject);
        jwtBuilder.setIssuedAt(issuedAt);
        jwtBuilder.setExpiration(expiration);

        Map<String, Object> tokenData = new HashMap<>();
        jwtBuilder.addClaims(tokenData);

        return jwtBuilder.signWith(SignatureAlgorithm.HS512, key).compact();
    }

    public Claims getClaimsFromJwtToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            log.error("Токен просрочен");
            throw new ExpiredRefreshTokenException();
        }
    }
}
