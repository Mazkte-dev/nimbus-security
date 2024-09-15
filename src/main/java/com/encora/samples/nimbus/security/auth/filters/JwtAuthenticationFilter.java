package com.encora.samples.nimbus.security.auth.filters;


import com.encora.samples.nimbus.security.auth.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Filter to authenticate requests based on JWT (JSON Web Token).
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtils jwtUtils;

    /**
     * Filters incoming requests and authenticates the user if a valid JWT is present.
     *
     * @param exchange The ServerWebExchange object representing the current request.
     * @param chain    The WebFilterChain to continue processing the request.
     * @return A Mono<Void> that completes when the filter chain has finished processing.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String jwt = parseJwt(exchange.getRequest());
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String userId = jwtUtils.getUserIdFromJwtToken(jwt);

            Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, null);
            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }
        return chain.filter(exchange);
    }

    /**
     * Parses the JWT from the Authorization header of the request.
     *
     * @param request The ServerHttpRequest object representing the current request.
     * @return The JWT string if found, otherwise null.
     */
    private String parseJwt(ServerHttpRequest request) {
        String headerAuth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}

