package com.example.mindStreamApplication.Controller;

import com.example.mindStreamApplication.Service.TvShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tvshows")
public class TvShowController {

    @Autowired
    private TvShowService tvShowService;

    // Get all TV shows
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllTvShows(
            @RequestParam(required = false) Long userId) {

        List<Map<String, Object>> tvShows = tvShowService.getAllTvShows(userId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "TV shows retrieved successfully",
                "data", tvShows,
                "count", tvShows.size()
        ));
    }

    // Get TV show by ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTvShowById(
            @PathVariable Long id,
            @RequestParam(required = false) Long userId) {

        Map<String, Object> tvShow = tvShowService.getTvShowById(id, userId);

        if (tvShow == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "TV show not found with id: " + id
            ));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "TV show retrieved successfully",
                "data", tvShow
        ));
    }

    // Search TV shows by album name
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchTvShows(
            @RequestParam String query,
            @RequestParam(required = false) Long userId) {

        List<Map<String, Object>> tvShows = tvShowService.searchTvShows(query, userId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Search results for: " + query,
                "data", tvShows,
                "count", tvShows.size()
        ));
    }

    // Get TV shows by year
    @GetMapping("/year/{year}")
    public ResponseEntity<Map<String, Object>> getTvShowsByYear(
            @PathVariable Integer year,
            @RequestParam(required = false) Long userId) {

        List<Map<String, Object>> tvShows = tvShowService.getTvShowsByYear(year, userId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "TV shows from year: " + year,
                "data", tvShows,
                "count", tvShows.size()
        ));
    }

    // Add a new TV show (Admin function)
    @PostMapping
    public ResponseEntity<Map<String, Object>> addTvShow(
            @RequestBody Map<String, Object> tvShowRequest) {

        String album = (String) tvShowRequest.get("album");
        Integer year = (Integer) tvShowRequest.get("year");
        String chartPosition = (String) tvShowRequest.get("chartPosition");

        if (album == null || album.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Album name is required"
            ));
        }

        Map<String, Object> result = tvShowService.addTvShow(album, year, chartPosition);
        boolean success = (boolean) result.get("success");

        if (success) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // Load initial data (Admin function - call once)
    @PostMapping("/load-initial-data")
    public ResponseEntity<Map<String, Object>> loadInitialData() {
        tvShowService.loadInitialData();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Initial TV show data loaded successfully"
        ));
    }
}