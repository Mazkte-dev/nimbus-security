package cloudcode.gemini.services.gateway.filters.handlers.exceptions;

import com.encora.samples.nimbus.apigateway.exception.handlers.CustomExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class CustomExceptionHandlerTest {

    private CustomExceptionHandler customExceptionHandler;

    @BeforeEach
    void setUp() {
        customExceptionHandler = new CustomExceptionHandler();
    }

    @Test
    void testCustomRateLimitHandlerWhenRateLimitExceeded() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);

        Mono<Void> result = customExceptionHandler.customRateLimitHandler().handle(exchange, ex);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException &&
                        ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.TOO_MANY_REQUESTS &&
                        ((ResponseStatusException) throwable).getReason().equals("You have exceeded the rate limit!"))
                .verify();
    }

    @Test
    void testCustomRateLimitHandlerWhenNotRateLimitException() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        Exception ex = new RuntimeException("Some other exception");

        Mono<Void> result = customExceptionHandler.customRateLimitHandler().handle(exchange, ex);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Some other exception"))
                .verify();
    }
}
