package com.cinereserve.payment.model;
// Member 6 - Payment Module - Kavindi

import java.io.Serializable;

/**
 * =========================================================================
 * OOP PRINCIPLE DEMONSTRATED: ABSTRACTION & ENCAPSULATION
 * =========================================================================
 * Abstract base class representing a Payment method.
 */
public abstract class Payment implements Serializable {
    private static final long serialVersionUID = 1L;

    private String paymentId;
    private String payerName;
    private double amount;
    private String currency; // "USD"

    public Payment() {}

    public Payment(String paymentId, String payerName, double amount, String currency) {
        this.paymentId = paymentId;
        this.payerName = payerName;
        this.amount = amount;
        this.currency = currency != null ? currency : "USD";
    }

    // =========================================================================
    // ABSTRACT METHODS (ABSTRACTION & POLYMORPHISM)
    // =========================================================================

    public abstract boolean processPayment(double amountToCharge);
    public abstract double calculateTransactionFee(double amountToCharge);
    public abstract String getPaymentProviderName();
    public abstract String getMaskedAccountDetails();
    public abstract String getReceiptFooter();

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getPayerName() { return payerName; }
    public void setPayerName(String payerName) { this.payerName = payerName; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    @Override
    public String toString() {
        return String.format("[%s] Payer: %s | Account: %s",
                getPaymentProviderName(), payerName, getMaskedAccountDetails());
    }
}