package com.example.mindStreamApplication.Service;

import com.example.mindStreamApplication.Domain.TvShow;
import com.example.mindStreamApplication.Exception.DuplicateResourceException;
import com.example.mindStreamApplication.Exception.ResourceNotFoundException;
import com.example.mindStreamApplication.Repository.TvShowRepository;
import com.example.mindStreamApplication.Repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TvShowService {

    @Autowired
    private TvShowRepository tvShowRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    @Lazy
    private FavoriteService favoriteService;

    // Get all TV shows
    public List<Map<String, Object>> getAllTvShows(Long userId) {
        List<TvShow> tvShows = tvShowRepository.findAll();
        return tvShows.stream()
                .map(show -> convertToMap(show, userId))
                .collect(Collectors.toList());
    }

    // Get TV show by ID
    public Map<String, Object> getTvShowById(Long id, Long userId) {
        TvShow show = tvShowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TV show", "id", id));
        return convertToMap(show, userId);
    }

    // Search TV shows by album name
    public List<Map<String, Object>> searchTvShows(String query, Long userId) {
        List<TvShow> tvShows = tvShowRepository.findByAlbumContaining(query);
        return tvShows.stream()
                .map(show -> convertToMap(show, userId))
                .collect(Collectors.toList());
    }

    // Get TV shows by year
    public List<Map<String, Object>> getTvShowsByYear(Integer year, Long userId) {
        List<TvShow> tvShows = tvShowRepository.findByYear(year);
        return tvShows.stream()
                .map(show -> convertToMap(show, userId))
                .collect(Collectors.toList());
    }

    // Add a new TV show (manual method to add data)
    public Map<String, Object> addTvShow(String album, Integer year, String chartPosition) {
        Map<String, Object> response = new HashMap<>();

        // Check if album already exists
        if (tvShowRepository.existsByAlbum(album)) {
            throw new DuplicateResourceException("TV show", "album", album);
        }

        // Create and save TV show
        TvShow tvShow = new TvShow(album, year, chartPosition);
        TvShow savedShow = tvShowRepository.save(tvShow);

        response.put("success", true);
        response.put("message", "TV show added successfully");
        response.put("tvShow", Map.of(
                "id", savedShow.getId(),
                "album", savedShow.getAlbum(),
                "year", savedShow.getYear() != null ? savedShow.getYear() : 0,
                "chartPosition", savedShow.getChartPosition() != null ? savedShow.getChartPosition() : ""
        ));

        return response;
    }

    // Load initial data for TV shows
    public void loadInitialData() {
        // Check if data already exists
        if (tvShowRepository.count() > 0) {
            return; // Data already loaded
        }

        // Sample TV shows data
        List<TvShow> sampleShows = Arrays.asList(
                new TvShow("Breaking Bad", 2008, "1"),
                new TvShow("Game of Thrones", 2011, "2"),
                new TvShow("The Wire", 2002, "3"),
                new TvShow("The Sopranos", 1999, "4"),
                new TvShow("Friends", 1994, "5"),
                new TvShow("Stranger Things", 2016, "6"),
                new TvShow("The Office", 2005, "7"),
                new TvShow("Westworld", 2016, "8"),
                new TvShow("Black Mirror", 2011, "9"),
                new TvShow("The Mandalorian", 2019, "10")
        );

        tvShowRepository.saveAll(sampleShows);
    }

    // Helper method to convert TvShow to Map with favorite status
    private Map<String, Object> convertToMap(TvShow show, Long userId) {
        Map<String, Object> showMap = new HashMap<>();
        showMap.put("id", show.getId());
        showMap.put("album", show.getAlbum());
        showMap.put("year", show.getYear());
        showMap.put("chartPosition", show.getChartPosition());

        // Check if favorited by user
        if (userId != null) {
            boolean isFavorite = favoriteRepository.existsByUserIdAndTvShowId(userId, show.getId());
            showMap.put("isFavorite", isFavorite);
        } else {
            showMap.put("isFavorite", false);
        }

        return showMap;
    }
}