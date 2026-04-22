package com.glowlogics.bookstore.model;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private Long id;
    private String title;
    private String author;
    private String category;
    private String description;
    private String shortSummary;
    private String authorIntroduction;
    private double price;
    private double originalPrice;
    private String imageUrl;
    private List<Review> reviews = new ArrayList<>();

    public Book() {
    }

    public Book(Long id,
                String title,
                String author,
                String category,
                String description,
                double price,
                double originalPrice,
                String imageUrl) {
        this(id, title, author, category, description, "", "", price, originalPrice, imageUrl);
    }

    public Book(Long id,
                String title,
                String author,
                String category,
                String description,
                String shortSummary,
                String authorIntroduction,
                double price,
                double originalPrice,
                String imageUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.shortSummary = shortSummary;
        this.authorIntroduction = authorIntroduction;
        this.price = price;
        this.originalPrice = originalPrice;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortSummary() {
        return shortSummary;
    }

    public void setShortSummary(String shortSummary) {
        this.shortSummary = shortSummary;
    }

    public String getAuthorIntroduction() {
        return authorIntroduction;
    }

    public void setAuthorIntroduction(String authorIntroduction) {
        this.authorIntroduction = authorIntroduction;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
