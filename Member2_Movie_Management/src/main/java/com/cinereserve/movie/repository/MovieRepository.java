package com.cinereserve.movie.repository;

import com.cinereserve.movie.model.*;
import com.cinereserve.movie.util.StringUtil;

import java.util.*;

public class MovieRepository implements Repository<Movie, String> {

    private static final String FILE_NAME = "movies.txt";
    private final FileStorageService storage = FileStorageService.getInstance();

    public MovieRepository() {
        storage.ensureFileExists(FILE_NAME);
        checkAndSeedDefaults();
    }

    private void checkAndSeedDefaults() {
        if (storage.readAllLines(FILE_NAME).isEmpty()) {
            save(new ThreeDMovie("MOV-001", "Resident Evil (2026)", "Action / Horror / Sci-Fi", "English", 115, "R", "/assets/images/posters/resident_evil.png", "A chilling nightmare unfolds in deep woods.", "2026-09-18", "NOW_SHOWING"));
            save(new ThreeDMovie("MOV-002", "The Odyssey", "Adventure / Drama / Epic", "English", 165, "PG-13", "/assets/images/posters/the_odyssey.jpg", "A mythic voyage of courage and sacrifice.", "2026-07-15", "NOW_SHOWING"));
            save(new StandardMovie("MOV-003", "The Uprising", "Drama / Action / History", "English", 138, "R", "/assets/images/posters/the_uprising.jpg", "A commoner leads a daring revolt for freedom.", "2026-09-11", "NOW_SHOWING"));
            save(new ThreeDMovie("MOV-004", "Spider-Man: Brand New Day", "Action / Adventure / Sci-Fi", "English", 140, "PG-13", "/assets/images/posters/spiderman.jpg", "Peter Parker starts a new chapter in New York.", "2026-07-31", "NOW_SHOWING"));
            save(new StandardMovie("MOV-005", "Michael", "Biography / Drama / Music", "English", 155, "PG-13", "/assets/images/posters/Michael.jpg", "The definitive biographical portrait of the King of Pop.", "2026-04-24", "NOW_SHOWING"));
            save(new ImaxMovie("MOV-006", "Dune: Part Three", "Sci-Fi / Adventure / Epic", "English", 170, "PG-13", "/assets/images/posters/dune_part_three.jpg", "The messianic holy war spreads across the galaxy.", "2026-11-20", "COMING_SOON"));
        }
    }

    @Override
    public Optional<Movie> findById(String movieId) {
        if (movieId == null) return Optional.empty();
        return findAll().stream().filter(m -> movieId.equalsIgnoreCase(m.getMovieId())).findFirst();
    }

    @Override
    public List<Movie> findAll() {
        List<String> lines = storage.readAllLines(FILE_NAME);
        List<Movie> movies = new ArrayList<>();
        for (String line : lines) {
            Movie m = parseLine(line);
            if (m != null) movies.add(m);
        }
        return movies;
    }

    @Override
    public boolean save(Movie movie) {
        if (movie == null || existsById(movie.getMovieId())) return false;
        return storage.appendLine(FILE_NAME, formatLine(movie));
    }

    @Override
    public boolean update(Movie movie) {
        if (movie == null) return false;
        return storage.updateLine(FILE_NAME, movie.getMovieId(), formatLine(movie));
    }

    @Override
    public boolean deleteById(String movieId) {
        if (movieId == null) return false;
        return storage.deleteLine(FILE_NAME, movieId);
    }

    @Override
    public boolean existsById(String movieId) {
        return findById(movieId).isPresent();
    }

    private Movie parseLine(String line) {
        try {
            String[] p = StringUtil.splitRecord(line);
            if (p.length < 11) return null;

            String movieId = p[0];
            String title = p[1];
            String genre = p[2];
            String language = p[3];
            int duration = Integer.parseInt(p[4]);
            String ageRating = p[5];
            String formatType = p[6];
            String posterUrl = p[7];
            String description = p[8];
            String releaseDate = p[9];
            String status = p[10];

            if ("3D".equalsIgnoreCase(formatType)) {
                return new ThreeDMovie(movieId, title, genre, language, duration, ageRating, posterUrl, description, releaseDate, status);
            } else if ("IMAX".equalsIgnoreCase(formatType)) {
                return new ImaxMovie(movieId, title, genre, language, duration, ageRating, posterUrl, description, releaseDate, status);
            } else {
                return new StandardMovie(movieId, title, genre, language, duration, ageRating, posterUrl, description, releaseDate, status);
            }
        } catch (Exception e) {
            return null;
        }
    }

    private String formatLine(Movie m) {
        return StringUtil.joinRecord(
                m.getMovieId(),
                m.getTitle(),
                m.getGenre(),
                m.getLanguage(),
                String.valueOf(m.getDurationMinutes()),
                m.getAgeRating(),
                m.getFormatType(),
                m.getPosterUrl(),
                m.getDescription(),
                m.getReleaseDate(),
                m.getStatus()
        );
    }
}