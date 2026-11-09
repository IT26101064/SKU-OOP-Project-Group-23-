package com.cinereserve.payment.model;

/**
 * Digital Wallet (Apple Pay / Google Pay / PayPal) with tokenized authentication.
 */
public class DigitalWalletPayment extends Payment {
    private static final long serialVersionUID = 1L;

    private String walletProvider; // "APPLE_PAY", "GOOGLE_PAY", "PAYPAL"
    private String tokenizedId;

    public DigitalWalletPayment(String paymentId, String payerName, double amount,
                                String walletProvider, String tokenizedId) {
        super(paymentId, payerName, amount, "USD");
        this.walletProvider = walletProvider != null ? walletProvider : "APPLE_PAY";
        this.tokenizedId = tokenizedId;
    }

    @Override
    public boolean processPayment(double amountToCharge) {
        return tokenizedId != null && !tokenizedId.trim().isEmpty() && amountToCharge > 0.0;
    }

    @Override
    public double calculateTransactionFee(double amountToCharge) {
        // 1.0% digital wallet fee
        return amountToCharge * 0.01;
    }

    @Override
    public String getPaymentProviderName() {
        return "DIGITAL_WALLET (" + walletProvider + ")";
    }

    @Override
    public String getMaskedAccountDetails() {
        return walletProvider + ": " + getPayerName();
    }

    @Override
    public String getReceiptFooter() {
        return "Biometrically authenticated transaction via " + walletProvider + " Device Account.";
    }
}