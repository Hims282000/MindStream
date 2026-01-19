package com.example.mindStreamApplication.Domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Favorite Entity Tests")
class FavoriteTest {

    private Favorite favorite;

    @BeforeEach
    void setUp() {
        favorite = new Favorite();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Default constructor should create empty Favorite")
        void defaultConstructor_ShouldCreateEmptyFavorite() {
            Favorite emptyFavorite = new Favorite();
            
            assertNotNull(emptyFavorite);
            assertNull(emptyFavorite.getId());
            assertNull(emptyFavorite.getUserId());
            assertNull(emptyFavorite.getTvShowId());
            assertNull(emptyFavorite.getAddedAt());
        }

        @Test
        @DisplayName("Parameterized constructor should set userId, tvShowId, and addedAt")
        void parameterizedConstructor_ShouldSetFields() {
            LocalDateTime beforeCreation = LocalDateTime.now();
            Favorite newFavorite = new Favorite(1L, 10L);
            LocalDateTime afterCreation = LocalDateTime.now();
            
            assertEquals(1L, newFavorite.getUserId());
            assertEquals(10L, newFavorite.getTvShowId());
            assertNotNull(newFavorite.getAddedAt());
            assertTrue(newFavorite.getAddedAt().isAfter(beforeCreation.minusSeconds(1)));
            assertTrue(newFavorite.getAddedAt().isBefore(afterCreation.plusSeconds(1)));
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get Id correctly")
        void shouldSetAndGetId() {
            favorite.setId(1L);
            assertEquals(1L, favorite.getId());
        }

        @Test
        @DisplayName("Should set and get UserId correctly")
        void shouldSetAndGetUserId() {
            favorite.setUserId(100L);
            assertEquals(100L, favorite.getUserId());
        }

        @Test
        @DisplayName("Should set and get TvShowId correctly")
        void shouldSetAndGetTvShowId() {
            favorite.setTvShowId(200L);
            assertEquals(200L, favorite.getTvShowId());
        }

        @Test
        @DisplayName("Should set and get AddedAt correctly")
        void shouldSetAndGetAddedAt() {
            LocalDateTime now = LocalDateTime.now();
            favorite.setAddedAt(now);
            assertEquals(now, favorite.getAddedAt());
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("ToString should contain all Favorite fields")
        void toString_ShouldContainAllFields() {
            LocalDateTime addedTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
            favorite.setId(1L);
            favorite.setUserId(10L);
            favorite.setTvShowId(20L);
            favorite.setAddedAt(addedTime);

            String result = favorite.toString();

            assertTrue(result.contains("id=1"));
            assertTrue(result.contains("userId=10"));
            assertTrue(result.contains("tvShowId=20"));
            assertTrue(result.contains("addedAt="));
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null UserId")
        void shouldHandleNullUserId() {
            favorite.setUserId(null);
            assertNull(favorite.getUserId());
        }

        @Test
        @DisplayName("Should handle null TvShowId")
        void shouldHandleNullTvShowId() {
            favorite.setTvShowId(null);
            assertNull(favorite.getTvShowId());
        }

        @Test
        @DisplayName("Should handle null AddedAt")
        void shouldHandleNullAddedAt() {
            favorite.setAddedAt(null);
            assertNull(favorite.getAddedAt());
        }

        @Test
        @DisplayName("Should handle large ID values")
        void shouldHandleLargeIdValues() {
            Long largeId = Long.MAX_VALUE;
            favorite.setId(largeId);
            favorite.setUserId(largeId);
            favorite.setTvShowId(largeId);

            assertEquals(largeId, favorite.getId());
            assertEquals(largeId, favorite.getUserId());
            assertEquals(largeId, favorite.getTvShowId());
        }

        @Test
        @DisplayName("Should handle past date for AddedAt")
        void shouldHandlePastDate() {
            LocalDateTime pastDate = LocalDateTime.of(2020, 1, 1, 0, 0);
            favorite.setAddedAt(pastDate);
            assertEquals(pastDate, favorite.getAddedAt());
        }
    }
}
