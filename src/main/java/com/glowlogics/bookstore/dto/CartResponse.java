package com.glowlogics.bookstore.dto;

import java.util.ArrayList;
import java.util.List;

public class CartResponse {
    private List<CartItemView> items = new ArrayList<>();
    private int totalItems;
    private double totalPrice;

    public List<CartItemView> getItems() {
        return items;
    }

    public void setItems(List<CartItemView> items) {
        this.items = items;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
