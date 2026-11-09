package com.cinereserve.user.repository;

import com.cinereserve.user.model.*;
import com.cinereserve.user.util.StringUtil;

import java.util.*;

/**
 * Concrete repository managing persistent User entities in local text storage (users.txt).
 * Implements Repository<User, String> demonstrating polymorphic interface implementation.
 */
public class UserRepository implements Repository<User, String> {

    private static final String FILE_NAME = "users.txt";
    private final FileStorageService storage = FileStorageService.getInstance();

    public UserRepository() {
        storage.ensureFileExists(FILE_NAME);
    }

    @Override
    public Optional<User> findById(String userId) {
        if (userId == null) return Optional.empty();
        return findAll().stream().filter(u -> userId.equalsIgnoreCase(u.getUserId())).findFirst();
    }

    public Optional<User> findByUsername(String username) {
        if (username == null) return Optional.empty();
        return findAll().stream().filter(u -> username.equalsIgnoreCase(u.getUsername())).findFirst();
    }

    public Optional<User> findByEmail(String email) {
        if (email == null) return Optional.empty();
        return findAll().stream().filter(u -> email.equalsIgnoreCase(u.getEmail())).findFirst();
    }

    @Override
    public List<User> findAll() {
        List<String> lines = storage.readAllLines(FILE_NAME);
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            User u = parseLine(line);
            if (u != null) users.add(u);
        }
        return users;
    }

    @Override
    public boolean save(User user) {
        if (user == null || existsById(user.getUserId())) return false;
        String line = formatLine(user);
        return storage.appendLine(FILE_NAME, line);
    }

    @Override
    public boolean update(User user) {
        if (user == null) return false;
        String line = formatLine(user);
        return storage.updateLine(FILE_NAME, user.getUserId(), line);
    }

    @Override
    public boolean deleteById(String userId) {
        if (userId == null) return false;
        return storage.deleteLine(FILE_NAME, userId);
    }

    @Override
    public boolean existsById(String userId) {
        return findById(userId).isPresent();
    }

    private User parseLine(String line) {
        try {
            String[] parts = StringUtil.splitRecord(line);
            if (parts.length < 10) return null;

            String userId = parts[0];
            String username = parts[1];
            String passwordHash = parts[2];
            String salt = parts[3];
            String fullName = parts[4];
            String email = parts[5];
            String phoneNumber = parts[6];
            String membershipType = parts[7];
            int loyaltyPoints = Integer.parseInt(parts[8]);
            String createdAt = parts[9];

            if ("PREMIUM".equalsIgnoreCase(membershipType)) {
                return new PremiumUser(userId, username, passwordHash, salt, fullName, email, phoneNumber, loyaltyPoints, createdAt);
            } else if ("ADMIN".equalsIgnoreCase(membershipType)) {
                String role = parts.length > 10 ? parts[10] : "MANAGER";
                return new AdminUser(userId, username, passwordHash, salt, fullName, email, phoneNumber, role, createdAt);
            } else {
                return new RegularUser(userId, username, passwordHash, salt, fullName, email, phoneNumber, loyaltyPoints, createdAt);
            }
        } catch (Exception e) {
            return null;
        }
    }

    private String formatLine(User u) {
        String role = (u instanceof AdminUser) ? ((AdminUser) u).getRole() : "";
        return StringUtil.joinRecord(
                u.getUserId(),
                u.getUsername(),
                u.getPasswordHash(),
                u.getSalt(),
                u.getFullName(),
                u.getEmail(),
                u.getPhoneNumber(),
                u.getMembershipType(),
                String.valueOf(u.getLoyaltyPoints()),
                u.getCreatedAt(),
                role
        );
    }
}
