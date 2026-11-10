package com.cinereserve.user;

import com.cinereserve.user.model.*;
import com.cinereserve.user.service.UserService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagementTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void testRegularUserPolymorphism() {
        User regular = new RegularUser("U1", "test_reg", "hash", "salt", "Test Reg", "reg@test.com", "12345678", 0, "2026-01-01");
        assertEquals("STANDARD MEMBER", regular.getMembershipBadge());
        // $100 bill -> 5% discount = $5.00
        assertEquals(5.0, regular.calculateDiscount(100.0));
        // $40 bill -> 0% discount
        assertEquals(0.0, regular.calculateDiscount(40.0));
        // Points: $100 -> 10 points
        assertEquals(10, regular.calculateLoyaltyPointsEarned(100.0));
    }

    @Test
    void testPremiumUserPolymorphism() {
        User premium = new PremiumUser("U2", "test_prem", "hash", "salt", "Test Prem", "prem@test.com", "12345678", 50, "2026-01-01");
        assertEquals("VIP GOLD MEMBER", premium.getMembershipBadge());
        // 15% discount flat -> $100 -> $15.00
        assertEquals(15.0, premium.calculateDiscount(100.0));
        // Double points: $100 -> 20 points
        assertEquals(20, premium.calculateLoyaltyPointsEarned(100.0));
    }
}
