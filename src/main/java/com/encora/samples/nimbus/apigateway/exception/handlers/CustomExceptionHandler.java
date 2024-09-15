package com.encora.samples.nimbus.apigateway.exception.handlers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Configuration
public class CustomExceptionHandler {

    @Bean
    public WebExceptionHandler customRateLimitHandler() {
        return (exchange, ex) -> {
            if (ex instanceof ResponseStatusException) {
                ResponseStatusException statusException = (ResponseStatusException) ex;
                if (statusException.getStatusCode().value() == HttpStatus.TOO_MANY_REQUESTS.value()) {

                    exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                    return Mono.error(new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                            "You have exceeded the rate limit!"));
                }
            }
            return Mono.error(ex);
        };
    }
}
