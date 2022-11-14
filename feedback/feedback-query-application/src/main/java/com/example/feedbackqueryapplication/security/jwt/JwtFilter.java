package com.example.feedbackqueryapplication.security.jwt;

import com.example.feedbackqueryapplication.dto.response.error.UnauthorizedErrorResponse;
import com.example.feedbackqueryapplication.exception.token.ExpiredTokenException;
import com.example.feedbackqueryapplication.exception.token.NotAccessTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest,
                                    @NonNull HttpServletResponse httpServletResponse,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(httpServletRequest);
        if (StringUtils.hasText(jwt)) {
            try {
                Authentication authentication = getAuthenticationFromJwt(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } catch (ExpiredJwtException ex) {
                log.error("У вас просрочился токен");
                ExpiredTokenException expiredTokenException = new ExpiredTokenException();

                httpServletResponse.setStatus(expiredTokenException.getRawStatusCode());
                httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                httpServletResponse.setCharacterEncoding("UTF-8");

                UnauthorizedErrorResponse errorResponse = new UnauthorizedErrorResponse(new Date(),
                        UnauthorizedErrorResponse.ErrorType.EXPIRED_ACCESS_TOKEN);
                try (Writer writer = httpServletResponse.getWriter()) {
                    mapper.writeValue(writer, errorResponse);
                }
            } catch (Exception ex) {
                httpServletResponse.setStatus(HttpStatus.SC_UNPROCESSABLE_ENTITY);
                httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                httpServletResponse.setCharacterEncoding("UTF-8");

                UnauthorizedErrorResponse errorResponse = new UnauthorizedErrorResponse(new Date(),
                        UnauthorizedErrorResponse.ErrorType.INVALID_ACCESS_TOKEN);
                try (Writer writer = httpServletResponse.getWriter()) {
                    mapper.writeValue(writer, errorResponse);
                }
            }
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    private Authentication getAuthenticationFromJwt(String token) {
        if (!jwtUtil.isAccessToken(token)) {
            log.error("Это не access токен");
            throw new NotAccessTokenException();
        }

        return jwtTokenProvider.getAuthentication(token);
    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        return jwtUtil.getTokenFromHeader(header);
    }
}
