package com.cinereserve.user.service;

import com.cinereserve.user.model.*;
import com.cinereserve.user.repository.UserRepository;
import com.cinereserve.user.util.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service orchestrating user authentication, registration, tier upgrades, and reward calculations.
 */
public class UserService {

    private final UserRepository repository;

    public UserService() {
        this.repository = new UserRepository();
    }

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User registerUser(String username, String rawPassword, String fullName, String email, String phone, String membershipType) {
        if (!ValidationUtil.isValidUsername(username)) {
            throw new IllegalArgumentException("Username must be alphanumeric (3-25 chars)");
        }
        if (!ValidationUtil.isValidPassword(rawPassword)) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (repository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Username already taken");
        }
        if (repository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Email already registered");
        }

        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hashPassword(rawPassword, salt);
        String userId = "USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        User newUser;
        if ("PREMIUM".equalsIgnoreCase(membershipType)) {
            newUser = new PremiumUser(userId, username, hash, salt, fullName, email, phone, 50, now); // 50 bonus sign-up points
        } else if ("ADMIN".equalsIgnoreCase(membershipType)) {
            newUser = new AdminUser(userId, username, hash, salt, fullName, email, phone, "MANAGER", now);
        } else {
            newUser = new RegularUser(userId, username, hash, salt, fullName, email, phone, 10, now);
        }

        repository.save(newUser);
        return newUser;
    }

    public Optional<User> authenticate(String username, String password) {
        Optional<User> userOpt = repository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (PasswordUtil.verifyPassword(password, user.getPasswordHash(), user.getSalt())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public boolean upgradeTier(String userId, String newTier) {
        Optional<User> opt = repository.findById(userId);
        if (!opt.isPresent()) return false;
        User current = opt.get();

        User upgraded;
        if ("PREMIUM".equalsIgnoreCase(newTier)) {
            upgraded = new PremiumUser(current.getUserId(), current.getUsername(), current.getPasswordHash(),
                    current.getSalt(), current.getFullName(), current.getEmail(), current.getPhoneNumber(),
                    current.getLoyaltyPoints() + 50, current.getCreatedAt());
        } else {
            upgraded = new RegularUser(current.getUserId(), current.getUsername(), current.getPasswordHash(),
                    current.getSalt(), current.getFullName(), current.getEmail(), current.getPhoneNumber(),
                    current.getLoyaltyPoints(), current.getCreatedAt());
        }
        return repository.update(upgraded);
    }

    public boolean addLoyaltyPoints(String userId, int points) {
        Optional<User> opt = repository.findById(userId);
        if (!opt.isPresent()) return false;
        User user = opt.get();
        user.addLoyaltyPoints(points);
        return repository.update(user);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return repository.findById(id);
    }

    public boolean deleteUser(String id) {
        return repository.deleteById(id);
    }
}
