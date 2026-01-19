package com.example.mindStreamApplication.Repository;

import com.example.mindStreamApplication.Domain.Favorite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
@DisplayName("FavoriteRepository Tests")
class FavoriteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FavoriteRepository favoriteRepository;

    private Favorite favorite1;
    private Favorite favorite2;
    private Favorite favorite3;

    @BeforeEach
    void setUp() {
        favorite1 = new Favorite(1L, 10L);
        favorite1.setAddedAt(LocalDateTime.now().minusDays(2));
        
        favorite2 = new Favorite(1L, 20L);
        favorite2.setAddedAt(LocalDateTime.now().minusDays(1));
        
        favorite3 = new Favorite(2L, 10L);
        favorite3.setAddedAt(LocalDateTime.now());
        
        entityManager.persist(favorite1);
        entityManager.persist(favorite2);
        entityManager.persist(favorite3);
        entityManager.flush();
    }

    @Nested
    @DisplayName("FindByUserId Tests")
    class FindByUserIdTests {

        @Test
        @DisplayName("Should find all favorites for user")
        void findByUserId_ShouldReturnUserFavorites() {
            List<Favorite> favorites = favoriteRepository.findByUserId(1L);
            
            assertEquals(2, favorites.size());
        }

        @Test
        @DisplayName("Should return empty list for user with no favorites")
        void findByUserId_WhenNoFavorites_ShouldReturnEmptyList() {
            List<Favorite> favorites = favoriteRepository.findByUserId(999L);
            
            assertTrue(favorites.isEmpty());
        }

        @Test
        @DisplayName("Should return single favorite for user with one favorite")
        void findByUserId_WhenOneFavorite_ShouldReturnSingleItem() {
            List<Favorite> favorites = favoriteRepository.findByUserId(2L);
            
            assertEquals(1, favorites.size());
            assertEquals(10L, favorites.get(0).getTvShowId());
        }
    }

    @Nested
    @DisplayName("FindByUserIdAndTvShowId Tests")
    class FindByUserIdAndTvShowIdTests {

        @Test
        @DisplayName("Should find specific favorite")
        void findByUserIdAndTvShowId_ShouldReturnFavorite() {
            Favorite found = favoriteRepository.findByUserIdAndTvShowId(1L, 10L);
            
            assertNotNull(found);
            assertEquals(1L, found.getUserId());
            assertEquals(10L, found.getTvShowId());
        }

        @Test
        @DisplayName("Should return null when favorite not exists")
        void findByUserIdAndTvShowId_WhenNotExists_ShouldReturnNull() {
            Favorite found = favoriteRepository.findByUserIdAndTvShowId(1L, 999L);
            
            assertNull(found);
        }

        @Test
        @DisplayName("Should return null for wrong user")
        void findByUserIdAndTvShowId_WrongUser_ShouldReturnNull() {
            Favorite found = favoriteRepository.findByUserIdAndTvShowId(999L, 10L);
            
            assertNull(found);
        }
    }

    @Nested
    @DisplayName("ExistsByUserIdAndTvShowId Tests")
    class ExistsByUserIdAndTvShowIdTests {

        @Test
        @DisplayName("Should return true when favorite exists")
        void existsByUserIdAndTvShowId_WhenExists_ShouldReturnTrue() {
            boolean exists = favoriteRepository.existsByUserIdAndTvShowId(1L, 10L);
            
            assertTrue(exists);
        }

        @Test
        @DisplayName("Should return false when favorite not exists")
        void existsByUserIdAndTvShowId_WhenNotExists_ShouldReturnFalse() {
            boolean exists = favoriteRepository.existsByUserIdAndTvShowId(1L, 999L);
            
            assertFalse(exists);
        }

        @Test
        @DisplayName("Should return false for wrong user")
        void existsByUserIdAndTvShowId_WrongUser_ShouldReturnFalse() {
            boolean exists = favoriteRepository.existsByUserIdAndTvShowId(999L, 10L);
            
            assertFalse(exists);
        }
    }

    @Nested
    @DisplayName("CountByUserId Tests")
    class CountByUserIdTests {

        @Test
        @DisplayName("Should count user favorites correctly")
        void countByUserId_ShouldReturnCorrectCount() {
            long count = favoriteRepository.countByUserId(1L);
            
            assertEquals(2, count);
        }

        @Test
        @DisplayName("Should return zero for user with no favorites")
        void countByUserId_WhenNoFavorites_ShouldReturnZero() {
            long count = favoriteRepository.countByUserId(999L);
            
            assertEquals(0, count);
        }

        @Test
        @DisplayName("Should return one for user with single favorite")
        void countByUserId_WhenOneFavorite_ShouldReturnOne() {
            long count = favoriteRepository.countByUserId(2L);
            
            assertEquals(1, count);
        }
    }

    @Nested
    @DisplayName("DeleteByUserIdAndTvShowId Tests")
    class DeleteByUserIdAndTvShowIdTests {

        @Test
        @DisplayName("Should delete specific favorite")
        void deleteByUserIdAndTvShowId_ShouldRemoveFavorite() {
            favoriteRepository.deleteByUserIdAndTvShowId(1L, 10L);
            entityManager.flush();
            
            boolean exists = favoriteRepository.existsByUserIdAndTvShowId(1L, 10L);
            assertFalse(exists);
        }

        @Test
        @DisplayName("Should not affect other favorites when deleting")
        void deleteByUserIdAndTvShowId_ShouldNotAffectOthers() {
            long countBefore = favoriteRepository.countByUserId(1L);
            
            favoriteRepository.deleteByUserIdAndTvShowId(1L, 10L);
            entityManager.flush();
            
            long countAfter = favoriteRepository.countByUserId(1L);
            assertEquals(countBefore - 1, countAfter);
        }
    }

    @Nested
    @DisplayName("CRUD Operations Tests")
    class CrudOperationsTests {

        @Test
        @DisplayName("Should save new favorite")
        void save_ShouldPersistFavorite() {
            Favorite newFavorite = new Favorite(3L, 30L);
            
            Favorite saved = favoriteRepository.save(newFavorite);
            
            assertNotNull(saved.getId());
            assertEquals(3L, saved.getUserId());
            assertEquals(30L, saved.getTvShowId());
        }

        @Test
        @DisplayName("Should delete favorite by entity")
        void delete_ShouldRemoveFavorite() {
            Long favoriteId = favorite1.getId();
            
            favoriteRepository.delete(favorite1);
            entityManager.flush();
            
            assertFalse(favoriteRepository.findById(favoriteId).isPresent());
        }

        @Test
        @DisplayName("Should find all favorites")
        void findAll_ShouldReturnAllFavorites() {
            List<Favorite> allFavorites = favoriteRepository.findAll();
            
            assertEquals(3, allFavorites.size());
        }

        @Test
        @DisplayName("Should update favorite")
        void update_ShouldModifyFavorite() {
            LocalDateTime newTime = LocalDateTime.now().plusDays(1);
            favorite1.setAddedAt(newTime);
            
            favoriteRepository.save(favorite1);
            entityManager.flush();
            entityManager.clear();
            
            Favorite updated = favoriteRepository.findByUserIdAndTvShowId(1L, 10L);
            assertEquals(newTime, updated.getAddedAt());
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null userId in search")
        void findByUserId_WithNull_ShouldReturnEmptyList() {
            List<Favorite> favorites = favoriteRepository.findByUserId(null);
            
            assertTrue(favorites.isEmpty());
        }

        @Test
        @DisplayName("Should handle zero counts correctly")
        void countByUserId_WithNonExistentUser_ShouldReturnZero() {
            long count = favoriteRepository.countByUserId(Long.MAX_VALUE);
            
            assertEquals(0, count);
        }

        @Test
        @DisplayName("Should allow same TV show for different users")
        void sameTvShowDifferentUsers_ShouldWork() {
            // User 1 and User 2 both have tvShowId 10
            boolean user1Has = favoriteRepository.existsByUserIdAndTvShowId(1L, 10L);
            boolean user2Has = favoriteRepository.existsByUserIdAndTvShowId(2L, 10L);
            
            assertTrue(user1Has);
            assertTrue(user2Has);
        }

        @Test
        @DisplayName("Should handle favorites with past dates")
        void shouldHandleFavoritesWithPastDates() {
            Favorite oldFavorite = new Favorite(5L, 50L);
            oldFavorite.setAddedAt(LocalDateTime.of(2020, 1, 1, 0, 0));
            
            Favorite saved = favoriteRepository.save(oldFavorite);
            entityManager.flush();
            
            assertNotNull(saved.getId());
            assertEquals(LocalDateTime.of(2020, 1, 1, 0, 0), saved.getAddedAt());
        }
    }
}
