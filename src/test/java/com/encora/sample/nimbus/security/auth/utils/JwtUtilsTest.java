package com.encora.sample.nimbus.security.auth.utils;

import com.encora.samples.nimbus.security.auth.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {


    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();

        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "nHsRrBuVxa3p9/8LbZ9M/vtUSO/d2RUDU1g11D53+qwWKwJ3mlICtUrnQo5g3f3GNsQPyUG3kGSpmW4XdMEBKA==");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 30);
    }

    @Test
    void testGenerateJwtToken() {
        String userId = "1";
        String token = jwtUtils.generateJwtToken(userId);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJhbGciOiJIUzUxMiJ9"));
    }

    @Test
    void testGetUserIdFromJwtToken() {
        String userId = "1";
        String token = jwtUtils.generateJwtToken(userId);

        String extractedUserId = jwtUtils.getUserIdFromJwtToken(token);

        assertEquals(userId, extractedUserId);
    }

    @Test
    void testValidateJwtTokenValid() {
        String userId = "1";
        String token = jwtUtils.generateJwtToken(userId);

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertTrue(isValid);
    }

    @Test
    void testValidateJwtTokenInvalid() {
        String token = "invalidToken";

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }

    @Test
    void testValidateJwtTokenExpired() {
        String userId = "1";
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 0); // Set expiration to 0 minutes
        String token = jwtUtils.generateJwtToken(userId);

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }
}

