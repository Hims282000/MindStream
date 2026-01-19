package com.example.mindStreamApplication.Service;

import com.example.mindStreamApplication.Domain.Favorite;
import com.example.mindStreamApplication.Domain.TvShow;
import com.example.mindStreamApplication.Exception.DuplicateResourceException;
import com.example.mindStreamApplication.Exception.ResourceNotFoundException;
import com.example.mindStreamApplication.Repository.FavoriteRepository;
import com.example.mindStreamApplication.Repository.TvShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("FavoriteService Tests")
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private TvShowRepository tvShowRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private TvShow testTvShow;
    private Favorite testFavorite;

    @BeforeEach
    void setUp() {
        testTvShow = new TvShow("Breaking Bad", 2008, "1");
        testTvShow.setId(10L);

        testFavorite = new Favorite(1L, 10L);
        testFavorite.setId(100L);
        testFavorite.setAddedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("AddToFavorites Tests")
    class AddToFavoritesTests {

        @Test
        @DisplayName("Should add TV show to favorites successfully")
        void addToFavorites_WhenValid_ShouldReturnSuccessResponse() {
            // Arrange
            when(tvShowRepository.findById(10L)).thenReturn(Optional.of(testTvShow));
            when(favoriteRepository.existsByUserIdAndTvShowId(1L, 10L)).thenReturn(false);
            when(favoriteRepository.save(any(Favorite.class))).thenAnswer(invocation -> {
                Favorite saved = invocation.getArgument(0);
                saved.setId(100L);
                return saved;
            });

            // Act
            Map<String, Object> result = favoriteService.addToFavorites(1L, 10L);

            // Assert
            assertTrue((Boolean) result.get("success"));
            assertEquals("Added to favorites", result.get("message"));
            verify(favoriteRepository).save(any(Favorite.class));
        }

        @Test
        @DisplayName("Should throw exception when TV show not found")
        void addToFavorites_WhenTvShowNotFound_ShouldThrowException() {
            // Arrange
            when(tvShowRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(
                ResourceNotFoundException.class,
                () -> favoriteService.addToFavorites(1L, 999L)
            );
            verify(favoriteRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when already favorited")
        void addToFavorites_WhenAlreadyFavorited_ShouldThrowException() {
            // Arrange
            when(tvShowRepository.findById(10L)).thenReturn(Optional.of(testTvShow));
            when(favoriteRepository.existsByUserIdAndTvShowId(1L, 10L)).thenReturn(true);

            // Act & Assert
            DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> favoriteService.addToFavorites(1L, 10L)
            );
            assertTrue(exception.getMessage().contains("already exists"));
            verify(favoriteRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should set addedAt timestamp when adding favorite")
        void addToFavorites_ShouldSetAddedAtTimestamp() {
            // Arrange
            when(tvShowRepository.findById(10L)).thenReturn(Optional.of(testTvShow));
            when(favoriteRepository.existsByUserIdAndTvShowId(1L, 10L)).thenReturn(false);
            when(favoriteRepository.save(any(Favorite.class))).thenAnswer(invocation -> {
                Favorite saved = invocation.getArgument(0);
                assertNotNull(saved.getAddedAt());
                saved.setId(100L);
                return saved;
            });

            // Act
            favoriteService.addToFavorites(1L, 10L);

            // Assert
            verify(favoriteRepository).save(any(Favorite.class));
        }
    }

    @Nested
    @DisplayName("RemoveFromFavorites Tests")
    class RemoveFromFavoritesTests {

        @Test
        @DisplayName("Should remove favorite successfully")
        void removeFromFavorites_WhenExists_ShouldReturnSuccessResponse() {
            // Arrange
            when(favoriteRepository.findByUserIdAndTvShowId(1L, 10L)).thenReturn(testFavorite);
            doNothing().when(favoriteRepository).delete(testFavorite);

            // Act
            Map<String, Object> result = favoriteService.removeFromFavorites(1L, 10L);

            // Assert
            assertTrue((Boolean) result.get("success"));
            assertEquals("Removed from favorites", result.get("message"));
            verify(favoriteRepository).delete(testFavorite);
        }

        @Test
        @DisplayName("Should throw exception when favorite not found")
        void removeFromFavorites_WhenNotFound_ShouldThrowException() {
            // Arrange
            when(favoriteRepository.findByUserIdAndTvShowId(1L, 999L)).thenReturn(null);

            // Act & Assert
            assertThrows(
                ResourceNotFoundException.class,
                () -> favoriteService.removeFromFavorites(1L, 999L)
            );
            verify(favoriteRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("GetUserFavorites Tests")
    class GetUserFavoritesTests {

        @Test
        @DisplayName("Should return user favorites with TV show details")
        void getUserFavorites_ShouldReturnFavoritesWithTvShowDetails() {
            // Arrange
            List<Favorite> favorites = Arrays.asList(testFavorite);
            when(favoriteRepository.findByUserId(1L)).thenReturn(favorites);
            when(tvShowRepository.findById(10L)).thenReturn(Optional.of(testTvShow));

            // Act
            List<Map<String, Object>> result = favoriteService.getUserFavorites(1L);

            // Assert
            assertEquals(1, result.size());
            Map<String, Object> favoriteMap = result.get(0);
            assertEquals(100L, favoriteMap.get("id"));
            assertTrue((Boolean) favoriteMap.get("isFavorite"));
            assertNotNull(favoriteMap.get("tvShow"));
        }

        @Test
        @DisplayName("Should return empty list when user has no favorites")
        void getUserFavorites_WhenNoFavorites_ShouldReturnEmptyList() {
            // Arrange
            when(favoriteRepository.findByUserId(999L)).thenReturn(Collections.emptyList());

            // Act
            List<Map<String, Object>> result = favoriteService.getUserFavorites(999L);

            // Assert
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should throw exception when TV show in favorite not found")
        void getUserFavorites_WhenTvShowNotFound_ShouldThrowException() {
            // Arrange
            List<Favorite> favorites = Arrays.asList(testFavorite);
            when(favoriteRepository.findByUserId(1L)).thenReturn(favorites);
            when(tvShowRepository.findById(10L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(
                ResourceNotFoundException.class,
                () -> favoriteService.getUserFavorites(1L)
            );
        }

        @Test
        @DisplayName("Should return multiple favorites for user")
        void getUserFavorites_ShouldReturnMultipleFavorites() {
            // Arrange
            TvShow tvShow2 = new TvShow("Game of Thrones", 2011, "2");
            tvShow2.setId(20L);

            Favorite favorite2 = new Favorite(1L, 20L);
            favorite2.setId(200L);
            favorite2.setAddedAt(LocalDateTime.now());

            List<Favorite> favorites = Arrays.asList(testFavorite, favorite2);
            when(favoriteRepository.findByUserId(1L)).thenReturn(favorites);
            when(tvShowRepository.findById(10L)).thenReturn(Optional.of(testTvShow));
            when(tvShowRepository.findById(20L)).thenReturn(Optional.of(tvShow2));

            // Act
            List<Map<String, Object>> result = favoriteService.getUserFavorites(1L);

            // Assert
            assertEquals(2, result.size());
        }
    }

    @Nested
    @DisplayName("IsFavorite Tests")
    class IsFavoriteTests {

        @Test
        @DisplayName("Should return true when TV show is favorited")
        void isFavorite_WhenFavorited_ShouldReturnTrue() {
            // Arrange
            when(favoriteRepository.existsByUserIdAndTvShowId(1L, 10L)).thenReturn(true);

            // Act
            boolean result = favoriteService.isFavorite(1L, 10L);

            // Assert
            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when TV show is not favorited")
        void isFavorite_WhenNotFavorited_ShouldReturnFalse() {
            // Arrange
            when(favoriteRepository.existsByUserIdAndTvShowId(1L, 999L)).thenReturn(false);

            // Act
            boolean result = favoriteService.isFavorite(1L, 999L);

            // Assert
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("CountUserFavorites Tests")
    class CountUserFavoritesTests {

        @Test
        @DisplayName("Should return correct count of favorites")
        void countUserFavorites_ShouldReturnCorrectCount() {
            // Arrange
            when(favoriteRepository.countByUserId(1L)).thenReturn(5L);

            // Act
            long count = favoriteService.countUserFavorites(1L);

            // Assert
            assertEquals(5L, count);
        }

        @Test
        @DisplayName("Should return zero when user has no favorites")
        void countUserFavorites_WhenNoFavorites_ShouldReturnZero() {
            // Arrange
            when(favoriteRepository.countByUserId(999L)).thenReturn(0L);

            // Act
            long count = favoriteService.countUserFavorites(999L);

            // Assert
            assertEquals(0L, count);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null userId gracefully")
        void getUserFavorites_WithNullUserId_ShouldReturnEmptyList() {
            // Arrange
            when(favoriteRepository.findByUserId(null)).thenReturn(Collections.emptyList());

            // Act
            List<Map<String, Object>> result = favoriteService.getUserFavorites(null);

            // Assert
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should handle large number of favorites")
        void getUserFavorites_WithManyFavorites_ShouldWork() {
            // Arrange - this is a conceptual test, in reality would test performance
            when(favoriteRepository.countByUserId(1L)).thenReturn(1000L);

            // Act
            long count = favoriteService.countUserFavorites(1L);

            // Assert
            assertEquals(1000L, count);
        }
    }
}
