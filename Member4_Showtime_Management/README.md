# Member 4: Showtime Management Component
### SE1020 - Object-Oriented Programming University Project

---

## 📌 Assigned Member Responsibilities
- **Subsystem:** Screening Schedule Management, Multi-Tier Time Slot Multipliers (Matinee, Standard, Prime Peak), Hall Overlap Conflict Detection with Cleaning Buffer.
- **Core OOP Concepts Implemented:**
  - **Abstraction:** Abstract base class `Showtime` declaring contractual time-slot methods (`calculateTimeSlotMultiplier`, `getSlotBadge`, `isPeakHour`, `getSlotDescription`).
  - **Inheritance:** Concrete subclasses `MatineeShowtime`, `StandardShowtime`, and `PrimeShowtime` extending `Showtime`.
  - **Polymorphism:** Runtime dynamic dispatch for off-peak discounts (0.85x) and prime-time demand adjustments (1.20x).
  - **Encapsulation:** Invariant validation (`startTime < endTime`, date/time regex) and robust interval collision algorithm with 15-minute cleaning buffer.
  - **Persistence:** Local thread-safe text file I/O (`data/showtimes.txt`) via `Repository<Showtime, String>`.

---

## 🚀 How to Run in IntelliJ IDEA
1. Open IntelliJ IDEA &rarr; **File** &rarr; **Open...**
2. Select this folder: `Member4_Showtime_Management`.
3. Allow IntelliJ to import it as a **Maven project**.
4. Navigate to: `src/main/java/com/cinereserve/showtime/ShowtimeManagementMain.java`.
5. Right-click &rarr; **Run 'ShowtimeManagementMain.main()'**.
6. To run tests, right-click `src/test/java/com/cinereserve/showtime/ShowtimeManagementTest.java` &rarr; **Run 'ShowtimeManagementTest'**.

---

## 📤 Step-by-Step Instructions: Push to GitHub from IntelliJ IDEA
1. In IntelliJ IDEA top menu, click **Git** &rarr; **Enable Version Control Integration...**
2. Select **Git** and click **OK**.
3. In the top menu, click **Git** &rarr; **GitHub** &rarr; **Share Project on GitHub**.
4. Enter repository name (e.g., `cinereserve-showtime-management`), select Public, and click **Share**.