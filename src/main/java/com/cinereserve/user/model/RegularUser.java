package com.cinereserve.user.model;

/**
 * =========================================================================
 * OOP PRINCIPLE DEMONSTRATED: INHERITANCE & POLYMORPHISM
 * =========================================================================
 * Subclass representing a standard customer.
 * Inherits common fields and methods from User, overrides abstract methods.
 */
public class RegularUser extends User {
    private static final long serialVersionUID = 1L;

    public RegularUser() {
        super();
        setMembershipType("REGULAR");
    }

    public RegularUser(String userId, String username, String passwordHash, String salt,
                       String fullName, String email, String phoneNumber,
                       int loyaltyPoints, String createdAt) {
        super(userId, username, passwordHash, salt, fullName, email, phoneNumber, "REGULAR", loyaltyPoints, createdAt);
    }

    @Override
    public double calculateDiscount(double subtotal) {
        // Regular members receive 5% discount on large bookings over $50
        if (subtotal >= 50.0) {
            return subtotal * 0.05;
        }
        return 0.0;
    }

    @Override
    public int calculateLoyaltyPointsEarned(double finalAmount) {
        // Standard rate: 1 loyalty point per $10 spent
        return (int) Math.floor(finalAmount / 10.0);
    }

    @Override
    public String getMembershipBadge() {
        return "STANDARD MEMBER";
    }

    @Override
    public String getTierPrivileges() {
        return "Standard seating, 5% discount over $50, 1 loyalty pt per $10";
    }
}
