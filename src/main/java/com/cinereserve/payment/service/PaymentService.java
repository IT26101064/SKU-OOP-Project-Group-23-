package com.cinereserve.payment.service;

import com.cinereserve.payment.model.*;
import com.cinereserve.payment.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PaymentService {

    private final PaymentRepository repository;

    public PaymentService() {
        this.repository = new PaymentRepository();
    }

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    /**
     * Executes payment through polymorphic payment method and generates confirmation.
     */
    public PaymentConfirmation processPayment(Payment paymentMethod, String bookingRef, double amount) {
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method required");
        }
        if (amount <= 0.0) {
            throw new IllegalArgumentException("Amount to charge must be positive");
        }

        // Polymorphic transaction fee calculation
        double fee = paymentMethod.calculateTransactionFee(amount);
        double finalCharged = amount + fee;

        // Polymorphic payment authorization
        boolean isSuccess = paymentMethod.processPayment(finalCharged);

        String confirmationId = "PMT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomSuffix = String.format("%04d", new Random().nextInt(10000));
        String txnRef = "TXN-" + datePrefix + "-" + randomSuffix;
        String receiptNo = "RCP-" + datePrefix + "-" + String.format("%04d", new Random().nextInt(10000));
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        PaymentConfirmation conf = new PaymentConfirmation(
                confirmationId, txnRef, bookingRef, paymentMethod.getPayerName(),
                amount, fee, finalCharged, paymentMethod.getPaymentProviderName(),
                isSuccess ? PaymentConfirmation.STATUS_SUCCESS : PaymentConfirmation.STATUS_FAILED,
                now, receiptNo
        );

        repository.save(conf);

        if (!isSuccess) {
            throw new IllegalStateException("Payment authorization failed with provider: " + paymentMethod.getPaymentProviderName());
        }

        return conf;
    }

    public boolean issueRefund(String bookingRef) {
        Optional<PaymentConfirmation> opt = repository.findByBookingRef(bookingRef);
        if (!opt.isPresent()) return false;

        PaymentConfirmation conf = opt.get();
        if (conf.isRefunded()) return false;

        conf.setStatus(PaymentConfirmation.STATUS_REFUNDED);
        return repository.update(conf);
    }

    public Optional<PaymentConfirmation> getConfirmationByBookingRef(String bookingRef) {
        return repository.findByBookingRef(bookingRef);
    }

    public List<PaymentConfirmation> getAllPayments() {
        return repository.findAll();
    }
}