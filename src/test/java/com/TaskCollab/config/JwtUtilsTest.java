package com.TaskCollab.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private Key secretKey;
    private String encodedSecretKey;

    @Mock
    private JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Generate a secure key
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        encodedSecretKey = java.util.Base64.getEncoder().encodeToString(secretKey.getEncoded());
        when(jwtProperties.getSecret()).thenReturn(encodedSecretKey);
        when(jwtProperties.getExpirationMs()).thenReturn(3600000L);
        jwtUtils = new JwtUtils(jwtProperties);
    }

    @Test
    void testGenerateToken() {
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("CREATE"));
        UserDetails userDetails = new User("testUser", "password", authorities);
        String token = jwtUtils.generateToken(userDetails);
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testExtractUsername() {
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("CREATE"));
        UserDetails userDetails = new User("testUser", "password", authorities);
        String token = jwtUtils.generateToken(userDetails);
        String username = jwtUtils.extractUsername(token);
        assertEquals("testUser", username);
    }

    @Test
    void testValidateToken() {
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("CREATE"));
        UserDetails userDetails = new User("testUser", "password", authorities);
        String token = jwtUtils.generateToken(userDetails);
        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    void testValidateExpiredToken() throws InterruptedException {
        JwtUtils expiredJwtUtils = new JwtUtils(new JwtProperties() {{
            Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
            setSecret(java.util.Base64.getEncoder().encodeToString(secretKey.getEncoded()));
            setExpirationMs(100L); // Very short expiration
        }});

        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("CREATE"));
        UserDetails userDetails = new User("testUser", "password", authorities);

        String token = expiredJwtUtils.generateToken(userDetails);
        Thread.sleep(200L); // Wait for token to expire

        assertFalse(expiredJwtUtils.validateToken(token));
    }

}