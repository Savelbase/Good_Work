package com.rm.toolkit.comment.query.security.jwt;


import com.rm.toolkit.comment.query.exception.unprocessableentity.InvalidAccessTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final CustomUserDetailsService customUserDetailsService;

    public Authentication getAuthentication(String token) throws InvalidAccessTokenException {
        CustomUser userDetails = customUserDetailsService.loadUserByUsername(token);

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }
}
