package com.glowlogics.bookstore.model;

import java.time.LocalDateTime;

public class Review {
    private String username;
    private String comment;
    private int rating;
    private LocalDateTime createdAt;

    public Review() {
    }

    public Review(String username, String comment, int rating, LocalDateTime createdAt) {
        this.username = username;
        this.comment = comment;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
