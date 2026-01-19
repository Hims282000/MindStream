package com.example.mindStreamApplication.Controller;

import com.example.mindStreamApplication.Exception.DuplicateResourceException;
import com.example.mindStreamApplication.Exception.GlobalExceptionHandler;
import com.example.mindStreamApplication.Exception.ResourceNotFoundException;
import com.example.mindStreamApplication.Service.FavoriteService;
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

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(FavoriteController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("FavoriteController Tests")
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, Object> successResponse;
    private List<Map<String, Object>> favoritesList;

    @BeforeEach
    void setUp() {
        successResponse = new HashMap<>();
        successResponse.put("success", true);
        successResponse.put("message", "Operation successful");

        Map<String, Object> favoriteItem = new HashMap<>();
        favoriteItem.put("id", 1L);
        favoriteItem.put("tvShow", Map.of(
            "id", 10L,
            "album", "Breaking Bad",
            "year", 2008,
            "chartPosition", "1"
        ));
        favoriteItem.put("addedAt", LocalDateTime.now().toString());
        favoriteItem.put("isFavorite", true);

        favoritesList = Arrays.asList(favoriteItem);
    }

    @Nested
    @DisplayName("GET /favorites/user/{userId} Tests")
    class GetUserFavoritesTests {

        @Test
        @WithMockUser
        @DisplayName("Should return user favorites successfully")
        void getUserFavorites_ShouldReturnFavorites() throws Exception {
            // Arrange
            when(favoriteService.getUserFavorites(1L)).thenReturn(favoritesList);
            when(favoriteService.countUserFavorites(1L)).thenReturn(1L);

            // Act & Assert
            mockMvc.perform(get("/favorites/user/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.count").value(1))
                    .andExpect(jsonPath("$.data").isArray());
        }

        @Test
        @WithMockUser
        @DisplayName("Should return empty list for user with no favorites")
        void getUserFavorites_WhenNoFavorites_ShouldReturnEmptyList() throws Exception {
            // Arrange
            when(favoriteService.getUserFavorites(999L)).thenReturn(Collections.emptyList());
            when(favoriteService.countUserFavorites(999L)).thenReturn(0L);

            // Act & Assert
            mockMvc.perform(get("/favorites/user/999"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.count").value(0))
                    .andExpect(jsonPath("$.data").isEmpty());
        }
    }

    @Nested
    @DisplayName("POST /favorites/add Tests")
    class AddToFavoritesTests {

        @Test
        @WithMockUser
        @DisplayName("Should add to favorites successfully")
        void addToFavorites_WithValidRequest_ShouldReturnSuccess() throws Exception {
            // Arrange
            successResponse.put("message", "Added to favorites");
            successResponse.put("favoriteId", 100L);
            when(favoriteService.addToFavorites(1L, 10L)).thenReturn(successResponse);

            Map<String, Long> request = new HashMap<>();
            request.put("userId", 1L);
            request.put("tvShowId", 10L);

            // Act & Assert
            mockMvc.perform(post("/favorites/add")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Added to favorites"));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return bad request when userId is missing")
        void addToFavorites_WithMissingUserId_ShouldReturnBadRequest() throws Exception {
            // Arrange
            Map<String, Long> request = new HashMap<>();
            request.put("tvShowId", 10L);

            // Act & Assert
            mockMvc.perform(post("/favorites/add")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("userId and tvShowId are required"));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return bad request when tvShowId is missing")
        void addToFavorites_WithMissingTvShowId_ShouldReturnBadRequest() throws Exception {
            // Arrange
            Map<String, Long> request = new HashMap<>();
            request.put("userId", 1L);

            // Act & Assert
            mockMvc.perform(post("/favorites/add")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return conflict when already favorited")
        void addToFavorites_WhenAlreadyFavorited_ShouldReturnConflict() throws Exception {
            // Arrange
            when(favoriteService.addToFavorites(1L, 10L))
                    .thenThrow(new DuplicateResourceException("Favorite already exists"));

            Map<String, Long> request = new HashMap<>();
            request.put("userId", 1L);
            request.put("tvShowId", 10L);

            // Act & Assert
            mockMvc.perform(post("/favorites/add")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return not found when TV show not exists")
        void addToFavorites_WhenTvShowNotExists_ShouldReturnNotFound() throws Exception {
            // Arrange
            when(favoriteService.addToFavorites(1L, 999L))
                    .thenThrow(new ResourceNotFoundException("TV show", "id", 999L));

            Map<String, Long> request = new HashMap<>();
            request.put("userId", 1L);
            request.put("tvShowId", 999L);

            // Act & Assert
            mockMvc.perform(post("/favorites/add")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false));
        }
    }

    @Nested
    @DisplayName("DELETE /favorites/remove Tests")
    class RemoveFromFavoritesTests {

        @Test
        @WithMockUser
        @DisplayName("Should remove from favorites successfully")
        void removeFromFavorites_WithValidRequest_ShouldReturnSuccess() throws Exception {
            // Arrange
            successResponse.put("message", "Removed from favorites");
            when(favoriteService.removeFromFavorites(1L, 10L)).thenReturn(successResponse);

            Map<String, Long> request = new HashMap<>();
            request.put("userId", 1L);
            request.put("tvShowId", 10L);

            // Act & Assert
            mockMvc.perform(delete("/favorites/remove")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Removed from favorites"));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return bad request when required fields missing")
        void removeFromFavorites_WithMissingFields_ShouldReturnBadRequest() throws Exception {
            // Arrange
            Map<String, Long> request = new HashMap<>();

            // Act & Assert
            mockMvc.perform(delete("/favorites/remove")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return not found when favorite not exists")
        void removeFromFavorites_WhenNotExists_ShouldReturnNotFound() throws Exception {
            // Arrange
            when(favoriteService.removeFromFavorites(1L, 999L))
                    .thenThrow(new ResourceNotFoundException("Favorite not found"));

            Map<String, Long> request = new HashMap<>();
            request.put("userId", 1L);
            request.put("tvShowId", 999L);

            // Act & Assert
            mockMvc.perform(delete("/favorites/remove")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false));
        }
    }

    @Nested
    @DisplayName("GET /favorites/check Tests")
    class CheckFavoriteTests {

        @Test
        @WithMockUser
        @DisplayName("Should return true when TV show is favorited")
        void checkFavorite_WhenFavorited_ShouldReturnTrue() throws Exception {
            // Arrange
            when(favoriteService.isFavorite(1L, 10L)).thenReturn(true);

            // Act & Assert
            mockMvc.perform(get("/favorites/check")
                    .param("userId", "1")
                    .param("tvShowId", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.isFavorite").value(true));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return false when TV show is not favorited")
        void checkFavorite_WhenNotFavorited_ShouldReturnFalse() throws Exception {
            // Arrange
            when(favoriteService.isFavorite(1L, 999L)).thenReturn(false);

            // Act & Assert
            mockMvc.perform(get("/favorites/check")
                    .param("userId", "1")
                    .param("tvShowId", "999"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.isFavorite").value(false));
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @WithMockUser
        @DisplayName("Should handle large userId")
        void getUserFavorites_WithLargeUserId_ShouldWork() throws Exception {
            // Arrange
            when(favoriteService.getUserFavorites(anyLong())).thenReturn(Collections.emptyList());
            when(favoriteService.countUserFavorites(anyLong())).thenReturn(0L);

            // Act & Assert
            mockMvc.perform(get("/favorites/user/" + Long.MAX_VALUE))
                    .andExpect(status().isOk());
        }
    }
}
