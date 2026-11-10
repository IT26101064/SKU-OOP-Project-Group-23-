package com.cinereserve.user.controller;

import com.cinereserve.user.model.User;
import com.cinereserve.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * MEMBER 1 - USER MANAGEMENT CONTROLLER (SE1020 OOP PROJECT)
 * ============================================================================
 * Web endpoints managing:
 * 1. User Registration (Create) -> /users/register
 * 2. User Directory & Search (Read) -> /users
 * 3. User Profile & Tier Rewards (Update) -> /users/profile/{id}
 * 4. User Account Deletion (Delete) -> /users/delete/{id}
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService = new UserService();

    // UI 2: All Users Directory / Search & Management (Read)
    @GetMapping
    public String listUsers(@RequestParam(value = "search", required = false) String search, Model model) {
        List<User> users = userService.getAllUsers();
        if (search != null && !search.trim().isEmpty()) {
            String s = search.toLowerCase();
            users = users.stream()
                    .filter(u -> u.getFullName().toLowerCase().contains(s) ||
                                 u.getUsername().toLowerCase().contains(s) ||
                                 u.getEmail().toLowerCase().contains(s) ||
                                 u.getUserId().toLowerCase().contains(s))
                    .toList();
        }
        model.addAttribute("users", users);
        model.addAttribute("search", search);
        return "user/users";
    }

    // UI 1: User Registration Page (Create Form)
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        return "user/register";
    }

    // Process Registration Form
    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("fullName") String fullName,
                               @RequestParam("email") String email,
                               @RequestParam("phone") String phone,
                               @RequestParam(value = "membershipType", defaultValue = "REGULAR") String membershipType,
                               RedirectAttributes redirectAttributes) {
        try {
            User newUser = userService.registerUser(username, password, fullName, email, phone, membershipType);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "User '" + newUser.getFullName() + "' (" + newUser.getMembershipBadge() + ") registered successfully! User ID: " + newUser.getUserId());
            return "redirect:/users";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/users/register";
        }
    }

    // UI 3: User Profile & Loyalty Rewards Details (Read/Update)
    @GetMapping("/profile/{id}")
    public String viewProfile(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "User with ID " + id + " not found!");
            return "redirect:/users";
        }
        User user = userOpt.get();
        model.addAttribute("user", user);
        model.addAttribute("discountRate", String.format("%.0f%%", user.calculateDiscount(100.0)));
        model.addAttribute("badge", user.getMembershipBadge());
        return "user/profile";
    }

    // Update Membership Tier
    @PostMapping("/profile/{id}/upgrade")
    public String upgradeTier(@PathVariable("id") String id,
                              @RequestParam("newTier") String newTier,
                              RedirectAttributes redirectAttributes) {
        boolean upgraded = userService.upgradeTier(id, newTier);
        if (upgraded) {
            redirectAttributes.addFlashAttribute("successMessage", "User tier upgraded successfully to " + newTier + "!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upgrade tier.");
        }
        return "redirect:/users/profile/" + id;
    }

    // Delete User (Delete)
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "User " + id + " deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete user " + id);
        }
        return "redirect:/users";
    }
}
