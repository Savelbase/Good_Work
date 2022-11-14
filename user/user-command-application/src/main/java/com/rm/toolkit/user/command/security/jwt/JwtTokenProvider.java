package com.rm.toolkit.user.command.security.jwt;

import com.rm.toolkit.user.command.exception.token.NotAccessTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final JwtUserDetailsService jwtUserDetailsService;

    public Authentication getAuthentication(String token) throws NotAccessTokenException {
        CustomUser userDetails = jwtUserDetailsService.loadUserByUsername(token);

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }
}
