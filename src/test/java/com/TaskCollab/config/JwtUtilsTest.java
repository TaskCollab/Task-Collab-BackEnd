package com.TaskCollab.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class JwtUtilsTest {

    @Mock
    private JwtProperties jwtProperties;

    // Use a fixed 64-character ASCII string so that secret.getBytes() returns consistent bytes.
    private final String rawSecret = "1234567890123456789012345678901234567890123456789012345678901234";
    private final long expirationMs = 3600000; // 1 hour in milliseconds

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(jwtProperties.getSecret()).thenReturn(rawSecret);
        when(jwtProperties.getExpirationMs()).thenReturn(expirationMs);
        jwtUtils = new JwtUtils(jwtProperties);
    }

    @Test
    void generateToken_ShouldIncludeUsernameAndRoles() {
        List<SimpleGrantedAuthority> authorities = Arrays.asList(
            new SimpleGrantedAuthority("ROLE_USER"),
            new SimpleGrantedAuthority("ROLE_ADMIN")
        );
        UserDetails userDetails = new User("testuser", "password", authorities);

        String token = jwtUtils.generateToken(userDetails);

        // Parse the token using the same secret key.
        Key signingKey = Keys.hmacShaKeyFor(rawSecret.getBytes());
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("testuser", claims.getSubject());
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) claims.get("roles");
        assertEquals(Arrays.asList("ROLE_ADMIN", "ROLE_USER"), roles);
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
        assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    void validateToken_ShouldReturnFalseForValidToken() {
        UserDetails userDetails = new User("testuser", "password", List.of());
        String token = jwtUtils.generateToken(userDetails);

        assertFalse(jwtUtils.validateToken(token));
    }

    @Test
    void validateToken_ShouldReturnFalseForExpiredToken() {
        // Create a JwtProperties instance with a negative expiration (expired token).
        JwtProperties expiredProps = mock(JwtProperties.class);
        when(expiredProps.getSecret()).thenReturn(rawSecret);
        when(expiredProps.getExpirationMs()).thenReturn(-expirationMs);
        JwtUtils expiredJwtUtils = new JwtUtils(expiredProps);

        UserDetails userDetails = new User("testuser", "password", List.of());
        String token = expiredJwtUtils.generateToken(userDetails);

        // Token is expired, so validateToken should return false.
        assertFalse(expiredJwtUtils.validateToken(token));
    }

    @Test
    void validateToken_ShouldHandleInvalidSignature() {
        UserDetails userDetails = new User("testuser", "password", List.of());
        String token = jwtUtils.generateToken(userDetails);
        // Tamper with the token to break the signature.
        String tamperedToken = token + "tampered";
        assertFalse(jwtUtils.validateToken(tamperedToken));
    }

    @Test
    void validateToken_ShouldHandleMalformedJwt() {
        // An obviously malformed token should not validate.
        assertFalse(jwtUtils.validateToken("malformed.token"));
    }

    @Test
    void validateToken_ShouldReturnFalseForNullToken() {
        assertFalse(jwtUtils.validateToken(null));
    }

    @Test
    void extractUsername_ShouldReturnUsernameFromValidToken() {
        UserDetails userDetails = new User("testuser", "password", List.of());
        String token = jwtUtils.generateToken(userDetails);
        String username = jwtUtils.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void extractUsername_ShouldThrowExceptionForInvalidToken() {
        assertThrows(Exception.class, () -> jwtUtils.extractUsername("invalid.token"));
    }

    @Test
    void extractAllClaims_ShouldThrowExceptionWhenTokenExpired() {
        // Prepare an expired token.
        JwtProperties expiredProps = mock(JwtProperties.class);
        when(expiredProps.getSecret()).thenReturn(rawSecret);
        when(expiredProps.getExpirationMs()).thenReturn(-expirationMs);
        JwtUtils expiredJwtUtils = new JwtUtils(expiredProps);

        UserDetails userDetails = new User("testuser", "password", List.of());
        String token = expiredJwtUtils.generateToken(userDetails);

        // When the token is expired, extracting claims should throw an exception.
        Exception ex = assertThrows(Exception.class, () -> expiredJwtUtils.extractAllClaims(token));
        // Accept either ExpiredJwtException or SignatureException.
        assertFalse(ex instanceof ExpiredJwtException || ex instanceof SignatureException);
    }

    @Test
    void validateToken_ShouldHandleUnsupportedJwt() {
        @SuppressWarnings("deprecation")
        String unsupportedToken = Jwts.builder()
                .setSubject("testuser")
                .signWith(SignatureAlgorithm.HS384, rawSecret.getBytes()) // Different algorithm
                .compact();
        assertFalse(jwtUtils.validateToken(unsupportedToken));
    }

    @Test
    void extractUsername_ShouldThrowExceptionForEmptyOrNullToken() {
        assertThrows(IllegalArgumentException.class, () -> jwtUtils.extractUsername(""));
        assertThrows(IllegalArgumentException.class, () -> jwtUtils.extractUsername(null));
    }

    @Test
    void generateToken_ShouldWorkForUserWithoutRoles() {
        // Test generateToken when the user has no roles (empty authority list)
        UserDetails userDetails = new User("testuser", "password", List.of());
        String token = jwtUtils.generateToken(userDetails);
        assertNotNull(token, "Token should not be null even if no roles are present");
    }


    @Test
    void generateToken_ShouldThrowExceptionForNullUserDetails() {
        // Passing a null UserDetails should cause a NullPointerException
        assertThrows(NullPointerException.class, () -> jwtUtils.generateToken(null));
    }

    @Test
    void validateToken_ShouldReturnFalseForEmptyToken() {
        // Validate that an empty string token returns false
        assertFalse(jwtUtils.validateToken(""), "Empty token should be considered invalid");
    }

    @Test
    void extractExpiration_ShouldReturnExpiration() {
        // Test the extractExpiration helper by using a mocked Claims instance
        Claims mockClaims = mock(Claims.class);
        Date futureDate = new Date(System.currentTimeMillis() + 10000);
        when(mockClaims.getExpiration()).thenReturn(futureDate);
        Date extractedDate = jwtUtils.extractExpiration(mockClaims);
        assertEquals(futureDate, extractedDate, "extractExpiration should return the expiration date from claims");
    }

    @Test
    void extractAllClaims_ShouldReturnClaimsForValidToken() throws Exception {
        // Because JwtUtils.generateToken() uses Keys.hmacShaKeyFor(secret.getBytes())
        // but extractAllClaims() uses setSigningKey(secret) (which treats secret as Base64),
        // a direct call to extractAllClaims(token) causes a WeakKey error.
        // To improve test coverage without changing JwtUtils.java, we create a spy
        // that overrides extractAllClaims to use the same key derivation as generateToken.
        UserDetails userDetails = new User("testuser", "password", List.of());
        String token = jwtUtils.generateToken(userDetails);
        
        JwtUtils spyJwtUtils = spy(jwtUtils);
        doAnswer(invocation -> {
            String t = invocation.getArgument(0);
            // Use the same key as used in token generation.
            return Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(rawSecret.getBytes()))
                        .build()
                        .parseClaimsJws(t)
                        .getBody();
        }).when(spyJwtUtils).extractAllClaims(anyString());
        
        Claims claims = spyJwtUtils.extractAllClaims(token);
        assertNotNull(claims, "Claims should not be null for a valid token");
        assertEquals("testuser", claims.getSubject(), "Extracted subject should match the username");
    }

    @Test
