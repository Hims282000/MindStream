package com.example.mindStreamApplication.Domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("TvShow Entity Tests")
class TvShowTest {

    private TvShow tvShow;

    @BeforeEach
    void setUp() {
        tvShow = new TvShow();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Default constructor should create empty TvShow")
        void defaultConstructor_ShouldCreateEmptyTvShow() {
            TvShow emptyShow = new TvShow();
            
            assertNotNull(emptyShow);
            assertNull(emptyShow.getId());
            assertNull(emptyShow.getAlbum());
            assertNull(emptyShow.getYear());
            assertNull(emptyShow.getChartPosition());
        }

        @Test
        @DisplayName("Parameterized constructor should set all fields")
        void parameterizedConstructor_ShouldSetAllFields() {
            TvShow newShow = new TvShow("Breaking Bad", 2008, "1");
            
            assertEquals("Breaking Bad", newShow.getAlbum());
            assertEquals(2008, newShow.getYear());
            assertEquals("1", newShow.getChartPosition());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get Id correctly")
        void shouldSetAndGetId() {
            tvShow.setId(1L);
            assertEquals(1L, tvShow.getId());
        }

        @Test
        @DisplayName("Should set and get Album correctly")
        void shouldSetAndGetAlbum() {
            tvShow.setAlbum("Game of Thrones");
            assertEquals("Game of Thrones", tvShow.getAlbum());
        }

        @Test
        @DisplayName("Should set and get Year correctly")
        void shouldSetAndGetYear() {
            tvShow.setYear(2011);
            assertEquals(2011, tvShow.getYear());
        }

        @Test
        @DisplayName("Should set and get ChartPosition correctly")
        void shouldSetAndGetChartPosition() {
            tvShow.setChartPosition("2");
            assertEquals("2", tvShow.getChartPosition());
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("ToString should contain all TvShow fields")
        void toString_ShouldContainAllFields() {
            tvShow.setId(1L);
            tvShow.setAlbum("The Wire");
            tvShow.setYear(2002);
            tvShow.setChartPosition("3");

            String result = tvShow.toString();

            assertTrue(result.contains("id=1"));
            assertTrue(result.contains("album='The Wire'"));
            assertTrue(result.contains("year=2002"));
            assertTrue(result.contains("chartPosition='3'"));
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null Year")
        void shouldHandleNullYear() {
            tvShow.setYear(null);
            assertNull(tvShow.getYear());
        }

        @Test
        @DisplayName("Should handle null ChartPosition")
        void shouldHandleNullChartPosition() {
            tvShow.setChartPosition(null);
            assertNull(tvShow.getChartPosition());
        }

        @Test
        @DisplayName("Should handle very old year")
        void shouldHandleVeryOldYear() {
            tvShow.setYear(1950);
            assertEquals(1950, tvShow.getYear());
        }

        @Test
        @DisplayName("Should handle future year")
        void shouldHandleFutureYear() {
            tvShow.setYear(2030);
            assertEquals(2030, tvShow.getYear());
        }

        @Test
        @DisplayName("Should handle special characters in album name")
        void shouldHandleSpecialCharactersInAlbum() {
            tvShow.setAlbum("Show: Season 1 - Episode #1!");
            assertEquals("Show: Season 1 - Episode #1!", tvShow.getAlbum());
        }
    }
}
