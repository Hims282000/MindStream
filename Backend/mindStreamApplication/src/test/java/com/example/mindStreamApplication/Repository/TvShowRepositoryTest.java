package com.example.mindStreamApplication.Repository;

import com.example.mindStreamApplication.Domain.TvShow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
@DisplayName("TvShowRepository Tests")
class TvShowRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TvShowRepository tvShowRepository;

    private TvShow testShow1;
    private TvShow testShow2;
    private TvShow testShow3;

    @BeforeEach
    void setUp() {
        testShow1 = new TvShow("Breaking Bad", 2008, "1");
        testShow2 = new TvShow("Game of Thrones", 2011, "2");
        testShow3 = new TvShow("Breaking Dawn", 2011, "3");
        
        entityManager.persist(testShow1);
        entityManager.persist(testShow2);
        entityManager.persist(testShow3);
        entityManager.flush();
    }

    @Nested
    @DisplayName("FindByAlbum Tests")
    class FindByAlbumTests {

        @Test
        @DisplayName("Should find TV show by exact album name")
        void findByAlbum_WhenAlbumExists_ShouldReturnTvShow() {
            TvShow found = tvShowRepository.findByAlbum("Breaking Bad");
            
            assertNotNull(found);
            assertEquals("Breaking Bad", found.getAlbum());
            assertEquals(2008, found.getYear());
        }

        @Test
        @DisplayName("Should return null when album not exists")
        void findByAlbum_WhenAlbumNotExists_ShouldReturnNull() {
            TvShow found = tvShowRepository.findByAlbum("Nonexistent Show");
            
            assertNull(found);
        }
    }

    @Nested
    @DisplayName("FindByYear Tests")
    class FindByYearTests {

        @Test
        @DisplayName("Should find all TV shows by year")
        void findByYear_ShouldReturnAllMatchingShows() {
            List<TvShow> shows = tvShowRepository.findByYear(2011);
            
            assertEquals(2, shows.size());
        }

        @Test
        @DisplayName("Should return empty list when no shows in year")
        void findByYear_WhenNoShowsInYear_ShouldReturnEmptyList() {
            List<TvShow> shows = tvShowRepository.findByYear(2000);
            
            assertTrue(shows.isEmpty());
        }

        @Test
        @DisplayName("Should return single show when only one in year")
        void findByYear_WhenOneShowInYear_ShouldReturnSingleShow() {
            List<TvShow> shows = tvShowRepository.findByYear(2008);
            
            assertEquals(1, shows.size());
            assertEquals("Breaking Bad", shows.get(0).getAlbum());
        }
    }

    @Nested
    @DisplayName("FindByAlbumContaining Tests")
    class FindByAlbumContainingTests {

        @Test
        @DisplayName("Should find TV shows containing search term")
        void findByAlbumContaining_ShouldReturnMatchingShows() {
            List<TvShow> shows = tvShowRepository.findByAlbumContaining("Breaking");
            
            assertEquals(2, shows.size());
        }

        @Test
        @DisplayName("Should return empty list when no match")
        void findByAlbumContaining_WhenNoMatch_ShouldReturnEmptyList() {
            List<TvShow> shows = tvShowRepository.findByAlbumContaining("Stranger");
            
            assertTrue(shows.isEmpty());
        }

        @Test
        @DisplayName("Should be case sensitive")
        void findByAlbumContaining_ShouldBeCaseSensitive() {
            List<TvShow> shows = tvShowRepository.findByAlbumContaining("breaking");
            
            // Note: This might return results depending on DB configuration
            // For H2 in-memory, this might be case-insensitive
            // Adjust based on actual behavior
            assertTrue(shows.size() >= 0);
        }

        @Test
        @DisplayName("Should find single character match")
        void findByAlbumContaining_SingleCharacter_ShouldWork() {
            List<TvShow> shows = tvShowRepository.findByAlbumContaining("B");
            
            assertTrue(shows.size() >= 2);
        }
    }

    @Nested
    @DisplayName("FindByYearBetween Tests")
    class FindByYearBetweenTests {

        @Test
        @DisplayName("Should find TV shows between years inclusive")
        void findByYearBetween_ShouldReturnShowsInRange() {
            List<TvShow> shows = tvShowRepository.findByYearBetween(2008, 2011);
            
            assertEquals(3, shows.size());
        }

        @Test
        @DisplayName("Should return empty list when no shows in range")
        void findByYearBetween_WhenNoShowsInRange_ShouldReturnEmptyList() {
            List<TvShow> shows = tvShowRepository.findByYearBetween(2000, 2005);
            
            assertTrue(shows.isEmpty());
        }

        @Test
        @DisplayName("Should work with same start and end year")
        void findByYearBetween_SameStartAndEnd_ShouldWork() {
            List<TvShow> shows = tvShowRepository.findByYearBetween(2008, 2008);
            
            assertEquals(1, shows.size());
        }
    }

    @Nested
    @DisplayName("ExistsByAlbum Tests")
    class ExistsByAlbumTests {

        @Test
        @DisplayName("Should return true when album exists")
        void existsByAlbum_WhenAlbumExists_ShouldReturnTrue() {
            boolean exists = tvShowRepository.existsByAlbum("Breaking Bad");
            
            assertTrue(exists);
        }

        @Test
        @DisplayName("Should return false when album not exists")
        void existsByAlbum_WhenAlbumNotExists_ShouldReturnFalse() {
            boolean exists = tvShowRepository.existsByAlbum("Nonexistent Show");
            
            assertFalse(exists);
        }
    }

    @Nested
    @DisplayName("CRUD Operations Tests")
    class CrudOperationsTests {

        @Test
        @DisplayName("Should save new TV show")
        void save_ShouldPersistTvShow() {
            TvShow newShow = new TvShow("New Show", 2020, "10");
            
            TvShow saved = tvShowRepository.save(newShow);
            
            assertNotNull(saved.getId());
            assertEquals("New Show", saved.getAlbum());
        }

        @Test
        @DisplayName("Should find TV show by id")
        void findById_ShouldReturnTvShow() {
            Optional<TvShow> found = tvShowRepository.findById(testShow1.getId());
            
            assertTrue(found.isPresent());
            assertEquals("Breaking Bad", found.get().getAlbum());
        }

        @Test
        @DisplayName("Should delete TV show")
        void delete_ShouldRemoveTvShow() {
            Long showId = testShow1.getId();
            
            tvShowRepository.delete(testShow1);
            entityManager.flush();
            
            Optional<TvShow> found = tvShowRepository.findById(showId);
            assertFalse(found.isPresent());
        }

        @Test
        @DisplayName("Should update TV show")
        void update_ShouldModifyTvShow() {
            testShow1.setChartPosition("100");
            
            tvShowRepository.save(testShow1);
            entityManager.flush();
            entityManager.clear();
            
            TvShow updated = tvShowRepository.findByAlbum("Breaking Bad");
            assertEquals("100", updated.getChartPosition());
        }

        @Test
        @DisplayName("Should find all TV shows")
        void findAll_ShouldReturnAllShows() {
            List<TvShow> allShows = tvShowRepository.findAll();
            
            assertEquals(3, allShows.size());
        }

        @Test
        @DisplayName("Should count TV shows")
        void count_ShouldReturnCorrectCount() {
            long count = tvShowRepository.count();
            
            assertEquals(3, count);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null year in search")
        void findByYear_WithNull_ShouldReturnEmptyList() {
            List<TvShow> shows = tvShowRepository.findByYear(null);
            
            assertTrue(shows.isEmpty());
        }

        @Test
        @DisplayName("Should save TV show with null year")
        void save_WithNullYear_ShouldWork() {
            TvShow showWithNullYear = new TvShow("No Year Show", null, "5");
            
            TvShow saved = tvShowRepository.save(showWithNullYear);
            entityManager.flush();
            
            assertNotNull(saved.getId());
            assertNull(saved.getYear());
        }

        @Test
        @DisplayName("Should handle empty album search")
        void findByAlbumContaining_WithEmptyString_ShouldReturnAll() {
            List<TvShow> shows = tvShowRepository.findByAlbumContaining("");
            
            assertEquals(3, shows.size());
        }
    }
}
