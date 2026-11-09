package com.cinereserve.showtime.service;

import com.cinereserve.showtime.model.*;
import com.cinereserve.showtime.repository.ShowtimeRepository;
import com.cinereserve.showtime.util.ValidationUtil;

import java.util.*;
import java.util.stream.Collectors;

public class ShowtimeService {

    public static final int CLEANING_BUFFER_MINUTES = 15;
    private final ShowtimeRepository repository;

    public ShowtimeService() {
        this.repository = new ShowtimeRepository();
    }

    public ShowtimeService(ShowtimeRepository repository) {
        this.repository = repository;
    }

    /**
     * Schedules a new screening event with strict conflict detection.
     */
    public Showtime scheduleShowtime(String movieId, String hallId, String showDate,
                                     String startTime, String endTime, double basePrice) {
        if (!ValidationUtil.isValidDate(showDate)) {
            throw new IllegalArgumentException("Invalid date format (must be YYYY-MM-DD)");
        }
        if (!ValidationUtil.isValidTime(startTime) || !ValidationUtil.isValidTime(endTime)) {
            throw new IllegalArgumentException("Invalid time format (must be HH:mm)");
        }
        int startMin = timeToMinutes(startTime);
        int endMin = timeToMinutes(endTime);
        if (startMin >= endMin) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        // Conflict Detection: verify hall is not occupied during [startTime, endTime + 15m]
        int newStart = startMin;
        int newEnd = endMin + CLEANING_BUFFER_MINUTES;

        List<Showtime> existingShows = repository.findAll().stream()
                .filter(s -> s.isActive() && s.getHallId().equalsIgnoreCase(hallId) && s.getShowDate().equals(showDate))
                .collect(Collectors.toList());

        for (Showtime s : existingShows) {
            int exStart = timeToMinutes(s.getStartTime());
            int exEnd = timeToMinutes(s.getEndTime()) + CLEANING_BUFFER_MINUTES;

            // Overlap interval check
            if (Math.max(newStart, exStart) < Math.min(newEnd, exEnd)) {
                throw new IllegalStateException(String.format(
                        "Schedule conflict! Hall %s is occupied by %s (%s - %s + %d min buffer)",
                        hallId, s.getShowtimeId(), s.getStartTime(), s.getEndTime(), CLEANING_BUFFER_MINUTES));
            }
        }

        String showtimeId = "SHT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Showtime showtime;
        if (startMin < 16 * 60) {
            showtime = new MatineeShowtime(showtimeId, movieId, hallId, showDate, startTime, endTime, basePrice, Showtime.STATUS_ACTIVE);
        } else if (startMin >= 19 * 60 + 30) {
            showtime = new PrimeShowtime(showtimeId, movieId, hallId, showDate, startTime, endTime, basePrice, Showtime.STATUS_ACTIVE);
        } else {
            showtime = new StandardShowtime(showtimeId, movieId, hallId, showDate, startTime, endTime, basePrice, Showtime.STATUS_ACTIVE);
        }

        repository.save(showtime);
        return showtime;
    }

    public List<Showtime> getAllShowtimes() {
        return repository.findAll();
    }

    public List<Showtime> getActiveShowtimes() {
        return repository.findAll().stream().filter(Showtime::isActive).collect(Collectors.toList());
    }

    public List<Showtime> getShowtimesByMovie(String movieId) {
        return repository.findAll().stream()
                .filter(s -> s.isActive() && s.getMovieId().equalsIgnoreCase(movieId))
                .collect(Collectors.toList());
    }

    public List<Showtime> getShowtimesByHall(String hallId) {
        return repository.findAll().stream()
                .filter(s -> s.isActive() && s.getHallId().equalsIgnoreCase(hallId))
                .collect(Collectors.toList());
    }

    public boolean cancelShowtime(String showtimeId) {
        Optional<Showtime> opt = repository.findById(showtimeId);
        if (!opt.isPresent()) return false;
        Showtime s = opt.get();
        s.setStatus(Showtime.STATUS_CANCELLED);
        return repository.update(s);
    }

    private static int timeToMinutes(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }
}