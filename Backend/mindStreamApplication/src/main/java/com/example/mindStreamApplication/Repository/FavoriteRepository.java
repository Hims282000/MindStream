package com.example.mindStreamApplication.Repository;

import com.example.mindStreamApplication.Domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // Find all favorites by user ID
    List<Favorite> findByUserId(Long userId);

    // Find specific favorite by user ID and TV show ID
    Favorite findByUserIdAndTvShowId(Long userId, Long tvShowId);

    // Check if a TV show is favorited by a user
    boolean existsByUserIdAndTvShowId(Long userId, Long tvShowId);

    // Delete favorite by user ID and TV show ID
    void deleteByUserIdAndTvShowId(Long userId, Long tvShowId);

    // Count favorites by user ID
    long countByUserId(Long userId);
}
