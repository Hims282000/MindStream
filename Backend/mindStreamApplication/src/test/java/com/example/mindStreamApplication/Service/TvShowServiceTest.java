package com.example.mindStreamApplication.Service;

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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("TvShowService Tests")
class TvShowServiceTest {

    @Mock
    private TvShowRepository tvShowRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private FavoriteService favoriteService;

    @InjectMocks
    private TvShowService tvShowService;

    private TvShow testTvShow1;
    private TvShow testTvShow2;

    @BeforeEach
    void setUp() {
        testTvShow1 = new TvShow("Breaking Bad", 2008, "1");
        testTvShow1.setId(1L);

        testTvShow2 = new TvShow("Game of Thrones", 2011, "2");
        testTvShow2.setId(2L);
    }

    @Nested
    @DisplayName("GetAllTvShows Tests")
    class GetAllTvShowsTests {

        @Test
        @DisplayName("Should return all TV shows")
        void getAllTvShows_ShouldReturnAllShows() {
            // Arrange
            List<TvShow> tvShows = Arrays.asList(testTvShow1, testTvShow2);
            when(tvShowRepository.findAll()).thenReturn(tvShows);
            when(favoriteRepository.existsByUserIdAndTvShowId(any(), any())).thenReturn(false);

            // Act
            List<Map<String, Object>> result = tvShowService.getAllTvShows(null);

            // Assert
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Should return empty list when no TV shows")
        void getAllTvShows_WhenNoShows_ShouldReturnEmptyList() {
            // Arrange
            when(tvShowRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<Map<String, Object>> result = tvShowService.getAllTvShows(null);

            // Assert
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should include favorite status when userId provided")
        void getAllTvShows_WithUserId_ShouldIncludeFavoriteStatus() {
            // Arrange
            List<TvShow> tvShows = Arrays.asList(testTvShow1);
            when(tvShowRepository.findAll()).thenReturn(tvShows);
            when(favoriteRepository.existsByUserIdAndTvShowId(1L, 1L)).thenReturn(true);

            // Act
            List<Map<String, Object>> result = tvShowService.getAllTvShows(1L);

            // Assert
            assertEquals(1, result.size());
            assertTrue((Boolean) result.get(0).get("isFavorite"));
        }

        @Test
        @DisplayName("Should return isFavorite as false when userId is null")
        void getAllTvShows_WithNullUserId_ShouldReturnIsFavoriteAsFalse() {
            // Arrange
            List<TvShow> tvShows = Arrays.asList(testTvShow1);
            when(tvShowRepository.findAll()).thenReturn(tvShows);

            // Act
            List<Map<String, Object>> result = tvShowService.getAllTvShows(null);

            // Assert
            assertFalse((Boolean) result.get(0).get("isFavorite"));
        }
    }

    @Nested
    @DisplayName("GetTvShowById Tests")
    class GetTvShowByIdTests {

        @Test
        @DisplayName("Should return TV show by id")
        void getTvShowById_WhenExists_ShouldReturnShow() {
            // Arrange
            when(tvShowRepository.findById(1L)).thenReturn(Optional.of(testTvShow1));
            when(favoriteRepository.existsByUserIdAndTvShowId(any(), any())).thenReturn(false);

            // Act
            Map<String, Object> result = tvShowService.getTvShowById(1L, null);

            // Assert
            assertEquals(1L, result.get("id"));
            assertEquals("Breaking Bad", result.get("album"));
        }

        @Test
        @DisplayName("Should throw exception when TV show not found")
        void getTvShowById_WhenNotFound_ShouldThrowException() {
            // Arrange
            when(tvShowRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(
                ResourceNotFoundException.class,
                () -> tvShowService.getTvShowById(999L, null)
            );
        }

        @Test
        @DisplayName("Should include favorite status with userId")
        void getTvShowById_WithUserId_ShouldIncludeFavoriteStatus() {
            // Arrange
            when(tvShowRepository.findById(1L)).thenReturn(Optional.of(testTvShow1));
            when(favoriteRepository.existsByUserIdAndTvShowId(1L, 1L)).thenReturn(true);

            // Act
            Map<String, Object> result = tvShowService.getTvShowById(1L, 1L);

            // Assert
            assertTrue((Boolean) result.get("isFavorite"));
        }
    }

    @Nested
    @DisplayName("SearchTvShows Tests")
    class SearchTvShowsTests {

        @Test
        @DisplayName("Should search TV shows by album name")
        void searchTvShows_ShouldReturnMatchingShows() {
            // Arrange
            List<TvShow> tvShows = Arrays.asList(testTvShow1);
            when(tvShowRepository.findByAlbumContaining("Breaking")).thenReturn(tvShows);
            when(favoriteRepository.existsByUserIdAndTvShowId(any(), any())).thenReturn(false);

            // Act
            List<Map<String, Object>> result = tvShowService.searchTvShows("Breaking", null);

            // Assert
            assertEquals(1, result.size());
            assertEquals("Breaking Bad", result.get(0).get("album"));
        }

        @Test
        @DisplayName("Should return empty list when no matches")
        void searchTvShows_WhenNoMatches_ShouldReturnEmptyList() {
            // Arrange
            when(tvShowRepository.findByAlbumContaining("Nonexistent")).thenReturn(Collections.emptyList());

            // Act
            List<Map<String, Object>> result = tvShowService.searchTvShows("Nonexistent", null);

            // Assert
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("GetTvShowsByYear Tests")
    class GetTvShowsByYearTests {

        @Test
        @DisplayName("Should return TV shows by year")
        void getTvShowsByYear_ShouldReturnShowsFromYear() {
            // Arrange
            List<TvShow> tvShows = Arrays.asList(testTvShow2);
            when(tvShowRepository.findByYear(2011)).thenReturn(tvShows);
            when(favoriteRepository.existsByUserIdAndTvShowId(any(), any())).thenReturn(false);

            // Act
            List<Map<String, Object>> result = tvShowService.getTvShowsByYear(2011, null);

            // Assert
            assertEquals(1, result.size());
            assertEquals(2011, result.get(0).get("year"));
        }

        @Test
        @DisplayName("Should return empty list when no shows in year")
        void getTvShowsByYear_WhenNoShowsInYear_ShouldReturnEmptyList() {
            // Arrange
            when(tvShowRepository.findByYear(2000)).thenReturn(Collections.emptyList());

            // Act
            List<Map<String, Object>> result = tvShowService.getTvShowsByYear(2000, null);

            // Assert
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("AddTvShow Tests")
    class AddTvShowTests {

        @Test
        @DisplayName("Should add new TV show successfully")
        void addTvShow_WhenValid_ShouldReturnSuccessResponse() {
            // Arrange
            when(tvShowRepository.existsByAlbum("New Show")).thenReturn(false);
            when(tvShowRepository.save(any(TvShow.class))).thenAnswer(invocation -> {
                TvShow saved = invocation.getArgument(0);
                saved.setId(100L);
                return saved;
            });

            // Act
            Map<String, Object> result = tvShowService.addTvShow("New Show", 2020, "10");

            // Assert
            assertTrue((Boolean) result.get("success"));
            assertEquals("TV show added successfully", result.get("message"));
        }

        @Test
        @DisplayName("Should throw exception when album already exists")
        void addTvShow_WhenAlbumExists_ShouldThrowException() {
            // Arrange
            when(tvShowRepository.existsByAlbum("Breaking Bad")).thenReturn(true);

            // Act & Assert
            assertThrows(
                DuplicateResourceException.class,
                () -> tvShowService.addTvShow("Breaking Bad", 2008, "1")
            );
            verify(tvShowRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should handle null year")
        void addTvShow_WithNullYear_ShouldWork() {
            // Arrange
            when(tvShowRepository.existsByAlbum("No Year Show")).thenReturn(false);
            when(tvShowRepository.save(any(TvShow.class))).thenAnswer(invocation -> {
                TvShow saved = invocation.getArgument(0);
                saved.setId(100L);
                return saved;
            });

            // Act
            Map<String, Object> result = tvShowService.addTvShow("No Year Show", null, "5");

            // Assert
            assertTrue((Boolean) result.get("success"));
        }

        @Test
        @DisplayName("Should handle null chartPosition")
        void addTvShow_WithNullChartPosition_ShouldWork() {
            // Arrange
            when(tvShowRepository.existsByAlbum("No Position Show")).thenReturn(false);
            when(tvShowRepository.save(any(TvShow.class))).thenAnswer(invocation -> {
                TvShow saved = invocation.getArgument(0);
                saved.setId(100L);
                return saved;
            });

            // Act
            Map<String, Object> result = tvShowService.addTvShow("No Position Show", 2020, null);

            // Assert
            assertTrue((Boolean) result.get("success"));
        }
    }

    @Nested
    @DisplayName("LoadInitialData Tests")
    class LoadInitialDataTests {

        @Test
        @DisplayName("Should load initial data when repository is empty")
        void loadInitialData_WhenEmpty_ShouldLoadData() {
            // Arrange
            when(tvShowRepository.count()).thenReturn(0L);

            // Act
            tvShowService.loadInitialData();

            // Assert
            verify(tvShowRepository).saveAll(any());
        }

        @Test
        @DisplayName("Should not load data when repository has data")
        void loadInitialData_WhenNotEmpty_ShouldNotLoadData() {
            // Arrange
            when(tvShowRepository.count()).thenReturn(10L);

            // Act
            tvShowService.loadInitialData();

            // Assert
            verify(tvShowRepository, never()).saveAll(any());
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle special characters in search query")
        void searchTvShows_WithSpecialCharacters_ShouldWork() {
            // Arrange
            when(tvShowRepository.findByAlbumContaining("Show: #1")).thenReturn(Collections.emptyList());

            // Act
            List<Map<String, Object>> result = tvShowService.searchTvShows("Show: #1", null);

            // Assert
            assertTrue(result.isEmpty());
            verify(tvShowRepository).findByAlbumContaining("Show: #1");
        }

        @Test
        @DisplayName("Should handle empty search query")
        void searchTvShows_WithEmptyQuery_ShouldWork() {
            // Arrange
            List<TvShow> allShows = Arrays.asList(testTvShow1, testTvShow2);
            when(tvShowRepository.findByAlbumContaining("")).thenReturn(allShows);
            when(favoriteRepository.existsByUserIdAndTvShowId(any(), any())).thenReturn(false);

            // Act
            List<Map<String, Object>> result = tvShowService.searchTvShows("", null);

            // Assert
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Should handle negative year")
        void getTvShowsByYear_WithNegativeYear_ShouldWork() {
            // Arrange
            when(tvShowRepository.findByYear(-1)).thenReturn(Collections.emptyList());

            // Act
            List<Map<String, Object>> result = tvShowService.getTvShowsByYear(-1, null);

            // Assert
            assertTrue(result.isEmpty());
        }
    }
}
