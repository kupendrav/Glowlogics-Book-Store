package com.glowlogics.bookstore.dto;

public class CheckoutResponse {
    private String orderId;
    private String message;
    private int itemCount;
    private double totalAmount;

    public CheckoutResponse() {
    }

    public CheckoutResponse(String orderId, String message, int itemCount, double totalAmount) {
        this.orderId = orderId;
        this.message = message;
        this.itemCount = itemCount;
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
