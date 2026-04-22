package com.glowlogics.bookstore.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.glowlogics.bookstore.dto.ReviewRequest;
import com.glowlogics.bookstore.model.Book;
import com.glowlogics.bookstore.model.Review;
import com.glowlogics.bookstore.service.AuthService;
import com.glowlogics.bookstore.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final AuthService authService;

    public BookController(BookService bookService, AuthService authService) {
        this.bookService = bookService;
        this.authService = authService;
    }

    @GetMapping
    public List<Book> getBooks(@RequestParam(name = "search", required = false) String search) {
        if (search == null) {
            return bookService.getAllBooks();
        }
        return bookService.searchBooksByTitle(search);
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.findBookById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }

    @PostMapping("/{id}/reviews")
    public Review addReview(@PathVariable Long id,
                            @Valid @RequestBody ReviewRequest request,
                            @RequestHeader(name = "X-Auth-Token", required = false) String token) {
        Optional<String> username = authService.getUsernameByToken(token);
        Review review = new Review(
                username.orElse("Guest Reader"),
                request.getComment().trim(),
                request.getRating(),
                LocalDateTime.now()
        );

        try {
            return bookService.addReview(id, review);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }
}
