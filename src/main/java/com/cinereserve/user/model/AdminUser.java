package com.cinereserve.user.model;

/**
 * =========================================================================
 * OOP PRINCIPLE DEMONSTRATED: INHERITANCE & POLYMORPHISM
 * =========================================================================
 * Subclass representing administrative staff with elevated privileges.
 */
public class AdminUser extends User {
    private static final long serialVersionUID = 1L;

    private String role; // "SUPER_ADMIN", "MANAGER", "MODERATOR"

    public AdminUser() {
        super();
        setMembershipType("ADMIN");
        this.role = "MANAGER";
    }

    public AdminUser(String userId, String username, String passwordHash, String salt,
                     String fullName, String email, String phoneNumber,
                     String role, String createdAt) {
        super(userId, username, passwordHash, salt, fullName, email, phoneNumber, "ADMIN", 0, createdAt);
        this.role = role != null ? role : "MANAGER";
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public double calculateDiscount(double subtotal) {
        return 0.0; // Administrators do not receive customer discounts
    }

    @Override
    public int calculateLoyaltyPointsEarned(double finalAmount) {
        return 0;
    }

    @Override
    public String getMembershipBadge() {
        return "ADMINISTRATOR [" + role + "]";
    }

    @Override
    public String getTierPrivileges() {
        return "System administration, user moderation, movie catalog management, audit logs";
    }
}
