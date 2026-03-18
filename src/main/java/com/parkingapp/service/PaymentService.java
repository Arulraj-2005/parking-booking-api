package com.parkingapp.service;

import com.parkingapp.model.Booking;
import com.parkingapp.model.Payment;
import com.parkingapp.model.PaymentStatus;
import com.parkingapp.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingService bookingService;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Transactional
    public Map<String, String> createPaymentIntent(Long bookingId) {
        Booking booking = bookingService.getById(bookingId);

        try {
            long amountInCents = booking.getTotalPrice()
                    .multiply(java.math.BigDecimal.valueOf(100))
                    .longValue();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("usd")
                    .putMetadata("bookingId", bookingId.toString())
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            Payment payment = Payment.builder()
                    .booking(booking)
                    .amount(booking.getTotalPrice())
                    .stripePaymentIntentId(intent.getId())
                    .stripeClientSecret(intent.getClientSecret())
                    .status(PaymentStatus.PENDING)
                    .build();

            paymentRepository.save(payment);

            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", intent.getClientSecret());
            response.put("paymentIntentId", intent.getId());
            return response;

        } catch (StripeException e) {
            throw new RuntimeException("Failed to create payment intent: " + e.getMessage());
        }
    }

    @Transactional
    public Payment confirmPayment(String paymentIntentId) {
        Payment payment = paymentRepository.findByStripePaymentIntentId(paymentIntentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.SUCCEEDED);
        paymentRepository.save(payment);

        bookingService.confirmBooking(payment.getBooking().getId());
        return payment;
    }

    public Payment getPaymentByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for booking: " + bookingId));
    }
}
