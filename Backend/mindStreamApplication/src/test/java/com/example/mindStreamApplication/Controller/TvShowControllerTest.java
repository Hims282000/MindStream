package com.example.mindStreamApplication.Controller;

import com.example.mindStreamApplication.Exception.DuplicateResourceException;
import com.example.mindStreamApplication.Exception.GlobalExceptionHandler;
import com.example.mindStreamApplication.Exception.ResourceNotFoundException;
import com.example.mindStreamApplication.Service.TvShowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(TvShowController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("TvShowController Tests")
class TvShowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TvShowService tvShowService;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, Object> tvShowMap;
    private List<Map<String, Object>> tvShowsList;

    @BeforeEach
    void setUp() {
        tvShowMap = new HashMap<>();
        tvShowMap.put("id", 1L);
        tvShowMap.put("album", "Breaking Bad");
        tvShowMap.put("year", 2008);
        tvShowMap.put("chartPosition", "1");
        tvShowMap.put("isFavorite", false);

        Map<String, Object> tvShowMap2 = new HashMap<>();
        tvShowMap2.put("id", 2L);
        tvShowMap2.put("album", "Game of Thrones");
        tvShowMap2.put("year", 2011);
        tvShowMap2.put("chartPosition", "2");
        tvShowMap2.put("isFavorite", false);

        tvShowsList = Arrays.asList(tvShowMap, tvShowMap2);
    }

    @Nested
    @DisplayName("GET /tvshows Tests")
    class GetAllTvShowsTests {

        @Test
        @WithMockUser
        @DisplayName("Should return all TV shows")
        void getAllTvShows_ShouldReturnAllShows() throws Exception {
            // Arrange
            when(tvShowService.getAllTvShows(null)).thenReturn(tvShowsList);

            // Act & Assert
            mockMvc.perform(get("/tvshows"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.count").value(2))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].album").value("Breaking Bad"));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return TV shows with user favorite status")
        void getAllTvShows_WithUserId_ShouldIncludeFavoriteStatus() throws Exception {
            // Arrange
            tvShowMap.put("isFavorite", true);
            when(tvShowService.getAllTvShows(1L)).thenReturn(Arrays.asList(tvShowMap));

            // Act & Assert
            mockMvc.perform(get("/tvshows")
                    .param("userId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].isFavorite").value(true));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return empty list when no TV shows")
        void getAllTvShows_WhenNoShows_ShouldReturnEmptyList() throws Exception {
            // Arrange
            when(tvShowService.getAllTvShows(null)).thenReturn(Collections.emptyList());

            // Act & Assert
            mockMvc.perform(get("/tvshows"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.count").value(0))
                    .andExpect(jsonPath("$.data").isEmpty());
        }
    }

    @Nested
    @DisplayName("GET /tvshows/{id} Tests")
    class GetTvShowByIdTests {

        @Test
        @WithMockUser
        @DisplayName("Should return TV show by id")
        void getTvShowById_WhenExists_ShouldReturnShow() throws Exception {
            // Arrange
            when(tvShowService.getTvShowById(1L, null)).thenReturn(tvShowMap);

            // Act & Assert
            mockMvc.perform(get("/tvshows/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.album").value("Breaking Bad"));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return not found when TV show not exists")
        void getTvShowById_WhenNotExists_ShouldReturnNotFound() throws Exception {
            // Arrange
            when(tvShowService.getTvShowById(999L, null))
                    .thenThrow(new ResourceNotFoundException("TV show", "id", 999L));

            // Act & Assert
            mockMvc.perform(get("/tvshows/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @WithMockUser
        @DisplayName("Should include favorite status when userId provided")
        void getTvShowById_WithUserId_ShouldIncludeFavoriteStatus() throws Exception {
            // Arrange
            tvShowMap.put("isFavorite", true);
            when(tvShowService.getTvShowById(1L, 1L)).thenReturn(tvShowMap);

            // Act & Assert
            mockMvc.perform(get("/tvshows/1")
                    .param("userId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.isFavorite").value(true));
        }
    }

    @Nested
    @DisplayName("GET /tvshows/search Tests")
    class SearchTvShowsTests {

        @Test
        @WithMockUser
        @DisplayName("Should search TV shows by query")
        void searchTvShows_ShouldReturnMatchingShows() throws Exception {
            // Arrange
            when(tvShowService.searchTvShows("Breaking", null)).thenReturn(Arrays.asList(tvShowMap));

            // Act & Assert
            mockMvc.perform(get("/tvshows/search")
                    .param("query", "Breaking"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.count").value(1))
                    .andExpect(jsonPath("$.message").value("Search results for: Breaking"));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return empty list for no matches")
        void searchTvShows_WhenNoMatches_ShouldReturnEmptyList() throws Exception {
            // Arrange
            when(tvShowService.searchTvShows("Nonexistent", null)).thenReturn(Collections.emptyList());

            // Act & Assert
            mockMvc.perform(get("/tvshows/search")
                    .param("query", "Nonexistent"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(0))
                    .andExpect(jsonPath("$.data").isEmpty());
        }
    }

    @Nested
    @DisplayName("GET /tvshows/year/{year} Tests")
    class GetTvShowsByYearTests {

        @Test
        @WithMockUser
        @DisplayName("Should return TV shows by year")
        void getTvShowsByYear_ShouldReturnShowsFromYear() throws Exception {
            // Arrange
            when(tvShowService.getTvShowsByYear(2008, null)).thenReturn(Arrays.asList(tvShowMap));

            // Act & Assert
            mockMvc.perform(get("/tvshows/year/2008"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("TV shows from year: 2008"))
                    .andExpect(jsonPath("$.data[0].year").value(2008));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return empty list for year with no shows")
        void getTvShowsByYear_WhenNoShowsInYear_ShouldReturnEmptyList() throws Exception {
            // Arrange
            when(tvShowService.getTvShowsByYear(2000, null)).thenReturn(Collections.emptyList());

            // Act & Assert
            mockMvc.perform(get("/tvshows/year/2000"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(0))
                    .andExpect(jsonPath("$.data").isEmpty());
        }
    }

    @Nested
    @DisplayName("POST /tvshows Tests")
    class AddTvShowTests {

        @Test
        @WithMockUser
        @DisplayName("Should add TV show successfully")
        void addTvShow_WithValidRequest_ShouldReturnSuccess() throws Exception {
            // Arrange
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("message", "TV show added successfully");
            successResponse.put("tvShow", tvShowMap);

            when(tvShowService.addTvShow("New Show", 2020, "10")).thenReturn(successResponse);

            Map<String, Object> request = new HashMap<>();
            request.put("album", "New Show");
            request.put("year", 2020);
            request.put("chartPosition", "10");

            // Act & Assert
            mockMvc.perform(post("/tvshows")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("TV show added successfully"));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return bad request when album is missing")
        void addTvShow_WithMissingAlbum_ShouldReturnBadRequest() throws Exception {
            // Arrange
            Map<String, Object> request = new HashMap<>();
            request.put("year", 2020);
            request.put("chartPosition", "10");

            // Act & Assert
            mockMvc.perform(post("/tvshows")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Album name is required"));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return bad request when album is empty")
        void addTvShow_WithEmptyAlbum_ShouldReturnBadRequest() throws Exception {
            // Arrange
            Map<String, Object> request = new HashMap<>();
            request.put("album", "");
            request.put("year", 2020);

            // Act & Assert
            mockMvc.perform(post("/tvshows")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return conflict when album already exists")
        void addTvShow_WhenAlbumExists_ShouldReturnConflict() throws Exception {
            // Arrange
            when(tvShowService.addTvShow("Breaking Bad", 2008, "1"))
                    .thenThrow(new DuplicateResourceException("TV show", "album", "Breaking Bad"));

            Map<String, Object> request = new HashMap<>();
            request.put("album", "Breaking Bad");
            request.put("year", 2008);
            request.put("chartPosition", "1");

            // Act & Assert
            mockMvc.perform(post("/tvshows")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.success").value(false));
        }
    }

    @Nested
    @DisplayName("POST /tvshows/load-initial-data Tests")
    class LoadInitialDataTests {

        @Test
        @WithMockUser
        @DisplayName("Should load initial data successfully")
        void loadInitialData_ShouldReturnSuccess() throws Exception {
            // Arrange
            doNothing().when(tvShowService).loadInitialData();

            // Act & Assert
            mockMvc.perform(post("/tvshows/load-initial-data")
                    .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Initial TV show data loaded successfully"));
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @WithMockUser
        @DisplayName("Should handle special characters in search query")
        void searchTvShows_WithSpecialCharacters_ShouldWork() throws Exception {
            // Arrange
            when(tvShowService.searchTvShows("Show: #1", null)).thenReturn(Collections.emptyList());

            // Act & Assert
            mockMvc.perform(get("/tvshows/search")
                    .param("query", "Show: #1"))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        @DisplayName("Should handle null year in add request")
        void addTvShow_WithNullYear_ShouldWork() throws Exception {
            // Arrange
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("message", "TV show added successfully");

            when(tvShowService.addTvShow("No Year Show", null, null)).thenReturn(successResponse);

            Map<String, Object> request = new HashMap<>();
            request.put("album", "No Year Show");

            // Act & Assert
            mockMvc.perform(post("/tvshows")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true));
        }
    }
}
