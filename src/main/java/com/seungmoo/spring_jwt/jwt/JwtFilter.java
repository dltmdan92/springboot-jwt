package com.seungmoo.spring_jwt.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    /**
     * JWT의 인증정보를 SecurityContext에 저장한다.
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();

        // httpServletRequest header에 있는 Bearer Token 정보를 통해
        // Authentication 객체를 가져 온다.
        Optional<Authentication> authentication = resolveToken(httpServletRequest)
                .filter(StringUtils::hasText)
                .filter(tokenProvider::validateToken)
                .map(tokenProvider::getAuthentication);

        authentication.ifPresentOrElse(
                auth -> {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.debug("Security Context에 '{}' 인증 정보를 저장했습니다. uri: {}", auth.getName(), requestURI);
                },
                () -> log.debug("유효한 JWT 토큰이 없습니다. uri: {}", requestURI));

        filterChain.doFilter(servletRequest, servletResponse);

    }

    /**
     * request 헤더에서 토큰 정보를 빼온다.
     * @param request
     * @return String "Bearer "
     */
    private Optional<String> resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.substring(7));
        }
        return Optional.empty();
    }
}
