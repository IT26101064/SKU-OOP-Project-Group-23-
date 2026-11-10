package com.cinereserve.payment.model;

import java.io.Serializable;

public class PaymentConfirmation implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_REFUNDED = "REFUNDED";
    public static final String STATUS_FAILED = "FAILED";

    private String confirmationId;
    private String transactionRef; // e.g. "TXN-20261011-9988"
    private String bookingRef;     // e.g. "CR-20261011-6349"
    private String payerName;
    private double amountPaid;
    private double feeAmount;
    private double finalCharged;
    private String paymentMethod;
    private String status;         // SUCCESS, REFUNDED, FAILED
    private String timestamp;
    private String receiptNumber;

    public PaymentConfirmation() {}

    public PaymentConfirmation(String confirmationId, String transactionRef, String bookingRef,
                               String payerName, double amountPaid, double feeAmount,
                               double finalCharged, String paymentMethod, String status,
                               String timestamp, String receiptNumber) {
        this.confirmationId = confirmationId;
        this.transactionRef = transactionRef;
        this.bookingRef = bookingRef;
        this.payerName = payerName;
        this.amountPaid = amountPaid;
        this.feeAmount = feeAmount;
        this.finalCharged = finalCharged;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.timestamp = timestamp;
        this.receiptNumber = receiptNumber;
    }

    public boolean isSuccessful() { return STATUS_SUCCESS.equalsIgnoreCase(status); }
    public boolean isRefunded() { return STATUS_REFUNDED.equalsIgnoreCase(status); }

    public String getConfirmationId() { return confirmationId; }
    public void setConfirmationId(String confirmationId) { this.confirmationId = confirmationId; }

    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }

    public String getBookingRef() { return bookingRef; }
    public void setBookingRef(String bookingRef) { this.bookingRef = bookingRef; }

    public String getPayerName() { return payerName; }
    public void setPayerName(String payerName) { this.payerName = payerName; }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public double getFeeAmount() { return feeAmount; }
    public void setFeeAmount(double feeAmount) { this.feeAmount = feeAmount; }

    public double getFinalCharged() { return finalCharged; }
    public void setFinalCharged(double finalCharged) { this.finalCharged = finalCharged; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }

    @Override
    public String toString() {
        return String.format("[%s] TXN: %s | Booking: %s | Charged: $%.2f | Method: %s",
                status, transactionRef, bookingRef, finalCharged, paymentMethod);
    }
}