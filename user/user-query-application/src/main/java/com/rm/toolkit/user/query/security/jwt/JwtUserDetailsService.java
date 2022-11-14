package com.rm.toolkit.user.query.security.jwt;

import com.rm.toolkit.user.query.exception.token.NotAccessTokenException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final JwtUtil jwtUtil;

    @Override
    public CustomUser loadUserByUsername(String token) throws UsernameNotFoundException {
        Claims claims = jwtUtil.getClaimsFromJwtToken(token);
        String userId = claims.getSubject();
        if (!jwtUtil.isAccessToken(token)) throw new NotAccessTokenException();
        List<String> authorities = claims.get("authorities", List.class);
        return new CustomUser(userId, authorities);
    }
}
