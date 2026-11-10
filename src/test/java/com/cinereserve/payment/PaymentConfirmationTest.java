package com.cinereserve.payment;

import com.cinereserve.payment.model.*;
import com.cinereserve.payment.service.PaymentService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentConfirmationTest {

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService();
    }

    @Test
    void testCreditCardPaymentPolymorphism() {
        Payment cc = new CreditCardPayment("P1", "John", 100.0, "1234567812345678", "12/28", "123", "VISA");
        assertTrue(cc.processPayment(100.0));
        assertEquals(2.00, cc.calculateTransactionFee(100.0)); // 2% fee
        assertEquals("**** **** **** 5678", cc.getMaskedAccountDetails());
        assertTrue(cc.getPaymentProviderName().contains("VISA"));
    }

    @Test
    void testDebitCardPaymentPolymorphism() {
        Payment debit = new DebitCardPayment("P2", "Sarah", 100.0, "987654321", "WELLS FARGO");
        assertTrue(debit.processPayment(100.0));
        assertEquals(0.00, debit.calculateTransactionFee(100.0)); // 0% fee
        assertTrue(debit.getMaskedAccountDetails().contains("4321"));
    }

    @Test
    void testDigitalWalletPaymentPolymorphism() {
        Payment wallet = new DigitalWalletPayment("P3", "Alice", 100.0, "GOOGLE_PAY", "TOK-12345");
        assertTrue(wallet.processPayment(100.0));
        assertEquals(1.00, wallet.calculateTransactionFee(100.0)); // 1% fee
        assertTrue(wallet.getPaymentProviderName().contains("GOOGLE_PAY"));
    }

    @Test
    void testPaymentAndRefundCycle() {
        Payment cc = new CreditCardPayment("P9", "Bob", 50.0, "1111222233334444", "01/29", "999", "MASTERCARD");
        String bRef = "CR-TEST-REFUND-01";
        PaymentConfirmation conf = paymentService.processPayment(cc, bRef, 50.0);
        assertTrue(conf.isSuccessful());

        // Refund
        assertTrue(paymentService.issueRefund(bRef));
        PaymentConfirmation refunded = paymentService.getConfirmationByBookingRef(bRef).get();
        assertTrue(refunded.isRefunded());
    }
}