package com.example.mindStreamApplication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Integration test to verify application context loads successfully
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Application Context Tests")
class MindStreamApplicationTests {

    @Test
    @DisplayName("Application context should load successfully")
    void contextLoads() {
        // This test verifies that the Spring application context loads without errors
        assertDoesNotThrow(() -> {});
    }
}
