package com.cinereserve.payment.model;

/**
 * Direct Bank Debit Card payment (0% transaction fee).
 */
public class DebitCardPayment extends Payment {
    private static final long serialVersionUID = 1L;

    private String bankAccountNumber;
    private String bankName;

    public DebitCardPayment(String paymentId, String payerName, double amount,
                            String bankAccountNumber, String bankName) {
        super(paymentId, payerName, amount, "USD");
        this.bankAccountNumber = bankAccountNumber;
        this.bankName = bankName != null ? bankName : "CHASE BANK";
    }

    @Override
    public boolean processPayment(double amountToCharge) {
        return bankAccountNumber != null && bankAccountNumber.length() >= 8 && amountToCharge > 0.0;
    }

    @Override
    public double calculateTransactionFee(double amountToCharge) {
        return 0.00; // Free direct debit
    }

    @Override
    public String getPaymentProviderName() {
        return "DEBIT_CARD (" + bankName + ")";
    }

    @Override
    public String getMaskedAccountDetails() {
        if (bankAccountNumber.length() >= 4) {
            return "DEBIT-****" + bankAccountNumber.substring(bankAccountNumber.length() - 4);
        }
        return "DEBIT-****";
    }

    @Override
    public String getReceiptFooter() {
        return "Direct Bank ACH Transfer. Funds immediately debited.";
    }
}