package com.cinereserve.user.model;

import java.io.Serializable;

/**
 * =========================================================================
 * OOP PRINCIPLE DEMONSTRATED: ABSTRACTION & ENCAPSULATION
 * =========================================================================
 * Abstract base class representing a general user in the cinema system.
 * Defines the contract that every specific user tier must implement.
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    // Encapsulated private attributes
    private String userId;
    private String username;
    private String passwordHash;
    private String salt;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String membershipType; // "REGULAR", "PREMIUM", "ADMIN"
    private int loyaltyPoints;
    private String createdAt;

    // Default constructor
    public User() {
    }

    // Parameterized constructor
    public User(String userId, String username, String passwordHash, String salt,
                String fullName, String email, String phoneNumber,
                String membershipType, int loyaltyPoints, String createdAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.membershipType = membershipType;
        this.loyaltyPoints = loyaltyPoints;
        this.createdAt = createdAt;
    }

    // =========================================================================
    // ABSTRACT METHODS (ABSTRACTION & POLYMORPHISM)
    // Every subclass must provide its own specific business logic.
    // =========================================================================

    /**
     * Calculates customer discount based on their membership tier.
     * @param subtotal original ticket bill
     * @return discount amount in dollars
     */
    public abstract double calculateDiscount(double subtotal);

    /**
     * Calculates reward points earned from a transaction.
     * @param finalAmount paid amount
     * @return points earned
     */
    public abstract int calculateLoyaltyPointsEarned(double finalAmount);

    /**
     * Returns a formatted badge for UI or console.
     */
    public abstract String getMembershipBadge();

    /**
     * Lists perks and privileges associated with this tier.
     */
    public abstract String getTierPrivileges();

    // =========================================================================
    // GETTERS AND SETTERS (ENCAPSULATION)
    // =========================================================================

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public void addLoyaltyPoints(int points) {
        if (points > 0) {
            this.loyaltyPoints += points;
        }
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("[%s] ID: %s | Username: %s | Name: %s | Email: %s | Points: %d",
                getMembershipBadge(), userId, username, fullName, email, loyaltyPoints);
    }
}
