package com.cinereserve.payment.model;

/**
 * Credit Card payment with 16-digit card validation, CVV, and 2.0% processing fee.
 */
public class CreditCardPayment extends Payment {
    private static final long serialVersionUID = 1L;

    private String cardNumber; // 16 digits
    private String expiryDate; // "MM/YY"
    private String cvv;        // 3 digits
    private String cardNetwork; // "VISA", "MASTERCARD", "AMEX"

    public CreditCardPayment(String paymentId, String payerName, double amount,
                             String cardNumber, String expiryDate, String cvv, String cardNetwork) {
        super(paymentId, payerName, amount, "USD");
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.cardNetwork = cardNetwork != null ? cardNetwork : "VISA";
    }

    @Override
    public boolean processPayment(double amountToCharge) {
        // Enforce credit card invariants
        if (cardNumber == null || cardNumber.replaceAll("\\s+", "").length() != 16) {
            return false;
        }
        if (cvv == null || cvv.trim().length() < 3) {
            return false;
        }
        return amountToCharge > 0.0;
    }

    @Override
    public double calculateTransactionFee(double amountToCharge) {
        // 2% standard credit card processing interchange fee
        return amountToCharge * 0.02;
    }

    @Override
    public String getPaymentProviderName() {
        return "CREDIT_CARD (" + cardNetwork + ")";
    }

    @Override
    public String getMaskedAccountDetails() {
        String clean = cardNumber.replaceAll("\\s+", "");
        if (clean.length() >= 4) {
            return "**** **** **** " + clean.substring(clean.length() - 4);
        }
        return "**** **** **** ****";
    }

    @Override
    public String getReceiptFooter() {
        return "Verified by " + cardNetwork + " SecureCode. Zero Liability Guarantee.";
    }
}