# 💺 Member 3: Cinema Hall Layout & Real-Time Seat Booking Subsystem
### SE1020: Object-Oriented Programming University Project - Group 23
**Assigned Student:** Shanuka Dassanayake (`IT26101064`)

---

## 📌 Module Responsibilities & Core Features
1. **Interactive Auditorium Screen & Seat Grid:**
   * Dynamic seat coordinate mapping (Rows A-Z, Columns 1-N).
   * Real-time seat status tracking (`AVAILABLE`, `LOCKED`, `RESERVED`, `MAINTENANCE`).
2. **Concurrency Safety & Double-Booking Prevention:**
   * Thread-safe seat locking engine preventing two customers from reserving the same seat at the same time.
   * Atomic hold verification and automated release routines on checkout expiry.
3. **Auditorium Multiplier & VIP Recliner Surcharges:**
   * Polymorphic hall pricing (`StandardHall` 1.00x vs `PremiumHall` 1.35x).
   * VIP Recliner surcharges for premium front-row viewing experiences.
4. **Local Text File Persistence:**
   * Thread-safe file I/O storing auditorium layouts and configurations (`data/screens.txt`).

---

## 🔄 Member 3: Comprehensive CRUD Operations Implementation

This subsystem provides complete, thread-safe **CRUD (Create, Read, Update, Delete)** operations across auditorium configurations, seat layout instances, and session-based seat locks.

| Operation | Action / Feature | Java Service / Method | Web Endpoint | Persistence / Storage |
|:---:|:---|:---|:---|:---|
| **CREATE** | Register / Add New Cinema Hall | `CinemaHallRepository.save(CinemaHall hall)` | `/seats/halls/add` | Saved to `data/screens.txt` |
| **CREATE** | Initialize Auditorium Seat Grid | `SeatLayout.initializeLayout(rows, cols)` | `/seats/layout` | In-memory Grid Generation |
| **CREATE** | Real-Time Seat Hold / Lock (Hold) | `SeatBookingService.lockSeats(showtimeId, seatCodes)` | `POST /seats/lock` | Session Lock Registry |
| **READ** | Retrieve All Cinema Halls | `SeatBookingService.getAllHalls()` | `GET /seats/halls` | Loaded from `data/screens.txt` |
| **READ** | Query Single Cinema Hall by ID | `SeatBookingService.getHallById(String hallId)` | `GET /seats/halls/{id}` | Read from `Repository` |
| **READ** | View Real-Time Seat Layout & Grid | `SeatBookingService.getSeatLayoutForShowtime(...)` | `GET /seats/layout` | Dynamic Layout Matrix |
| **READ** | Render ASCII Terminal Seat Visualizer | `SeatLayout.renderAsciiMap()` | Console / Terminal | Console ASCII Output |
| **UPDATE** | Update Seat State (Lock &rarr; Reserved) | `Seat.setStatus(SeatStatus.RESERVED)` | `POST /bookings/new` | In-Memory & Booking Sync |
| **UPDATE** | Update Cinema Hall Specs / Multipliers | `CinemaHall.setHallPriceMultiplier(double mult)` | `POST /seats/halls/edit` | Re-saved to `data/screens.txt` |
| **UPDATE** | Reconfigure VIP Recliner Rows | `CinemaHall.setVipRowThreshold(int threshold)` | `POST /seats/halls/edit` | Re-saved to `data/screens.txt` |
| **DELETE** | Release Expired / Cancelled Locked Seats | `SeatBookingService.releaseLockedSeats(showId, seats)` | `POST /seats/release` | Cleared from Lock Registry |
| **DELETE** | Cancel Reserved Seats (Refund Sync) | `Seat.setStatus(SeatStatus.AVAILABLE)` | `POST /payments/refund` | Reset to Available |
| **DELETE** | Decommission / Remove Cinema Hall | `CinemaHallRepository.deleteById(String hallId)` | `POST /seats/halls/delete`| Removed from `data/screens.txt` |

