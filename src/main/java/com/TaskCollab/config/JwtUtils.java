package com.TaskCollab.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private final String secret;
    private final long expirationMs;

    public JwtUtils(JwtProperties jwtProperties) {
        this.secret = jwtProperties.getSecret();
        this.expirationMs = jwtProperties.getExpirationMs();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Add roles to the JWT claims
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        // Keys.hmacShaKeyFor for creating a secure signing key
        Key signingKey = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }


    private Boolean isTokenExpired(Claims claims) {
            return extractExpiration(claims).before(new Date());
    }

 

    public Date extractExpiration(Claims claims) {
        return claims.getExpiration(); // Extract expiration
    }
    
    }
}

