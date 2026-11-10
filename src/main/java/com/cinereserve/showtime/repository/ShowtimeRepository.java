package com.cinereserve.showtime.repository;

import com.cinereserve.showtime.model.*;
import com.cinereserve.showtime.util.StringUtil;

import java.util.*;

public class ShowtimeRepository implements Repository<Showtime, String> {

    private static final String FILE_NAME = "showtimes.txt";
    private final FileStorageService storage = FileStorageService.getInstance();

    public ShowtimeRepository() {
        storage.ensureFileExists(FILE_NAME);
        checkAndSeedDefaults();
    }

    private void checkAndSeedDefaults() {
        if (storage.readAllLines(FILE_NAME).isEmpty()) {
            save(new MatineeShowtime("SHT-001", "MOV-001", "HAL-001", "2026-10-12", "13:30", "15:25", 16.00, Showtime.STATUS_ACTIVE));
            save(new StandardShowtime("SHT-002", "MOV-002", "HAL-001", "2026-10-12", "16:30", "19:15", 20.00, Showtime.STATUS_ACTIVE));
            save(new PrimeShowtime("SHT-003", "MOV-001", "HAL-001", "2026-10-12", "20:00", "21:55", 18.00, Showtime.STATUS_ACTIVE));
            save(new MatineeShowtime("SHT-004", "MOV-003", "HAL-002", "2026-10-12", "14:00", "16:18", 15.00, Showtime.STATUS_ACTIVE));
            save(new StandardShowtime("SHT-005", "MOV-004", "HAL-002", "2026-10-12", "17:00", "19:20", 18.00, Showtime.STATUS_ACTIVE));
            save(new PrimeShowtime("SHT-006", "MOV-005", "HAL-002", "2026-10-12", "20:00", "22:35", 16.00, Showtime.STATUS_ACTIVE));
        }
    }

    @Override
    public Optional<Showtime> findById(String id) {
        if (id == null) return Optional.empty();
        return findAll().stream().filter(s -> id.equalsIgnoreCase(s.getShowtimeId())).findFirst();
    }

    @Override
    public List<Showtime> findAll() {
        List<String> lines = storage.readAllLines(FILE_NAME);
        List<Showtime> list = new ArrayList<>();
        for (String line : lines) {
            Showtime s = parseLine(line);
            if (s != null) list.add(s);
        }
        return list;
    }

    @Override
    public boolean save(Showtime s) {
        if (s == null || existsById(s.getShowtimeId())) return false;
        return storage.appendLine(FILE_NAME, formatLine(s));
    }

    @Override
    public boolean update(Showtime s) {
        if (s == null) return false;
        List<Showtime> all = findAll();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getShowtimeId().equalsIgnoreCase(s.getShowtimeId())) {
                all.set(i, s);
                found = true;
                break;
            }
        }
        if (!found) return false;
        List<String> lines = new ArrayList<>();
        for (Showtime item : all) lines.add(formatLine(item));
        return storage.writeAllLines(FILE_NAME, lines);
    }

    @Override
    public boolean deleteById(String id) {
        List<Showtime> all = findAll();
        boolean removed = all.removeIf(s -> s.getShowtimeId().equalsIgnoreCase(id));
        if (!removed) return false;
        List<String> lines = new ArrayList<>();
        for (Showtime item : all) lines.add(formatLine(item));
        return storage.writeAllLines(FILE_NAME, lines);
    }

    @Override
    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    private Showtime parseLine(String line) {
        try {
            String[] p = StringUtil.splitRecord(line);
            if (p.length < 8) return null;
            String showtimeId = p[0];
            String movieId = p[1];
            String hallId = p[2];
            String showDate = p[3];
            String startTime = p[4];
            String endTime = p[5];
            double basePrice = Double.parseDouble(p[6]);
            String status = p[7];

            int startMinutes = parseTimeToMinutes(startTime);
            if (startMinutes < 16 * 60) {
                return new MatineeShowtime(showtimeId, movieId, hallId, showDate, startTime, endTime, basePrice, status);
            } else if (startMinutes >= 19 * 60 + 30) {
                return new PrimeShowtime(showtimeId, movieId, hallId, showDate, startTime, endTime, basePrice, status);
            } else {
                return new StandardShowtime(showtimeId, movieId, hallId, showDate, startTime, endTime, basePrice, status);
            }
        } catch (Exception e) {
            return null;
        }
    }

    private String formatLine(Showtime s) {
        return StringUtil.joinRecord(
                s.getShowtimeId(),
                s.getMovieId(),
                s.getHallId(),
                s.getShowDate(),
                s.getStartTime(),
                s.getEndTime(),
                String.format(Locale.US, "%.2f", s.getBasePrice()),
                s.getStatus()
        );
    }

    private static int parseTimeToMinutes(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }
}