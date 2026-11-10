package com.cinereserve.payment.model;

import java.util.Locale;

public class CinemaInvoice {

    public static String generatePrintableReceipt(PaymentConfirmation conf, Payment payment, String movieTitle, String seats) {
        double subtotal = conf.getAmountPaid();
        double tax = subtotal * 0.08; // 8% sales tax
        double fee = conf.getFeeAmount();
        double grandTotal = subtotal + tax + fee;

        StringBuilder sb = new StringBuilder();
        sb.append("\n============================================================\n");
        sb.append("                  CINERESERVE THEATRES                      \n");
        sb.append("               OFFICIAL TAX INVOICE & RECEIPT               \n");
        sb.append("============================================================\n");
        sb.append(String.format("Receipt Number : %s\n", conf.getReceiptNumber()));
        sb.append(String.format("Transaction Ref: %s\n", conf.getTransactionRef()));
        sb.append(String.format("Booking Ref    : %s\n", conf.getBookingRef()));
        sb.append(String.format("Date & Time    : %s\n", conf.getTimestamp()));
        sb.append(String.format("Customer Name  : %s\n", conf.getPayerName()));
        sb.append(String.format("Payment Method : %s\n", conf.getPaymentMethod()));
        sb.append(String.format("Account        : %s\n", payment.getMaskedAccountDetails()));
        sb.append("------------------------------------------------------------\n");
        sb.append(String.format("Movie Title    : %s\n", movieTitle));
        sb.append(String.format("Reserved Seats : %s\n", seats));
        sb.append("------------------------------------------------------------\n");
        sb.append(String.format(Locale.US, "Tickets Subtotal      : $ %8.2f\n", subtotal));
        sb.append(String.format(Locale.US, "Estimated Tax (8%%)    : $ %8.2f\n", tax));
        sb.append(String.format(Locale.US, "Processing Fee        : $ %8.2f\n", fee));
        sb.append("------------------------------------------------------------\n");
        sb.append(String.format(Locale.US, "TOTAL AMOUNT CHARGED  : $ %8.2f\n", grandTotal));
        sb.append(String.format("Payment Status        : %s\n", conf.getStatus()));
        sb.append("============================================================\n");
        sb.append("Notice: " + payment.getReceiptFooter() + "\n");
        sb.append("        Barcode: ||| | ||||| |||| | ||| || |||| | |||\n");
        sb.append("============================================================\n");
        return sb.toString();
    }
}