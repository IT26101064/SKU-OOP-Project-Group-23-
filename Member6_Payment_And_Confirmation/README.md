# Member 6: Payment & Confirmation Management Component
### SE1020 - Object-Oriented Programming University Project

---

## 📌 Assigned Member Responsibilities
- **Subsystem:** Payment Gateway Abstraction (Credit Card, Debit Card, Digital Wallets), Card Masking Security, Transaction Confirmation Generation, Tax Invoice & Barcode Pass Rendering, Refund Processing.
- **Core OOP Concepts Implemented:**
  - **Abstraction:** Abstract base class `Payment` declaring contractual processing methods (`processPayment`, `calculateTransactionFee`, `getPaymentProviderName`, `getMaskedAccountDetails`, `getReceiptFooter`).
  - **Inheritance:** Concrete subclasses `CreditCardPayment`, `DebitCardPayment`, and `DigitalWalletPayment` extending `Payment`.
  - **Polymorphism:** Runtime dynamic dispatch for payment validation, transaction fees (0% debit, 1% wallet, 2% credit), and account masking.
  - **Encapsulation:** PCI-DSS inspired account number masking (`**** **** **** 1234`), CVV security, immutable transaction confirmation ledger.
  - **Persistence:** Local thread-safe text file I/O (`data/payments.txt`) via `Repository<PaymentConfirmation, String>`.

---

## 🚀 How to Run in IntelliJ IDEA
1. Open IntelliJ IDEA &rarr; **File** &rarr; **Open...**
2. Select this folder: `Member6_Payment_And_Confirmation`.
3. Allow IntelliJ to import it as a **Maven project**.
4. Navigate to: `src/main/java/com/cinereserve/payment/PaymentConfirmationMain.java`.
5. Right-click &rarr; **Run 'PaymentConfirmationMain.main()'**.
6. To run tests, right-click `src/test/java/com/cinereserve/payment/PaymentConfirmationTest.java` &rarr; **Run 'PaymentConfirmationTest'**.

---

## 📤 Step-by-Step Instructions: Push to GitHub from IntelliJ IDEA
1. In IntelliJ IDEA top menu, click **Git** &rarr; **Enable Version Control Integration...**
2. Select **Git** and click **OK**.
3. In the top menu, click **Git** &rarr; **GitHub** &rarr; **Share Project on GitHub**.
4. Enter repository name (e.g., `cinereserve-payment-confirmation`), select Public, and click **Share**.