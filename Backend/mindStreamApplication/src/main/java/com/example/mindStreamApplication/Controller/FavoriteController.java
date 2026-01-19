package com.example.mindStreamApplication.Controller;

import com.example.mindStreamApplication.Service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    // Get user's favorites
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserFavorites(@PathVariable Long userId) {
        List<Map<String, Object>> favorites = favoriteService.getUserFavorites(userId);
        long count = favoriteService.countUserFavorites(userId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User favorites retrieved successfully",
                "data", favorites,
                "count", count
        ));
    }

    // Add TV show to favorites
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToFavorites(
            @RequestBody Map<String, Long> request) {

        Long userId = request.get("userId");
        Long tvShowId = request.get("tvShowId");

        if (userId == null || tvShowId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "userId and tvShowId are required"
            ));
        }

        Map<String, Object> result = favoriteService.addToFavorites(userId, tvShowId);
        boolean success = (boolean) result.get("success");

        if (success) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // Remove TV show from favorites
    @DeleteMapping("/remove")
    public ResponseEntity<Map<String, Object>> removeFromFavorites(
            @RequestBody Map<String, Long> request) {

        Long userId = request.get("userId");
        Long tvShowId = request.get("tvShowId");

        if (userId == null || tvShowId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "userId and tvShowId are required"
            ));
        }

        Map<String, Object> result = favoriteService.removeFromFavorites(userId, tvShowId);
        boolean success = (boolean) result.get("success");

        if (success) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // Check if TV show is favorited by user
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkFavorite(
            @RequestParam Long userId,
            @RequestParam Long tvShowId) {

        boolean isFavorite = favoriteService.isFavorite(userId, tvShowId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "isFavorite", isFavorite,
                "message", isFavorite ? "TV show is in favorites" : "TV show is not in favorites"
        ));
    }
}
