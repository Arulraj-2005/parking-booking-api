package com.parkingapp.controller;

import com.parkingapp.model.Payment;
import com.parkingapp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // Step 1: Create a Stripe PaymentIntent → returns clientSecret for frontend
    @PostMapping("/checkout/{bookingId}")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.createPaymentIntent(bookingId));
    }

    // Step 2: Called after Stripe confirms payment on frontend
    @PostMapping("/confirm")
    public ResponseEntity<Payment> confirmPayment(@RequestParam String paymentIntentId) {
        return ResponseEntity.ok(paymentService.confirmPayment(paymentIntentId));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<Payment> getPaymentByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getPaymentByBookingId(bookingId));
    }
}
