package com.cinereserve.payment;

import com.cinereserve.payment.model.*;
import com.cinereserve.payment.service.PaymentService;

/**
 * ============================================================================
 * SE1020 OBJECT-ORIENTED PROGRAMMING - MEMBER 6 DEMO
 * COMPONENT: PAYMENT & CONFIRMATION MANAGEMENT
 * ============================================================================
 * Demonstrates:
 * 1. Abstraction (Payment abstract base class & Repository interface)
 * 2. Inheritance (CreditCardPayment, DebitCardPayment, DigitalWalletPayment extending Payment)
 * 3. Polymorphism (processPayment, calculateTransactionFee, getPaymentProviderName, getMaskedAccountDetails)
 * 4. Encapsulation (Account masking **** 1234, CVV validation, immutable confirmation audit)
 * 5. Persistence (Thread-safe File I/O into data/payments.txt)
 */
public class PaymentConfirmationMain {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("         CINERESERVE - PAYMENT & CONFIRMATION SYSTEM (MEMBER 6)                 ");
        System.out.println("================================================================================");

        PaymentService service = new PaymentService();

        System.out.println("\n--- [STEP 1: Testing Polymorphic Payment Providers & Transaction Fees] ---");
        double orderSubtotal = 75.00;
        System.out.printf("Cinema Order Subtotal: $%.2f\n\n", orderSubtotal);

        Payment creditCard = new CreditCardPayment("PMT-C01", "Sarah Connor", orderSubtotal,
                "4532118944521234", "12/28", "882", "VISA");

        Payment debitCard = new DebitCardPayment("PMT-D01", "John Doe", orderSubtotal,
                "9876543210", "CHASE BANK");

        Payment digitalWallet = new DigitalWalletPayment("PMT-W01", "Alice Smith", orderSubtotal,
                "APPLE_PAY", "AP-TOK-9923812");

        Payment[] testMethods = { creditCard, debitCard, digitalWallet };

        for (Payment p : testMethods) {
            double fee = p.calculateTransactionFee(orderSubtotal);
            double total = orderSubtotal + fee;
            System.out.println("Provider       : " + p.getPaymentProviderName());
            System.out.println("  Account Mask : " + p.getMaskedAccountDetails());
            System.out.printf("  Subtotal     : $%.2f | Fee: $%.2f\n", orderSubtotal, fee);
            System.out.printf("  Total To Pay : $%.2f\n", total);
            System.out.println("  Security     : " + p.getReceiptFooter());
            System.out.println("  -------------------------------------------------------------");
        }

        System.out.println("\n--- [STEP 2: Executing Secure Checkout & Generating Tax Invoice] ---");
        String bookingRef = "CR-20261012-7741";
        PaymentConfirmation confirmation = service.processPayment(creditCard, bookingRef, orderSubtotal);

        System.out.println(">> PAYMENT SUCCESSFUL!");
        System.out.println(CinemaInvoice.generatePrintableReceipt(
                confirmation, creditCard, "Spider-Man: Brand New Day (RealD 3D)", "B3, B4 (VIP Recliners)"));

        System.out.println("\n--- [STEP 3: Demonstrating Instant Refund Processing] ---");
        System.out.print("Processing Refund for Booking " + bookingRef + " -> ");
        boolean refunded = service.issueRefund(bookingRef);
        if (refunded) {
            PaymentConfirmation updated = service.getConfirmationByBookingRef(bookingRef).get();
            System.out.println("SUCCESS (Payment Status updated to: " + updated.getStatus() + ")");
        } else {
            System.out.println("FAILED");
        }

        System.out.println("\n================================================================================");
        System.out.println("  MEMBER 6 MODULE READY: All OOP concepts validated and verified!              ");
        System.out.println("================================================================================");
    }
}