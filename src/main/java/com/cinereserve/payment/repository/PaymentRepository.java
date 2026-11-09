package com.cinereserve.payment.repository;

import com.cinereserve.payment.model.PaymentConfirmation;
import com.cinereserve.payment.util.StringUtil;

import java.util.*;

public class PaymentRepository implements Repository<PaymentConfirmation, String> {

    private static final String FILE_NAME = "payments.txt";
    private final FileStorageService storage = FileStorageService.getInstance();

    public PaymentRepository() {
        storage.ensureFileExists(FILE_NAME);
    }

    @Override
    public Optional<PaymentConfirmation> findById(String id) {
        if (id == null) return Optional.empty();
        return findAll().stream().filter(p -> id.equalsIgnoreCase(p.getConfirmationId())).findFirst();
    }

    public Optional<PaymentConfirmation> findByBookingRef(String bookingRef) {
        if (bookingRef == null) return Optional.empty();
        return findAll().stream().filter(p -> bookingRef.equalsIgnoreCase(p.getBookingRef())).findFirst();
    }

    @Override
    public List<PaymentConfirmation> findAll() {
        List<String> lines = storage.readAllLines(FILE_NAME);
        List<PaymentConfirmation> list = new ArrayList<>();
        for (String line : lines) {
            PaymentConfirmation p = parseLine(line);
            if (p != null) list.add(p);
        }
        return list;
    }

    @Override
    public boolean save(PaymentConfirmation p) {
        if (p == null || existsById(p.getConfirmationId())) return false;
        return storage.appendLine(FILE_NAME, formatLine(p));
    }

    @Override
    public boolean update(PaymentConfirmation p) {
        if (p == null) return false;
        List<PaymentConfirmation> all = findAll();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getConfirmationId().equalsIgnoreCase(p.getConfirmationId())) {
                all.set(i, p);
                found = true;
                break;
            }
        }
        if (!found) return false;
        List<String> lines = new ArrayList<>();
        for (PaymentConfirmation item : all) lines.add(formatLine(item));
        return storage.writeAllLines(FILE_NAME, lines);
    }

    @Override
    public boolean deleteById(String id) {
        List<PaymentConfirmation> all = findAll();
        boolean removed = all.removeIf(p -> p.getConfirmationId().equalsIgnoreCase(id));
        if (!removed) return false;
        List<String> lines = new ArrayList<>();
        for (PaymentConfirmation item : all) lines.add(formatLine(item));
        return storage.writeAllLines(FILE_NAME, lines);
    }

    @Override
    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    private PaymentConfirmation parseLine(String line) {
        try {
            String[] p = StringUtil.splitRecord(line);
            if (p.length < 11) return null;
            return new PaymentConfirmation(
                    p[0], p[1], p[2], p[3],
                    Double.parseDouble(p[4]),
                    Double.parseDouble(p[5]),
                    Double.parseDouble(p[6]),
                    p[7], p[8], p[9], p[10]
            );
        } catch (Exception e) {
            return null;
        }
    }

    private String formatLine(PaymentConfirmation p) {
        return StringUtil.joinRecord(
                p.getConfirmationId(),
                p.getTransactionRef(),
                p.getBookingRef(),
                p.getPayerName(),
                String.format(Locale.US, "%.2f", p.getAmountPaid()),
                String.format(Locale.US, "%.2f", p.getFeeAmount()),
                String.format(Locale.US, "%.2f", p.getFinalCharged()),
                p.getPaymentMethod(),
                p.getStatus(),
                p.getTimestamp(),
                p.getReceiptNumber()
        );
    }
}