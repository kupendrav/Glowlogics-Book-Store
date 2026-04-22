package com.glowlogics.bookstore.dto;

import com.glowlogics.bookstore.model.Book;

public class CartItemView {
    private Book book;
    private int quantity;
    private double lineTotal;

    public CartItemView() {
    }

    public CartItemView(Book book, int quantity, double lineTotal) {
        this.book = book;
        this.quantity = quantity;
        this.lineTotal = lineTotal;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }
}
