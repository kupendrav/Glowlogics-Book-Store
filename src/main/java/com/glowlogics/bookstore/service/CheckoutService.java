package com.glowlogics.bookstore.service;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.glowlogics.bookstore.dto.CheckoutResponse;

@Service
public class CheckoutService {

    private final AtomicLong orderCounter = new AtomicLong(1000);

    public CheckoutResponse placeOrder(int itemCount, double totalAmount) {
        String orderId = "GLW-" + orderCounter.incrementAndGet();
        String message = "Order placed successfully! Thank you for shopping with Glowlogics Book Store.";
        return new CheckoutResponse(orderId, message, itemCount, totalAmount);
    }
}
