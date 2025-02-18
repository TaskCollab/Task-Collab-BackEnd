package com.TaskCollab.Security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import com.TaskCollab.Service.UserService;
import com.TaskCollab.config.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthFilterTest {

    private JwtAuthFilter jwtAuthFilter;
    
    @Mock
    private JwtUtils jwtUtils;
    
    @Mock
    private UserService userService;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthFilter = new JwtAuthFilter(jwtUtils, userService);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_NoAuthorizationHeader_SecurityContextNotSet() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_InvalidToken_SecurityContextNotSet() throws Exception {
        String token = "invalid.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.validateToken(token)).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
void doFilterInternal_ValidToken_SecurityContextSet() throws Exception {
    String token = "valid.token";
    String username = "user@test.com";
    UserDetails userDetails = new User(username, "password", 
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

    when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
    when(jwtUtils.validateToken(token)).thenReturn(true);
    when(jwtUtils.extractUsername(token)).thenReturn(username);
    when(userService.loadUserByUsername(username)).thenReturn(userDetails);

    jwtAuthFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    UsernamePasswordAuthenticationToken authentication = 
        (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    
    assertNotNull(authentication);
    assertEquals(userDetails, authentication.getPrincipal());
    
    // Compare authority strings directly instead of collection objects
    assertEquals(1, authentication.getAuthorities().size());
    assertEquals("ROLE_USER", 
        authentication.getAuthorities().iterator().next().getAuthority());
    
    assertTrue(authentication.getDetails().getClass().getName()
        .contains("WebAuthenticationDetails"));
}

    @Test
    void doFilterInternal_ExceptionThrown_LogsErrorAndContinues() throws Exception {
        String token = "exception.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.validateToken(token)).thenThrow(new RuntimeException("Token validation error"));

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_ValidTokenButUserNotFound_SecurityContextNotSet() throws Exception {
        String token = "valid.token";
        String username = "nonexistent@test.com";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        when(jwtUtils.extractUsername(token)).thenReturn(username);
        when(userService.loadUserByUsername(username))
            .thenThrow(new UsernameNotFoundException("User not found"));

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void parseJwt_ValidHeader_ReturnsToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer testToken");

        Method method = JwtAuthFilter.class.getDeclaredMethod("parseJwt", HttpServletRequest.class);
        method.setAccessible(true);
        String token = (String) method.invoke(jwtAuthFilter, request);

        assertEquals("testToken", token);
    }

    @Test
    void parseJwt_HeaderWithoutBearer_ReturnsNull() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("InvalidToken");

        Method method = JwtAuthFilter.class.getDeclaredMethod("parseJwt", HttpServletRequest.class);
        method.setAccessible(true);
        String token = (String) method.invoke(jwtAuthFilter, request);

        assertNull(token);
    }

    @Test
    void parseJwt_NoHeader_ReturnsNull() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        Method method = JwtAuthFilter.class.getDeclaredMethod("parseJwt", HttpServletRequest.class);
        method.setAccessible(true);
        String token = (String) method.invoke(jwtAuthFilter, request);

        assertNull(token);
    }
}