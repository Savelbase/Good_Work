package com.rm.toolkit.comment.query.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rm.toolkit.comment.query.dto.response.error.UnauthorizedErrorResponse;
import com.rm.toolkit.comment.query.exception.unauthorized.ExpiredAccessTokenException;
import com.rm.toolkit.comment.query.exception.unprocessableentity.InvalidAccessTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${authentication.token.type}")
    private String tokenType;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest,
                                    @NonNull HttpServletResponse httpServletResponse,
                                    @NonNull FilterChain filterChain) throws IOException, ServletException {
        String jwt = resolveToken(httpServletRequest);
        if (StringUtils.hasText(jwt)) {
            try {
                if (!jwtUtil.isAccessToken(jwt)) {
                    log.error("Это не access токен");
                    throw new InvalidAccessTokenException();
                }
                Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } catch (ExpiredJwtException ex) {
                log.error("У вас просрочился токен");
                ExpiredAccessTokenException expiredTokenException = new ExpiredAccessTokenException();

                httpServletResponse.setStatus(expiredTokenException.getRawStatusCode());
                httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                httpServletResponse.setCharacterEncoding("UTF-8");

                UnauthorizedErrorResponse errorResponse = new UnauthorizedErrorResponse(new Date(),
                        UnauthorizedErrorResponse.ErrorType.EXPIRED_ACCESS_TOKEN);
                try (Writer writer = httpServletResponse.getWriter()) {
                    mapper.writeValue(writer, errorResponse);
                }
            } catch (Exception ex) {
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenType)) {
            return bearerToken.substring(tokenType.length() + 1);
        }
        return null;
    }
}
