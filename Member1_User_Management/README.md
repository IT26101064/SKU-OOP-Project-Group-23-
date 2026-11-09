# Member 1: User Management Component
### SE1020 - Object-Oriented Programming University Project

---

## 📌 Assigned Member Responsibilities
- **Subsystem:** User Authentication, Profile Management, Tier Upgrades, and Loyalty Points Engine.
- **Core OOP Concepts Implemented:**
  - **Abstraction:** Abstract base class `User` defining contractual methods (`calculateDiscount`, `calculateLoyaltyPointsEarned`, `getMembershipBadge`).
  - **Inheritance:** Concrete subclasses `RegularUser`, `PremiumUser`, and `AdminUser` extending `User`.
  - **Polymorphism:** Runtime dynamic method dispatch for tiered discounts and loyalty rewards.
  - **Encapsulation:** Private attributes with accessor/mutator methods, input validation, and salted SHA-256 password hashing.
  - **Persistence:** Local thread-safe text file I/O (`data/users.txt`) via generic `Repository<T, ID>` interface.

---

## 🚀 How to Run in IntelliJ IDEA
1. Open IntelliJ IDEA &rarr; **File** &rarr; **Open...**
2. Select this folder: `Member1_User_Management`.
3. Allow IntelliJ to import it as a **Maven project**.
4. Navigate to: `src/main/java/com/cinereserve/user/UserManagementMain.java`.
5. Right-click &rarr; **Run 'UserManagementMain.main()'** (or click the green Play icon).
6. To run JUnit 5 tests, right-click `src/test/java/com/cinereserve/user/UserManagementTest.java` &rarr; **Run 'UserManagementTest'**.

---

## 📤 Step-by-Step Instructions: Push to GitHub from IntelliJ IDEA
1. In IntelliJ IDEA top menu, click **VCS** (or **Git**) &rarr; **Enable Version Control Integration...**
2. Select **Git** and click **OK**.
3. In the top menu, click **Git** &rarr; **GitHub** &rarr; **Share Project on GitHub**.
4. Log into your GitHub account (or choose token), enter repository name (e.g., `cinereserve-user-management`), select Public/Private, and click **Share**.
5. IntelliJ will automatically commit all project files and push them to your GitHub repository!
