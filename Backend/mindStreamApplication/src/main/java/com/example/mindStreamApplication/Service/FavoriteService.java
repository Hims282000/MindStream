package com.example.mindStreamApplication.Service;

import com.example.mindStreamApplication.Domain.Favorite;
import com.example.mindStreamApplication.Domain.TvShow;
import com.example.mindStreamApplication.Exception.DuplicateResourceException;
import com.example.mindStreamApplication.Exception.ResourceNotFoundException;
import com.example.mindStreamApplication.Repository.FavoriteRepository;
import com.example.mindStreamApplication.Repository.TvShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private TvShowRepository tvShowRepository;

    // Add TV show to favorites
    public Map<String, Object> addToFavorites(Long userId, Long tvShowId) {
        Map<String, Object> response = new HashMap<>();

        // Check if TV show exists
        TvShow tvShow = tvShowRepository.findById(tvShowId)
                .orElseThrow(() -> new ResourceNotFoundException("TV show", "id", tvShowId));

        // Check if already favorited
        if (favoriteRepository.existsByUserIdAndTvShowId(userId, tvShowId)) {
            throw new DuplicateResourceException("Favorite already exists");
        }

        // Create and save favorite
        Favorite favorite = new Favorite(userId, tvShowId);
        favorite.setAddedAt(LocalDateTime.now());
        favoriteRepository.save(favorite);

        response.put("success", true);
        response.put("message", "Added to favorites");
        response.put("favoriteId", favorite.getId());

        return response;
    }

    // Remove TV show from favorites
    public Map<String, Object> removeFromFavorites(Long userId, Long tvShowId) {
        Map<String, Object> response = new HashMap<>();

        // Find the favorite
        Favorite favorite = favoriteRepository.findByUserIdAndTvShowId(userId, tvShowId);

        if (favorite == null) {
            throw new ResourceNotFoundException("Favorite not found for user: " + userId + " and TV show: " + tvShowId);
        }

        // Delete favorite
        favoriteRepository.delete(favorite);

        response.put("success", true);
        response.put("message", "Removed from favorites");

        return response;
    }

    // Get user's favorites
    public List<Map<String, Object>> getUserFavorites(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Favorite favorite : favorites) {
            // Get TV show details
            TvShow tvShow = tvShowRepository.findById(favorite.getTvShowId())
                    .orElseThrow(() -> new ResourceNotFoundException("TV show", "id", favorite.getTvShowId()));

            Map<String, Object> favoriteMap = new HashMap<>();
            favoriteMap.put("id", favorite.getId());
            
            Map<String, Object> tvShowMap = new HashMap<>();
            tvShowMap.put("id", tvShow.getId());
            tvShowMap.put("album", tvShow.getAlbum() != null ? tvShow.getAlbum() : "");
            tvShowMap.put("year", tvShow.getYear() != null ? tvShow.getYear() : 0);
            tvShowMap.put("chartPosition", tvShow.getChartPosition() != null ? tvShow.getChartPosition() : "-");
            favoriteMap.put("tvShow", tvShowMap);
            
            favoriteMap.put("addedAt", favorite.getAddedAt());
            favoriteMap.put("isFavorite", true);

            result.add(favoriteMap);
        }

        return result;
    }

    // Check if TV show is favorited by user
    public boolean isFavorite(Long userId, Long tvShowId) {
        return favoriteRepository.existsByUserIdAndTvShowId(userId, tvShowId);
    }

    // Count user's favorites
    public long countUserFavorites(Long userId) {
        return favoriteRepository.countByUserId(userId);
    }
}