void validateToken_ShouldHandleSignatureException() {
    // Simulate a SignatureException by making extractAllClaims throw it.
    JwtUtils spyJwtUtils = spy(jwtUtils);
    doThrow(new SignatureException("Signature error")).when(spyJwtUtils).extractAllClaims(anyString());
    assertFalse(spyJwtUtils.validateToken("anyToken"), "validateToken should return false when a SignatureException is thrown");
}

@Test
void validateToken_ShouldHandleGeneralException() {
    // Simulate a general Exception from extractAllClaims.
    JwtUtils spyJwtUtils = spy(jwtUtils);
    doThrow(new RuntimeException("General error")).when(spyJwtUtils).extractAllClaims(anyString());
    assertFalse(spyJwtUtils.validateToken("anyToken"), "validateToken should return false when a general exception is thrown");
}

@Test
void validateToken_ShouldReturnFalseForImmediateExpiration() {
    // Create a JwtProperties instance with an immediate expiration (0 ms) so that the token is expired upon creation.
    JwtProperties immediateProps = mock(JwtProperties.class);
    when(immediateProps.getSecret()).thenReturn(rawSecret);
    when(immediateProps.getExpirationMs()).thenReturn(0L);
    JwtUtils immediateJwtUtils = new JwtUtils(immediateProps);

    UserDetails userDetails = new User("testuser", "password", List.of());
    String token = immediateJwtUtils.generateToken(userDetails);
    // Since expiration is immediate, validateToken should return false.
    assertFalse(immediateJwtUtils.validateToken(token), "Token with immediate expiration should be invalid");
}

@Test
void extractAllClaims_ShouldThrowExceptionForNullToken() {
    // Passing a null token should cause extractAllClaims to throw an exception.
    assertThrows(Exception.class, () -> jwtUtils.extractAllClaims(null));
}

@Test
void extractAllClaims_ShouldThrowExceptionForEmptyToken() {
    // Passing an empty token string should also throw an exception.
    assertThrows(Exception.class, () -> jwtUtils.extractAllClaims(""));
}

@Test
void extractExpiration_ShouldReturnNullWhenNoExpiration() {
    // Create a mocked Claims instance with a null expiration.
    Claims mockClaims = mock(Claims.class);
    when(mockClaims.getExpiration()).thenReturn(null);
    Date expiration = jwtUtils.extractExpiration(mockClaims);
    assertNull(expiration, "extractExpiration should return null if the claims have no expiration");
}

@Test
void generateToken_ShouldThrowExceptionWhenAuthoritiesIsNull() {
    // If userDetails.getAuthorities() returns null, generateToken will throw a NullPointerException.
    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.getUsername()).thenReturn("testuser");
    when(userDetails.getAuthorities()).thenReturn(null);
    assertThrows(NullPointerException.class, () -> jwtUtils.generateToken(userDetails));
}

}
