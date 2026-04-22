package com.glowlogics.bookstore.service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final Map<String, Map<Long, Integer>> cartByUser = new ConcurrentHashMap<>();

    public void addToCart(String username, Long bookId, int quantity) {
        Map<Long, Integer> userCart = cartByUser.computeIfAbsent(username, key -> new LinkedHashMap<>());
        userCart.compute(bookId, (key, existingQuantity) -> {
            int current = existingQuantity == null ? 0 : existingQuantity;
            return current + quantity;
        });
    }

    public Map<Long, Integer> getCart(String username) {
        Map<Long, Integer> userCart = cartByUser.get(username);
        if (userCart == null) {
            return Collections.emptyMap();
        }
        return new LinkedHashMap<>(userCart);
    }

    public void removeFromCart(String username, Long bookId) {
        Map<Long, Integer> userCart = cartByUser.get(username);
        if (userCart != null) {
            userCart.remove(bookId);
        }
    }

    public void clearCart(String username) {
        cartByUser.remove(username);
    }
}
