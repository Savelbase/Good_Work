package com.rm.toolkit.auth.test.util;

import com.rm.toolkit.auth.model.Role;
import com.rm.toolkit.auth.repository.RoleRepository;
import com.rm.toolkit.auth.security.AuthorityType;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class TokenFactory {

    @Value("${authentication.token.issuer}")
    private String jwtIssuer;
    @Value("${authentication.token.key}")
    private String jwtKey;
    @Value("${authentication.token.accessTokenExpirationSec}")
    private int accessTokenExpirationSec;
    @Value("${authentication.token.refreshTokenExpirationSec}")
    private int refreshTokenExpirationSec;

    private static final String EMPLOYEE_ROLE_ID = "579fc993-6123-419a-ae3c-96b0b230f834";

    private final RoleRepository roleRepository;

    /**
     * @param userId кому токен генерировать
     * @return Pair<refresh-токен, expirationDate>
     */
    public Pair<String, Instant> generateRefreshToken(String userId) {
        Instant issueDate = Instant.now().minus(10, ChronoUnit.SECONDS);

        return generateRefreshToken(userId, issueDate);
    }

    public Pair<String, Instant> generateExpiredRefreshToken(String userId) {
        Instant issueDate = Instant.now().minus(15, ChronoUnit.DAYS);

        return generateRefreshToken(userId, issueDate);
    }

    public Pair<String, Instant> generateRefreshToken(String userId, Instant issueDate) {
        Instant refreshTokenExpirationDate = issueDate.plus(refreshTokenExpirationSec, ChronoUnit.SECONDS);

        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setIssuer(jwtIssuer);
        jwtBuilder.setSubject(userId);
        jwtBuilder.setIssuedAt(new Date(issueDate.toEpochMilli()));
        jwtBuilder.setExpiration(new Date(refreshTokenExpirationDate.toEpochMilli()));

        return Pair.of(jwtBuilder.signWith(SignatureAlgorithm.HS512, jwtKey).compact(), refreshTokenExpirationDate);
    }

    public String generateAccessToken(String userId) {
        Role role = roleRepository.findById(EMPLOYEE_ROLE_ID).orElseThrow(RuntimeException::new);

        return generateAccessToken(userId, role.getAuthorities());
    }

    public String generateAccessToken(String userId, AuthorityType[] authorities) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.SECOND, -10);
        Date issueDate = currentDate.getTime();

        Date accessTokenExpirationDate = new Date(issueDate.getTime() + (long) accessTokenExpirationSec * 1000);

        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setIssuer(jwtIssuer);
        jwtBuilder.setSubject(userId);
        jwtBuilder.setIssuedAt(issueDate);
        jwtBuilder.setExpiration(accessTokenExpirationDate);

        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("authorities", authorities);
        jwtBuilder.addClaims(tokenData);

        return jwtBuilder.signWith(SignatureAlgorithm.HS512, jwtKey).compact();
    }
}
