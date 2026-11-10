package com.cinereserve.user;

import com.cinereserve.user.model.*;
import com.cinereserve.user.service.UserService;

import java.util.List;

/**
 * ============================================================================
 * SE1020 OBJECT-ORIENTED PROGRAMMING - MEMBER 1 DEMO
 * COMPONENT: USER MANAGEMENT
 * ============================================================================
 * Demonstrates:
 * 1. Abstraction (User abstract base class & Repository interface)
 * 2. Inheritance (RegularUser, PremiumUser, AdminUser extending User)
 * 3. Polymorphism (calculateDiscount, calculateLoyaltyPointsEarned, getMembershipBadge)
 * 4. Encapsulation (Private fields, input validation, salted SHA-256 password hashing)
 * 5. Persistence (Thread-safe File I/O into data/users.txt)
 */
public class UserManagementMain {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("             CINERESERVE - USER MANAGEMENT SYSTEM (MEMBER 1)                   ");
        System.out.println("================================================================================");

        UserService service = new UserService();

        System.out.println("\n--- [STEP 1: Registering Demo Users across different Tiers] ---");
        try {
            service.registerUser("john_doe", "User123!", "John Doe", "john@example.com", "+1-555-0101", "REGULAR");
            System.out.println(">> Registered Regular User: john_doe");
        } catch (Exception e) {
            System.out.println("Note: " + e.getMessage());
        }

        try {
            service.registerUser("sarah_connor", "User123!", "Sarah Connor", "sarah@cyberdyne.com", "+1-555-0102", "PREMIUM");
            System.out.println(">> Registered Premium User: sarah_connor (VIP Gold)");
        } catch (Exception e) {
            System.out.println("Note: " + e.getMessage());
        }

        try {
            service.registerUser("admin_alice", "AdminPass99!", "Alice Smith", "admin@cinereserve.com", "+1-555-0199", "ADMIN");
            System.out.println(">> Registered Admin User: admin_alice");
        } catch (Exception e) {
            System.out.println("Note: " + e.getMessage());
        }

        System.out.println("\n--- [STEP 2: Demonstrating Polymorphism on Bill Discount & Loyalty Points] ---");
        double testBillAmount = 100.00;
        System.out.printf("Test Cinema Booking Subtotal: $%.2f\n\n", testBillAmount);

        List<User> users = service.getAllUsers();
        for (User u : users) {
            double discount = u.calculateDiscount(testBillAmount);
            double finalPrice = testBillAmount - discount;
            int pointsEarned = u.calculateLoyaltyPointsEarned(finalPrice);

            System.out.println("User: " + u.getFullName() + " | Username: " + u.getUsername());
            System.out.println("  Tier Badge      : " + u.getMembershipBadge());
            System.out.println("  Privileges      : " + u.getTierPrivileges());
            System.out.printf("  Calculated Disc : $%.2f (Final to Pay: $%.2f)\n", discount, finalPrice);
            System.out.println("  Loyalty Points  : +" + pointsEarned + " points awarded");
            System.out.println("  -------------------------------------------------------------");
        }

        System.out.println("\n--- [STEP 3: Demonstrating Secure Authentication with Salted SHA-256] ---");
        System.out.print("Testing Login with 'sarah_connor' / 'User123!' -> ");
        if (service.authenticate("sarah_connor", "User123!").isPresent()) {
            System.out.println("SUCCESS (Authenticated!)");
        } else {
            System.out.println("FAILED");
        }

        System.out.print("Testing Login with 'sarah_connor' / 'WrongPassword' -> ");
        if (service.authenticate("sarah_connor", "WrongPassword").isPresent()) {
            System.out.println("UNEXPECTED SUCCESS");
        } else {
            System.out.println("CORRECTLY REJECTED (Invalid password!)");
        }

        System.out.println("\n================================================================================");
        System.out.println("  MEMBER 1 MODULE READY: All OOP concepts validated and verified!              ");
        System.out.println("================================================================================");
    }
}
