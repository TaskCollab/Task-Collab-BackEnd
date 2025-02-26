package com.TaskCollab.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class JwtPropertiesTest {

    @Test
    void gettersAndSetters_ShouldSetAndReturnValues() {
        // Arrange
        JwtProperties properties = new JwtProperties();
        String expectedSecret = "super-secret-key";
        long expectedExpiration = 3600000; // 1 hour

        // Act
        properties.setSecret(expectedSecret);
        properties.setExpirationMs(expectedExpiration);

        // Assert
        assertThat(properties.getSecret()).isEqualTo(expectedSecret);
        assertThat(properties.getExpirationMs()).isEqualTo(expectedExpiration);
    }

    @Test
    void defaultValues_ShouldBeNullAndZero() {
        // Arrange
        JwtProperties properties = new JwtProperties();

        // Act & Assert
        assertThat(properties.getSecret()).isNull();
        assertThat(properties.getExpirationMs()).isZero();
    }

    @Test
    void shouldHandleDifferentExpirationValues() {
        // Arrange
        JwtProperties properties = new JwtProperties();
        
        // Test positive value
        properties.setExpirationMs(86400000L); // 24 hours
        assertThat(properties.getExpirationMs()).isEqualTo(86400000L);

        // Test negative value
        properties.setExpirationMs(-1000L);
        assertThat(properties.getExpirationMs()).isEqualTo(-1000L);
    }

    @Test
    void shouldHandleEmptySecret() {
        // Arrange
        JwtProperties properties = new JwtProperties();
        
        // Act
        properties.setSecret("");
        
        // Assert
        assertThat(properties.getSecret()).isEmpty();
    }
}