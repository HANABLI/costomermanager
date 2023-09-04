package fr.nablihatem3.costomermanager.filter;

import fr.nablihatem3.costomermanager.provider.TokenProvider;
import fr.nablihatem3.costomermanager.utils.ExceptionUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static fr.nablihatem3.costomermanager.utils.ExceptionUtils.processError;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String[] PUPLIC_ROUTES = {"/user/login", "/user/verify/code", "/user/register", "/user/refresh/token"};
    public static final String HTTP_OPTIONS_METHOD = "OPTIONS";
    private final static String EMAIL_PARAM = "email";
    private final static String TOKEN_PARAM = "token";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Map<String, String> values = getRequestValues(request);
            String token = getToken(request);
            if(tokenProvider.isTockenValid(Objects.requireNonNull(values).get(EMAIL_PARAM), token)) {
                List<GrantedAuthority> authorityList = tokenProvider.getAuthorities(token);
                Authentication authentication = tokenProvider.getAuthentication(values.get(EMAIL_PARAM), authorityList, request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            processError(request, response, exception);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getHeader(AUTHORIZATION) == null || !request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX) ||
                request.getMethod().equalsIgnoreCase(HTTP_OPTIONS_METHOD) || asList(PUPLIC_ROUTES).contains(request.getRequestURI());
    }


    private Map<String, String> getRequestValues(HttpServletRequest request) {
        return Map.of(EMAIL_PARAM, tokenProvider.getSubject(getToken(request), request), TOKEN_PARAM, getToken(request));
    }

    private String getToken(HttpServletRequest request) {
        return ofNullable(request.getHeader(AUTHORIZATION)).filter(header -> header.startsWith(TOKEN_PREFIX))
                .map(token -> token.replace(TOKEN_PREFIX, EMPTY)).get();
    }
}
