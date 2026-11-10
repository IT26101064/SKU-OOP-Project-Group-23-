package com.cinereserve.movie;

import com.cinereserve.movie.model.*;
import com.cinereserve.movie.service.MovieService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class MovieManagementTest {

    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieService = new MovieService();
    }

    @Test
    void testStandardMoviePolymorphism() {
        Movie std = new StandardMovie("M1", "Test 2D", "Drama", "English", 120, "PG-13", "", "Desc", "2026-01-01", "NOW_SHOWING");
        assertEquals("STANDARD 2D", std.getFormatBadge());
        assertEquals(0.00, std.getFormatSurcharge());
        assertFalse(std.includesComplimentary3DGlasses());
    }

    @Test
    void testThreeDMoviePolymorphism() {
        Movie threeD = new ThreeDMovie("M2", "Test 3D", "Action", "English", 130, "PG-13", "", "Desc", "2026-01-01", "NOW_SHOWING");
        assertEquals("RealD 3D", threeD.getFormatBadge());
        assertEquals(3.50, threeD.getFormatSurcharge());
        assertTrue(threeD.includesComplimentary3DGlasses());
    }

    @Test
    void testImaxMoviePolymorphism() {
        Movie imax = new ImaxMovie("M3", "Test IMAX", "Sci-Fi", "English", 160, "PG-13", "", "Desc", "2026-01-01", "NOW_SHOWING");
        assertEquals("IMAX LASER", imax.getFormatBadge());
        assertEquals(5.00, imax.getFormatSurcharge());
    }
}