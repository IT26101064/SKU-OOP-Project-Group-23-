# 👤 User Management Module — Apex Cinema


## 📌 1. Component Overview
The **User Management Module** handles the complete customer identity and account lifecycle within the Apex Cinema Online Booking System. It provides secure account registration, session-based authentication, user profile management with real-time input sanitization, dynamic loyalty tiering, and administrative account oversight.

All persistent records are maintained in a thread-safe, pipe-delimited text storage file (`data/users.txt`) without the use of external SQL or NoSQL database engines.

---

## ⚙️ 2. Core Features & Capabilities

* **Secure Authentication & Session Management**:
  * User registration with unique username and email conflict checks.
  * SHA-256 password hashing with salt via `PasswordUtil.java` to prevent plain-text credential leaks.
  * HTTP Session tracking (`HttpSession`) for persistent user state across booking flows.
* **Foolproof Phone Number Validation**:
  * **Real-time Keyboard Interception**: Hardware `keydown` event listeners physically block any non-numeric characters from being typed into phone inputs.
  * **Clipboard Sanitization**: Automatic regex stripping on `paste` and `input` events.
  * **Server-side Validation**: Regular expression validation (`^\+?[0-9]{9,15}$`) via `ValidationUtil.java`.
* **Dynamic Membership Tiers**:
  * Support for `RegularUser` (standard rate) and `PremiumUser` (automatic 15% discount on ticket checkouts).
* **Self-Service Profile Dashboard**:
  * Ability for customers to view booking history, update full name, modify phone numbers, and change passwords.
* **Administrative Moderation**:
  * Administrator overview table with instant search filter and account deactivation/deletion capabilities.

---

## 🔄 3. CRUD Operations Breakdown

This module implements complete **Create, Read, Update, Delete (CRUD)** operations:

| Operation | Action | Controller (Servlet) | Service / Repository Method | Data Storage File |
| :--- | :--- | :--- | :--- | :--- |
| **CREATE** | New user account registration | `UserServlet` (`/register`) | `UserService.register()` ➔ `UserRepository.save()` | `data/users.txt` |
| **READ** | User login authentication & profile view | `UserServlet` (`/login`, `/profile`) | `UserService.authenticate()` ➔ `UserRepository.findById()` | `data/users.txt` |
| **UPDATE** | Edit profile info, phone number & password | `UserServlet` (`/profile/edit`) | `UserService.updateProfile()` ➔ `UserRepository.save()` | `data/users.txt` |
| **DELETE** | Account removal by System Administrator | `AdminServlet` (`/admin/users/delete`) | `UserService.deleteUser()` ➔ `UserRepository.deleteById()` | `data/users.txt` |

---

## 🏛️ 4. OOP Principles Implementation

This module strictly adheres to the four fundamental pillars of Object-Oriented Programming:

### 1. Abstraction
* **Class**: `com.cinereserve.model.User`
* The abstract base class `User` defines generic account attributes and abstract contracts (`getMembershipDiscountRate()`, `getRoleTier()`) without binding to specific implementation details.

### 2. Inheritance
* **Classes**: `RegularUser.java` and `PremiumUser.java` extend `User.java`.
* Child classes inherit shared attributes (`userId`, `username`, `email`, `passwordHash`, `phoneNumber`) from the parent class, eliminating redundant code.

### 3. Polymorphism
* **Method**: Dynamic dispatch of `@Override public double getMembershipDiscountRate()`.
* When the checkout engine calculates discounts, it calls this method polymorphically on the logged-in `User` reference:
  * `RegularUser` returns `0.00` (0% discount).
  * `PremiumUser` returns `0.15` (15% discount).

### 4. Encapsulation
* All model fields are strictly `private`.
* Access and mutations are governed through public getters and validated setters.
* User state integrity is preserved—passwords and phone numbers cannot be corrupted or directly modified outside established business logic.

---

## 📂 5. File Structure & Class Mapping

```text
src/main/java/com/cinereserve/
├── model/
│   ├── User.java                # Abstract base entity with encapsulated fields
│   ├── RegularUser.java         # Inherited subclass representing standard customers
│   └── PremiumUser.java         # Inherited subclass with 15% discount implementation
├── repository/
│   └── UserRepository.java      # File I/O CRUD handler managing data/users.txt
├── service/
│   └── UserService.java         # Business logic: registration, login, profile updates
├── servlet/
│   ├── UserServlet.java         # Controller for /register, /login, /profile
│   └── AdminServlet.java        # Admin controller for /admin/users and account deletion
└── util/
    ├── PasswordUtil.java        # Secure cryptographic hashing
    └── ValidationUtil.java      # Server-side regex pattern enforcement

src/main/webapp/
├── WEB-INF/views/
│   ├── customer/
│   │   ├── register.jsp         # Registration UI with real-time numeric badges
│   │   ├── login.jsp            # Authentication form
│   │   └── profile.jsp          # Customer profile editor & tier overview
│   └── admin/
│       └── users.jsp            # Admin user table with search & delete controls
└── assets/js/
    └── main.js                  # Frontend keyboard interception (prevents letter typing)

data/
└── users.txt                    # Pipe-delimited file storage database
```

---

## 💾 6. Data Storage Format (`data/users.txt`)

Each user account is stored as a single pipe-delimited record:
```text
USER_ID|USERNAME|EMAIL|PASSWORD_HASH|FULL_NAME|PHONE_NUMBER|ROLE_TIER
```

**Example Record:**
```text
USR-101|sarah_c|sarah@example.com|ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f|Sarah Connor|+94771234567|PREMIUM
```