---

### 💡 Detailed Breakdown of CRUD Methods

#### 1. 🟢 CREATE Operations
* **Add Cinema Hall:** Instantiates polymorphic `StandardHall` or `PremiumHall` and writes comma-delimited record (`hallId,name,type,rows,cols,vipRows`) to `data/screens.txt`.
  ```java
  CinemaHall hall = new PremiumHall("HAL-003", "IMAX Screen 1", 10, 12, 3);
  hallRepository.save(hall);
  ```
* **Real-Time Seat Lock (Hold):** Prevents double-booking collisions by atomically verifying that requested seats are `AVAILABLE` before transitioning them to `LOCKED`.
  ```java
  boolean locked = seatBookingService.lockSeats("SHW-001", List.of("A1", "A2"));
  ```

#### 2. 🔵 READ Operations
* **Query Seat Status & Layout:** Fetches real-time seat matrix for any movie screening session:
  ```java
  SeatLayout layout = seatBookingService.getSeatLayoutForShowtime("HAL-001", "SHW-001");
  Map<String, Seat> seats = layout.getAllSeats();
  ```
* **ASCII Layout Visualization:** Generates formatted interactive seat map with row indicators and status tags:
  ```text
  ==================== [ CINEMA SCREEN ] ====================
  Row A [VIP] : [A1:FREE] [A2:FREE] [A3:LOCK] [A4:RESV] [A5:FREE]
  Row B [VIP] : [B1:FREE] [B2:FREE] [B3:FREE] [B4:FREE] [B5:FREE]
  Row C [STD] : [C1:FREE] [C2:FREE] [C3:RESV] [C4:FREE] [C5:FREE]
  ```

#### 3. 🟡 UPDATE Operations
* **Seat State Transition:** Safely transitions seats through their lifecycle states:
  $$\text{AVAILABLE} \xrightarrow{\text{lock}} \text{LOCKED} \xrightarrow{\text{pay}} \text{RESERVED}$$
* **Auditorium Specifications:** Updates seat dimensions, row counts, and hall pricing rates dynamically.

#### 4. 🔴 DELETE Operations
* **Release Locked Seats:** Immediately unlocks held seats back to `AVAILABLE` if customer checkout cancels or exceeds the hold timeout window:
  ```java
  seatBookingService.releaseLockedSeats("SHW-001", List.of("A1", "A2"));
  ```
* **Hall Decommissioning:** Deletes obsolete screen records from file storage using thread-safe temporary file swap.

---

## 🏛️ OOP Principles Implementation Mapping

| OOP Concept | Implementation in Member 3 Subsystem |
|---|---|
| **Abstraction** | Abstract base class `CinemaHall` defining contractual methods (`getHallType`, `getHallPriceMultiplier`, `isVipSeat`, `renderAsciiMap`). |
| **Inheritance** | Subclasses `StandardHall` and `PremiumHall` extending `CinemaHall` with customized capacities and price rates. |
| **Polymorphism** | Dynamic method dispatch when calculating hall ticket surcharges and seat layouts across different auditorium types. |
| **Encapsulation** | Immutable seat identifiers, protected grid arrays, and defensive copying in `SeatLayout` and `Seat`. |

---

## 🧪 Testing & Execution Instructions
1. **Run Console UI Test Runner:**
   * File: `SeatBookingManagementMain.java`
   * Demonstrates interactive hall selection, real-time seat locking, and ASCII map rendering.
2. **Run JUnit 5 Automated Test Suite:**
   * File: `SeatBookingManagementTest.java`
   * Tests Standard/Premium hall polymorphism, capacity limits, and collision prevention.
3. **Web Interface Endpoints (Spring Boot):**
   * View visual screen grid: `http://localhost:8080/seats/layout?hallId=HAL-001`
   * Reserve & lock seats: `http://localhost:8080/seats/lock?hallId=HAL-001`
   * Cinema halls registry: `http://localhost:8080/seats/halls`
