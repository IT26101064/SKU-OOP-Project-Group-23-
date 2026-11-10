# Member 2: Movie Management Component
### SE1020 - Object-Oriented Programming University Project

---

## 📌 Assigned Member Responsibilities
- **Subsystem:** Movie Catalog Administration, Multi-Format Surcharges (2D, RealD 3D, IMAX), Genre Filtering, and Search.
- **Core OOP Concepts Implemented:**
  - **Abstraction:** Abstract base class `Movie` declaring polymorphic methods (`getFormatSurcharge`, `getFormatBadge`, `includesComplimentary3DGlasses`, `getFormatDescription`).
  - **Inheritance:** `StandardMovie`, `ThreeDMovie`, and `ImaxMovie` extending `Movie`.
  - **Polymorphism:** Runtime dynamic dispatch for format price adjustments and technical gear requirements.
  - **Encapsulation:** Private attributes with getters/setters, runtime duration validation (30 - 360 mins), and rating constraints.
  - **Persistence:** Local thread-safe text file I/O (`data/movies.txt`) implementing `Repository<Movie, String>`.

---

## 🚀 How to Run in IntelliJ IDEA
1. Open IntelliJ IDEA &rarr; **File** &rarr; **Open...**
2. Select this folder: `Member2_Movie_Management`.
3. Allow IntelliJ to import it as a **Maven project**.
4. Navigate to: `src/main/java/com/cinereserve/movie/MovieManagementMain.java`.
5. Right-click &rarr; **Run 'MovieManagementMain.main()'**.
6. To run unit tests, right-click `src/test/java/com/cinereserve/movie/MovieManagementTest.java` &rarr; **Run 'MovieManagementTest'**.

---

## 📤 Step-by-Step Instructions: Push to GitHub from IntelliJ IDEA
1. In IntelliJ IDEA top menu, click **Git** &rarr; **Enable Version Control Integration...**
2. Select **Git** and click **OK**.
3. In the top menu, click **Git** &rarr; **GitHub** &rarr; **Share Project on GitHub**.
4. Enter repository name (e.g., `cinereserve-movie-management`), select Public, and click **Share**.