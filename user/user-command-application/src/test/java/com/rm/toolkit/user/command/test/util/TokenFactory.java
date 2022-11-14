package com.rm.toolkit.user.command.test.util;

import com.rm.toolkit.user.command.model.Role;
import com.rm.toolkit.user.command.model.User;
import com.rm.toolkit.user.command.repository.UserRepository;
import com.rm.toolkit.user.command.util.ValidationUtil;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class TokenFactory {

    private final ValidationUtil validationUtil;

    @Value("${authentication.token.issuer}")
    private String jwtIssuer;
    @Value("${authentication.token.key}")
    private String jwtKey;
    private final static int accessTokenExpirationSec = 3600;

    @Transactional
    public String generateAccessToken(String userId) {
        User user = validationUtil.findUserIfExist(userId);
        Role role = validationUtil.findRoleIfExist(user.getRoleId());

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
        tokenData.put("authorities", role.getAuthorities());
        tokenData.put("rolePriority", role.getPriority());
        jwtBuilder.addClaims(tokenData);

        return jwtBuilder.signWith(SignatureAlgorithm.HS512, jwtKey).compact();
    }
}
