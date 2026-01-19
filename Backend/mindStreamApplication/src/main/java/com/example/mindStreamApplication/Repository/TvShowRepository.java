package com.example.mindStreamApplication.Repository;

import com.example.mindStreamApplication.Domain.TvShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TvShowRepository extends JpaRepository<TvShow, Long> {

    // Find TV show by album name
    TvShow findByAlbum(String album);

    // Find TV shows by year
    List<TvShow> findByYear(Integer year);

    // Find TV shows containing album name
    List<TvShow> findByAlbumContaining(String albumName);

    // Find TV shows by year range
    List<TvShow> findByYearBetween(Integer startYear, Integer endYear);

    // Check if album exists
    boolean existsByAlbum(String album);
}