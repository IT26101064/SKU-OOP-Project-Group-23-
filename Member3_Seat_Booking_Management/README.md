# Member 3: Seat Booking & Hall Management Component
### SE1020 - Object-Oriented Programming University Project

---

## 📌 Assigned Member Responsibilities
- **Subsystem:** Cinema Auditorium Configuration, Seat Layout Matrix, Real-Time Seat Locking, Double-Booking Collision Prevention.
- **Core OOP Concepts Implemented:**
  - **Abstraction:** Abstract base class `CinemaHall` with contractual methods (`getHallPriceMultiplier`, `getSoundSystem`, `getScreenType`, `getAmenities`).
  - **Inheritance:** Concrete subclasses `StandardHall` and `PremiumHall` extending `CinemaHall`.
  - **Polymorphism:** Runtime dynamic price multiplier dispatch and auditorium amenity rendering.
  - **Encapsulation:** State machine for `Seat` status (`AVAILABLE` &rarr; `LOCKED` &rarr; `RESERVED`), row letter boundary calculation, and capacity metrics.
  - **Persistence:** Local thread-safe text file storage (`data/screens.txt`) via `Repository<CinemaHall, String>`.

---

## 🚀 How to Run in IntelliJ IDEA
1. Open IntelliJ IDEA &rarr; **File** &rarr; **Open...**
2. Select this folder: `Member3_Seat_Booking_Management`.
3. Allow IntelliJ to import it as a **Maven project**.
4. Navigate to: `src/main/java/com/cinereserve/seat/SeatBookingManagementMain.java`.
5. Right-click &rarr; **Run 'SeatBookingManagementMain.main()'**.
6. To run tests, right-click `src/test/java/com/cinereserve/seat/SeatBookingManagementTest.java` &rarr; **Run 'SeatBookingManagementTest'**.

---

## 📤 Step-by-Step Instructions: Push to GitHub from IntelliJ IDEA
1. In IntelliJ IDEA top menu, click **Git** &rarr; **Enable Version Control Integration...**
2. Select **Git** and click **OK**.
3. In the top menu, click **Git** &rarr; **GitHub** &rarr; **Share Project on GitHub**.
4. Enter repository name (e.g., `cinereserve-seat-booking`), select Public, and click **Share**.