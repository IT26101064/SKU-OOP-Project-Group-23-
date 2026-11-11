# 🎬 CineReserve - Cinema Reservation & Auditorium Management System
### SE1020: Object-Oriented Programming - University Group Project (Group 23)

A complete, enterprise-grade Cinema Reservation, Auditorium Management, and Ticket Booking System developed in **Java 17**, **Spring Boot 3.2.5**, **Thymeleaf**, **Bootstrap 5**, and local thread-safe file persistence (`data/*.txt`).

---

## 👥 Group Members & Module Allocation (Group 23)

| Member | Student ID | Student Name | GitHub Account | Assigned Subsystem / Module |
|:---:|:---:|:---|:---:|:---|
| **Member 1** | `IT26101062` | **M. C. Annathugoda** | [@IT26101062](https://github.com/IT26101062) | User Authentication, Customer Profiles & Loyalty Rewards Engine |
| **Member 2** | `IT26101061` | **Milakshan** | [@IT26101061Milakshan](https://github.com/IT26101061Milakshan) | Movie Catalog, Visual Formats & Cinema Experience Engine |
| **Member 3 ** | `IT26101064` | **Shanuka Dassanayake** | [@IT26101064](https://github.com/IT26101064) | Cinema Hall Layout, Screen Multipliers & Real-Time Seat Booking Engine |
| **Member 4** | `IT26101063` | **Niwandya Karunarathna** | [@IT26101063](https://github.com/IT26101063) | Showtime Scheduling, Screening Slots & Collision Avoidance Engine |
| **Member 5** | `IT26101065` | **Wanniarachchi W.A.M.P** | [@it26101065](https://github.com/it26101065) | Dynamic Ticket Pricing, Multi-Tier Admission Passes & Customer Booking Engine |
| **Member 6** | `IT26101066` | **Kavindi Chandrasena** | [@Kavi101066](https://github.com/Kavi101066) | Multi-Gateway Payment Processing, Audit Ledger & Cinema Tax Invoicing |

---

## 📦 Comprehensive Module Architecture & Responsibilities

### 👤 Member 1: User Management & Tier Loyalty Engine
* **Assigned Student:** M. C. Annathugoda (`IT26101062`)
* **Package:** `com.cinereserve.user`
* **Key Components:**
  * **Abstract Base Class:** `User` defining contract methods (`calculateDiscount`, `calculateLoyaltyPointsEarned`, `getMembershipBadge`).
  * **Inherited Subclasses:** `RegularUser`, `PremiumUser`, and `AdminUser`.
  * **Polymorphism:** Dynamic discount deductions (Regular 5%, VIP Gold 15%) and tier upgrade policies.
  * **Security & Encapsulation:** Salted SHA-256 password hashing and protected user credentials.
  * **Persistence:** `UserRepository` reading/writing to `data/users.txt`.
  * **Web Endpoints:** `/users` (Directory), `/users/register` (Registration), `/users/profile/{id}` (Loyalty Points & Upgrades).

---

### 🎬 Member 2: Movie Catalog & Format Experience Engine
* **Assigned Student:** Milakshan (`IT26101061`)
* **Package:** `com.cinereserve.movie`
* **Key Components:**
  * **Abstract Base Class:** `Movie` with contractual methods (`calculateTicketPriceMultiplier`, `getExperienceBadge`).
  * **Inherited Subclasses:** `Standard2DMovie`, `IMAX3DMovie`, and `FourDXMovie`.
  * **Polymorphism:** Experience pricing surcharges (Standard 1.0x, IMAX 1.5x, 4DX 2.0x).
  * **Encapsulation:** Rating classifications (`G`, `PG-13`, `R-18`), genre tagging, and duration bounds.
  * **Persistence:** `MovieRepository` reading/writing to `data/movies.txt`.
  * **Web Endpoints:** `/movies` (Catalog Grid), `/movies/add` (New Movie Entry), `/movies/view/{id}` (Detail Page).

---

### 💺 Member 3 (Team Leader): Cinema Hall Layout & Real-Time Seat Booking Engine
* **Assigned Student:** Shanuka Dassanayake (`IT26101064`)
* **Package:** `com.cinereserve.seat`
* **Key Components:**
  * **Abstract Base Class:** `CinemaHall` with tier multipliers and visual ASCII layout rendering.
  * **Inherited Subclasses:** `StandardHall` (Multiplier 1.00x) and `PremiumHall` (Multiplier 1.35x with Recliner seats).
  * **Seat Modeling:** `Seat`, `SeatType` (`REGULAR`, `PREMIUM`, `RECLINER`), and dynamic status (`AVAILABLE`, `LOCKED`, `RESERVED`).
  * **Real-Time Collision Prevention Engine:** Thread-safe seat locking mechanism preventing double-booking race conditions during simultaneous user checkouts.
  * **ASCII Layout Visualization:** Dynamic console and web layout generator rendering real-time screen and seat status grids:
    ```text
    ==================== [ CINEMA SCREEN ] ====================
    Row A [VIP] : [A1:FREE] [A2:FREE] [A3:LOCK] [A4:RESV] [A5:FREE]
    Row B [VIP] : [B1:FREE] [B2:FREE] [B3:FREE] [B4:FREE] [B5:FREE]
    Row C [STD] : [C1:FREE] [C2:FREE] [C3:RESV] [C4:FREE] [C5:FREE]
    ```
  * **Persistence:** `ScreenRepository` reading/writing to `data/screens.txt`.
  * **Web Endpoints:** `/seats/layout` (Interactive Hall Screen), `/seats/lock` (Hold Seats), `/seats/halls` (Auditorium Registry).

---

### 🕒 Member 4: Showtime Scheduling & Collision Detection Engine
* **Assigned Student:** Niwandya Karunarathna (`IT26101063`)
* **Package:** `com.cinereserve.showtime`
* **Key Components:**
  * **Abstract Base Class:** `Showtime` defining schedule rules and time-slot badges.
  * **Inherited Subclasses:** `MatineeShowtime`, `PrimeShowtime`, and `StandardShowtime`.
  * **Collision Prevention Engine:** Temporal conflict detection algorithm ensuring no two movies overlap in the same hall.
  * **Polymorphism:** Prime-time price surcharges vs matinee discounted pricing.
  * **Persistence:** `ShowtimeRepository` reading/writing to `data/showtimes.txt`.
  * **Web Endpoints:** `/showtimes` (Timetable), `/showtimes/add` (Scheduling Form), `/showtimes/cancel/{id}` (Cancellation).

---

### 🎟️ Member 5: Ticket Pricing, Admission Passes & Customer Booking Engine
* **Assigned Student:** Wanniarachchi W.A.M.P (`IT26101065`)
* **Package:** `com.cinereserve.ticket`
* **Key Components:**
  * **Abstract Base Class:** `Ticket` defining pass barcode generation, perk summaries, and pricing policies.
  * **Inherited Subclasses:** `StandardTicket`, `VIPTicket`, and `StudentTicket`.
  * **Booking Aggregator:** `Booking` model generating unique alphanumeric pass references (`CR-YYYYMMDD-XXXX`).
  * **Polymorphism:** Concession discounts (Student 20% off) vs VIP lounge perks & luxury amenities.
  * **Persistence:** `BookingRepository` reading/writing to `data/bookings.txt`.
  * **Web Endpoints:** `/bookings` (Master Registry), `/bookings/new` (Reservation Checkout), `/bookings/{ref}` (Digital E-Ticket).

---

### 💳 Member 6: Multi-Gateway Payment Processing & Invoicing Engine
* **Assigned Student:** Kavindi Chandrasena (`IT26101066`)
* **Package:** `com.cinereserve.payment`
* **Key Components:**
  * **Abstract Base Class:** `Payment` declaring payment execution lifecycle and receipt formatting.
  * **Inherited Subclasses:** `CreditCardPayment`, `DebitCardPayment`, and `DigitalWalletPayment`.
  * **Polymorphism:** Payment gateway authorization fee structures and verification rules.
  * **Confirmation & Audit:** `PaymentConfirmation` generating official cinema tax invoices and audit ledger entries.
  * **Persistence:** `PaymentRepository` reading/writing to `data/payments.txt`.
  * **Web Endpoints:** `/payments/checkout` (Checkout Gateway), `/payments/invoice/{ref}` (Official Tax Invoice), `/payments/history` (Audit Ledger).

---

## 🏛️ System-Wide OOP Principles Implementation

1. **Abstraction:**
   Every subsystem features an abstract base class (`User`, `Movie`, `CinemaHall`, `Showtime`, `Ticket`, `Payment`) ensuring high-level architectural contracts without exposing internal operational mechanics.
2. **Inheritance:**
   Subclasses inherit and extend attributes and behaviors from base abstractions, ensuring DRY (Don't Repeat Yourself) clean code.
3. **Polymorphism:**
   Runtime dynamic method dispatch calculates tiered discounts, hall surcharges, admission pass benefits, and gateway fees seamlessly.
4. **Encapsulation:**
   All domain fields are strictly `private` with validated getters and setters, defensive copies, and immutable identifiers.
5. **Separation of Concerns (MVC):**
   Clean separation between Models (`com.cinereserve.*.model`), Repositories (`*.repository`), Services (`*.service`), and Spring Web Controllers (`*.controller`).

---

## 🚀 How to Run the Unified Cinema Web Application

### Prerequisites
* **Java Development Kit (JDK):** Version 17 or higher
* **Apache Maven:** 3.8+ (or IntelliJ IDEA bundled Maven)

### Run via Command Line / Terminal:
```bash
# In the project root directory:
mvn clean compile
mvn spring-boot:run -DskipTests
```

### Accessing the Web Portal:
Open your web browser and navigate to:
```
http://localhost:8080/
```
The central dashboard provides one-click navigation across all 6 members' interactive subsystems.
