package com.cinereserve.seat.repository;

import com.cinereserve.seat.model.*;
import com.cinereserve.seat.util.StringUtil;

import java.util.*;

public class CinemaHallRepository implements Repository<CinemaHall, String> {

    private static final String FILE_NAME = "screens.txt";
    private final FileStorageService storage = FileStorageService.getInstance();

    public CinemaHallRepository() {
        storage.ensureFileExists(FILE_NAME);
        checkAndSeedDefaults();
    }

    private void checkAndSeedDefaults() {
        if (storage.readAllLines(FILE_NAME).isEmpty()) {
            save(new PremiumHall("HAL-001", "Cinema Hall 1 (Dolby Atmos)", 8, 12, 3));
            save(new StandardHall("HAL-002", "Cinema Hall 2 (Laser Lounge)", 8, 12, 2));
            save(new PremiumHall("HAL-003", "Cinema Hall 3 (Cozy Premium)", 8, 12, 3));
        }
    }

    @Override
    public Optional<CinemaHall> findById(String hallId) {
        if (hallId == null) return Optional.empty();
        return findAll().stream().filter(h -> hallId.equalsIgnoreCase(h.getHallId())).findFirst();
    }

    @Override
    public List<CinemaHall> findAll() {
        List<String> lines = storage.readAllLines(FILE_NAME);
        List<CinemaHall> list = new ArrayList<>();
        for (String line : lines) {
            CinemaHall h = parseLine(line);
            if (h != null) list.add(h);
        }
        return list;
    }

    @Override
    public boolean save(CinemaHall hall) {
        if (hall == null || existsById(hall.getHallId())) return false;
        return storage.appendLine(FILE_NAME, formatLine(hall));
    }

    @Override
    public boolean update(CinemaHall hall) {
        if (hall == null) return false;
        List<CinemaHall> all = findAll();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getHallId().equalsIgnoreCase(hall.getHallId())) {
                all.set(i, hall);
                found = true;
                break;
            }
        }
        if (!found) return false;
        List<String> lines = new ArrayList<>();
        for (CinemaHall h : all) lines.add(formatLine(h));
        return storage.writeAllLines(FILE_NAME, lines);
    }

    @Override
    public boolean deleteById(String hallId) {
        List<CinemaHall> all = findAll();
        boolean removed = all.removeIf(h -> h.getHallId().equalsIgnoreCase(hallId));
        if (!removed) return false;
        List<String> lines = new ArrayList<>();
        for (CinemaHall h : all) lines.add(formatLine(h));
        return storage.writeAllLines(FILE_NAME, lines);
    }

    @Override
    public boolean existsById(String hallId) {
        return findById(hallId).isPresent();
    }

    private CinemaHall parseLine(String line) {
        try {
            String[] p = StringUtil.splitRecord(line);
            if (p.length < 6) return null;
            String hallId = p[0];
            String name = p[1];
            String type = p[2];
            int totalRows = Integer.parseInt(p[3]);
            int seatsPerRow = Integer.parseInt(p[4]);
            int vipRows = Integer.parseInt(p[5]);

            if ("PREMIUM".equalsIgnoreCase(type)) {
                return new PremiumHall(hallId, name, totalRows, seatsPerRow, vipRows);
            } else {
                return new StandardHall(hallId, name, totalRows, seatsPerRow, vipRows);
            }
        } catch (Exception e) {
            return null;
        }
    }

    private String formatLine(CinemaHall h) {
        return StringUtil.joinRecord(
                h.getHallId(),
                h.getName(),
                h.getHallType(),
                String.valueOf(h.getTotalRows()),
                String.valueOf(h.getSeatsPerRow()),
                String.valueOf(h.getVipRowsCount())
        );
    }
}