# 🎬 CineReserve - Cinema Reservation & Auditorium Management System
### SE1020: Object-Oriented Programming - University Group Project (Group 23)

A comprehensive Cinema Reservation, Auditorium Management, and Ticket Booking System developed in Java 17, Spring Boot, Thymeleaf, and pure Object-Oriented Programming (OOP) architecture.

---

## 👥 Group Members & Module Allocation

| Member | Student ID | Name | Assigned Subsystem |
|---|---|---|---|
| **Member 1** | IT26101062 | M.C.Annathugoda | User Authentication, Profiles & Loyalty Rewards |
| **Member 2** | IT26101061 | Milakshan | Movie Catalog, Formats & Experience Engine |
| **Member 3 (Leader)** | IT26101064 | Shanuka Dassanayake | Cinema Hall Layout & Real-Time Seat Booking |
| **Member 4** | IT26101063 | Niwandya Karunarathna | Showtime Scheduling & Collision Detection Engine |
| **Member 5** | IT26101065 | Wanniarachchi W.A.M.P | Ticket Pricing, Admission Passes & Booking Engine |
| **Member 6** | IT26101066 | Kavindi Chandrasena | Multi-Gateway Payment Processing & Invoicing |

---

## 🎟️ Member 5: Ticket Pricing & Customer Reservation Management
- **Assigned Student:** Wanniarachchi W.A.M.P (`IT26101065`)
- **Module Responsibilities:**
  - Dynamic ticket price calculation with hall multipliers and customer loyalty discounts.
  - Tiered admission passes (`StandardTicket`, `VIPTicket`, `StudentTicket`) using OOP polymorphism.
  - Unique booking reference generation (`CR-YYYYMMDD-XXXX`) and lifecycle state tracking.
  - Local thread-safe text file persistence (`data/bookings.txt`) via generic Repository architecture.
