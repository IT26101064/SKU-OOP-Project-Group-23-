package com.cinereserve.user.model;

/**
 * =========================================================================
 * OOP PRINCIPLE DEMONSTRATED: INHERITANCE & POLYMORPHISM
 * =========================================================================
 * Subclass representing a VIP Gold tier customer.
 * Overrides discount and rewards calculations with premium rates.
 */
public class PremiumUser extends User {
    private static final long serialVersionUID = 1L;

    public PremiumUser() {
        super();
        setMembershipType("PREMIUM");
    }

    public PremiumUser(String userId, String username, String passwordHash, String salt,
                       String fullName, String email, String phoneNumber,
                       int loyaltyPoints, String createdAt) {
        super(userId, username, passwordHash, salt, fullName, email, phoneNumber, "PREMIUM", loyaltyPoints, createdAt);
    }

    @Override
    public double calculateDiscount(double subtotal) {
        // VIP Gold members receive a flat 15% discount on all purchases
        return subtotal * 0.15;
    }

    @Override
    public int calculateLoyaltyPointsEarned(double finalAmount) {
        // Double reward points: 2 loyalty points per $10 spent
        return (int) Math.floor((finalAmount / 10.0) * 2.0);
    }

    @Override
    public String getMembershipBadge() {
        return "VIP GOLD MEMBER";
    }

    @Override
    public String getTierPrivileges() {
        return "Flat 15% discount, 2x loyalty points, VIP lounge access, priority booking";
    }
}
