package com.rm.toolkit.comment.command.testUtil;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class TestTokenBuilder {
    public Supplier<String> newToken = () -> {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 15);

        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("sub", "e45bfff0-1b04-4b8a-ac95-9629dff88a3e");
        tokenData.put("iss", "rmt_mobile_auth");
        tokenData.put("exp", calendar.getTime());
        tokenData.put("rolePriority", "10");
        tokenData.put("authorities", new String[]{
                "AUTHORIZATION",
                "VIEW_COMMENTS",
                "EDIT_COMMENTS"});

        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setExpiration(calendar.getTime());
        jwtBuilder.setClaims(tokenData);

        String tokenSecretKey = "test";
        return jwtBuilder.signWith(SignatureAlgorithm.HS512, tokenSecretKey).compact();
    };
}
