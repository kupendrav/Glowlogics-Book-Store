package com.glowlogics.bookstore.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.glowlogics.bookstore.dto.AddToCartRequest;
import com.glowlogics.bookstore.dto.CartItemView;
import com.glowlogics.bookstore.dto.CartResponse;
import com.glowlogics.bookstore.model.Book;
import com.glowlogics.bookstore.service.AuthService;
import com.glowlogics.bookstore.service.BookService;
import com.glowlogics.bookstore.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final BookService bookService;
    private final AuthService authService;

    public CartController(CartService cartService, BookService bookService, AuthService authService) {
        this.cartService = cartService;
        this.bookService = bookService;
        this.authService = authService;
    }

    @PostMapping
    public CartResponse addToCart(@Valid @RequestBody AddToCartRequest request,
                                  @RequestHeader(name = "X-Auth-Token", required = false) String token) {
        String username = requireUsername(token);

        bookService.findBookById(request.getBookId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        cartService.addToCart(username, request.getBookId(), request.getQuantity());
        return buildCartResponse(username);
    }

    @GetMapping
    public CartResponse getCart(@RequestHeader(name = "X-Auth-Token", required = false) String token) {
        String username = requireUsername(token);
        return buildCartResponse(username);
    }

    @DeleteMapping("/{id}")
    public CartResponse removeFromCart(@PathVariable("id") Long bookId,
                                       @RequestHeader(name = "X-Auth-Token", required = false) String token) {
        String username = requireUsername(token);
        cartService.removeFromCart(username, bookId);
        return buildCartResponse(username);
    }

    private String requireUsername(String token) {
        return authService.getUsernameByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please login first"));
    }

    private CartResponse buildCartResponse(String username) {
        Map<Long, Integer> cartData = cartService.getCart(username);
        List<CartItemView> items = new ArrayList<>();
        int totalItems = 0;
        double totalPrice = 0;

        for (Map.Entry<Long, Integer> entry : cartData.entrySet()) {
            Long bookId = entry.getKey();
            int quantity = entry.getValue();

            Book book = bookService.findBookById(bookId).orElse(null);
            if (book == null) {
                continue;
            }

            double lineTotal = quantity * book.getPrice();
            totalItems += quantity;
            totalPrice += lineTotal;
            items.add(new CartItemView(book, quantity, round(lineTotal)));
        }

        CartResponse response = new CartResponse();
        response.setItems(items);
        response.setTotalItems(totalItems);
        response.setTotalPrice(round(totalPrice));
        return response;
    }

    private double round(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }
}
