package com.cinereserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ============================================================================
 * CINERESERVE - CINEMA RESERVATION & AUDITORIUM MANAGEMENT SYSTEM
 * SE1020 Object-Oriented Programming University Project
 * ============================================================================
 * Unified Spring Boot Web Application hosting all 6 member components:
 * - Member 1: User Management & Tier Rewards (com.cinereserve.user)
 * - Member 2: Movie Catalog & Format Management (com.cinereserve.movie)
 * - Member 3: Cinema Hall Layout & Real-Time Seat Booking (com.cinereserve.seat)
 * - Member 4: Showtime Scheduling & Collision Engine (com.cinereserve.showtime)
 * - Member 5: Ticket Pricing & Customer Bookings (com.cinereserve.ticket)
 * - Member 6: Multi-Gateway Payment & Invoicing (com.cinereserve.payment)
 */
@SpringBootApplication(scanBasePackages = "com.cinereserve")
public class CineReserveWebApplication {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("   STARTING CINERESERVE CINEMA WEB PORTAL (SE1020 OOP PROJECT)                  ");
        System.out.println("   Web Portal URL: http://localhost:8080                                        ");
        System.out.println("================================================================================");
        SpringApplication.run(CineReserveWebApplication.class, args);
    }
}
