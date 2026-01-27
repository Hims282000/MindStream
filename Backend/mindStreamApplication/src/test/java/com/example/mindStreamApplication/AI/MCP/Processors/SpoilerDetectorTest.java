package com.example.mindStreamApplication.AI.MCP.Processors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpoilerDetectorTest {

    @Autowired
    private SpoilerDetector spoilerDetector;

    @Test
    void testContainsSpoilers_WithDeathKeyword_ReturnsTrue() {
        // Given
        String textWithSpoiler = "The main character dies at the end.";

        // When & Then
        assertTrue(spoilerDetector.containsSpoilers(textWithSpoiler));
    }

    @Test
    void testContainsSpoilers_WithTwistKeyword_ReturnsTrue() {
        // Given
        String textWithSpoiler = "The movie has an amazing plot twist in the finale.";

        // When & Then
        assertTrue(spoilerDetector.containsSpoilers(textWithSpoiler));
    }

    @Test
    void testContainsSpoilers_WithEndingKeyword_ReturnsTrue() {
        // Given
        String textWithSpoiler = "The ending reveals everything.";

        // When & Then
        assertTrue(spoilerDetector.containsSpoilers(textWithSpoiler));
    }

    @Test
    void testContainsSpoilers_NoSpoilers_ReturnsFalse() {
        // Given
        String cleanText = "A thrilling adventure movie with great action scenes.";

        // When & Then
        assertFalse(spoilerDetector.containsSpoilers(cleanText));
    }

    @Test
    void testContainsSpoilers_NullInput_ReturnsFalse() {
        // When & Then
        assertFalse(spoilerDetector.containsSpoilers(null));
    }

    @Test
    void testContainsSpoilers_CaseInsensitive_ReturnsTrue() {
        // Given
        String textWithUppercase = "The MAIN CHARACTER DIES at the end.";

        // When & Then
        assertTrue(spoilerDetector.containsSpoilers(textWithUppercase));
    }

    @Test
    void testRemoveSpoilers_RemovesDeathKeyword() {
        // Given
        String textWithSpoiler = "The main character dies at the end.";

        // When
        String result = spoilerDetector.removeSpoilers(textWithSpoiler);

        // Then
        assertFalse(result.contains("dies"));
        assertTrue(result.contains("[spoiler removed]"));
    }

    @Test
    void testRemoveSpoilers_RemovesMultipleSpoilers() {
        // Given
        String textWithMultipleSpoilers = "The twist is that the villain kills the hero at the ending.";

        // When
        String result = spoilerDetector.removeSpoilers(textWithMultipleSpoilers);

        // Then
        assertFalse(result.contains("twist"));
        assertFalse(result.contains("kills"));
        assertFalse(result.contains("ending"));
        assertTrue(result.contains("[spoiler removed]"));
    }

    @Test
    void testRemoveSpoilers_NullInput_ReturnsEmptyString() {
        // When
        String result = spoilerDetector.removeSpoilers(null);

        // Then
        assertEquals("", result);
    }

    @Test
    void testRemoveSpoilers_NoSpoilers_ReturnsOriginal() {
        // Given
        String cleanText = "A great adventure movie with action scenes.";

        // When
        String result = spoilerDetector.removeSpoilers(cleanText);

        // Then
        assertEquals(cleanText, result);
    }

    @Test
    void testRemoveSpoilers_CaseInsensitiveRemoval() {
        // Given
        String textWithUppercase = "The BETRAYAL was unexpected.";

        // When
        String result = spoilerDetector.removeSpoilers(textWithUppercase);

        // Then
        assertFalse(result.toLowerCase().contains("betrayal"));
        assertTrue(result.contains("[spoiler removed]"));
    }
}
