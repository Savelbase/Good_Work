package com.rm.toolkit.user.command.test.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Collections;

@RequiredArgsConstructor
@Component
public class NetUtil {

    private final TokenFactory tokenFactory;

    @Value("${authentication.token.type}")
    private String tokenType;

    public HttpHeaders createHttpHeaders(String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization",
                String.format("%s %s", tokenType, tokenFactory.generateAccessToken(userId)));
        return headers;
    }
}
