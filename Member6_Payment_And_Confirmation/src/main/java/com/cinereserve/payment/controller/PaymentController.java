package com.cinereserve.payment.controller;

import com.cinereserve.payment.model.*;
import com.cinereserve.payment.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * MEMBER 6 - PAYMENT & CONFIRMATION CONTROLLER (SE1020 OOP PROJECT)
 * ============================================================================
 * Web endpoints managing:
 * 1. Multi-Gateway Checkout (Create) -> /payments/checkout
 * 2. Cinema Tax Invoice & Payment Receipt (Read) -> /payments/invoice/{bookingRef}
 * 3. Payment Audit Ledger & Refund Engine (Read/Update) -> /payments/history
 */
@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService = new PaymentService();

    // UI 3: Payment Transaction Ledger & Audit Trail (Read)
    @GetMapping("/history")
    public String viewPaymentHistory(Model model) {
        List<PaymentConfirmation> payments = paymentService.getAllPayments();
        double totalSettled = payments.stream()
                .filter(PaymentConfirmation::isSuccessful)
                .mapToDouble(PaymentConfirmation::getFinalCharged)
                .sum();

        model.addAttribute("payments", payments);
        model.addAttribute("totalSettled", String.format("$%.2f", totalSettled));
        return "payment/history";
    }

    // UI 1: Multi-Gateway Checkout Page (Create Form)
    @GetMapping("/checkout")
    public String showCheckoutPage(@RequestParam(value = "bookingRef", defaultValue = "CR-DEMO-001") String bookingRef,
                                   @RequestParam(value = "amount", defaultValue = "25.00") double amount,
                                   Model model) {
        model.addAttribute("bookingRef", bookingRef);
        model.addAttribute("amount", amount);
        return "payment/checkout";
    }

    // Process Payment Transaction
    @PostMapping("/checkout")
    public String processPayment(@RequestParam("bookingRef") String bookingRef,
                                 @RequestParam("amount") double amount,
                                 @RequestParam("paymentType") String paymentType,
                                 @RequestParam("payerName") String payerName,
                                 @RequestParam(value = "cardNumber", required = false) String cardNumber,
                                 @RequestParam(value = "walletProvider", required = false) String walletProvider,
                                 RedirectAttributes redirectAttributes) {
        try {
            Payment paymentMethod;
            String pId = "PMT-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            if ("CREDIT_CARD".equalsIgnoreCase(paymentType)) {
                String cleanCard = (cardNumber != null && cardNumber.replaceAll("\\s+", "").length() == 16) 
                        ? cardNumber.replaceAll("\\s+", "") : "4111222233334444";
                paymentMethod = new CreditCardPayment(pId, payerName, amount, cleanCard, "12/28", "123", "VISA");
            } else if ("DEBIT_CARD".equalsIgnoreCase(paymentType)) {
                String cleanAcc = (cardNumber != null && cardNumber.trim().length() >= 8) 
                        ? cardNumber.trim() : "1234567890";
                paymentMethod = new DebitCardPayment(pId, payerName, amount, cleanAcc, "BANK-OF-CEYLON");
            } else {
                String prov = (walletProvider != null && !walletProvider.isEmpty()) ? walletProvider : "APPLE_PAY";
                paymentMethod = new DigitalWalletPayment(pId, payerName, amount, prov, "TOK-" + java.util.UUID.randomUUID().toString().substring(0, 8));
            }

            PaymentConfirmation conf = paymentService.processPayment(paymentMethod, bookingRef, amount);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Payment of $" + String.format("%.2f", conf.getFinalCharged()) + " authorized successfully! Receipt No: " + conf.getReceiptNumber());
            return "redirect:/payments/invoice/" + bookingRef;
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/payments/checkout?bookingRef=" + bookingRef + "&amount=" + amount;
        }
    }

    // UI 2: Official Cinema Tax Invoice & Payment Receipt (Read)
    @GetMapping("/invoice/{bookingRef}")
    public String viewInvoice(@PathVariable("bookingRef") String bookingRef, Model model, RedirectAttributes redirectAttributes) {
        Optional<PaymentConfirmation> confOpt = paymentService.getConfirmationByBookingRef(bookingRef);
        if (confOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "No payment confirmation found for reference: " + bookingRef);
            return "redirect:/payments/history";
        }
        model.addAttribute("confirmation", confOpt.get());
        return "payment/invoice";
    }

    // Issue Refund (Update)
    @PostMapping("/refund/{bookingRef}")
    public String refundPayment(@PathVariable("bookingRef") String bookingRef, RedirectAttributes redirectAttributes) {
        boolean refunded = paymentService.issueRefund(bookingRef);
        if (refunded) {
            redirectAttributes.addFlashAttribute("successMessage", "Payment for booking " + bookingRef + " was successfully REFUNDED.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Refund failed or transaction already refunded.");
        }
        return "redirect:/payments/history";
    }
}
