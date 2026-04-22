package com.glowlogics.bookstore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.glowlogics.bookstore.dto.CartResponse;
import com.glowlogics.bookstore.dto.CheckoutResponse;
import com.glowlogics.bookstore.service.AuthService;
import com.glowlogics.bookstore.service.CartService;
import com.glowlogics.bookstore.service.CheckoutService;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CartService cartService;
    private final AuthService authService;
    private final CartController cartController;
    private final CheckoutService checkoutService;

    public CheckoutController(CartService cartService,
                              AuthService authService,
                              CartController cartController,
                              CheckoutService checkoutService) {
        this.cartService = cartService;
        this.authService = authService;
        this.cartController = cartController;
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public CheckoutResponse checkout(@RequestHeader(name = "X-Auth-Token", required = false) String token) {
        String username = authService.getUsernameByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please login first"));

        CartResponse cartResponse = cartController.getCart(token);
        if (cartResponse.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }

        CheckoutResponse response = checkoutService.placeOrder(
                cartResponse.getTotalItems(),
                cartResponse.getTotalPrice()
        );

        cartService.clearCart(username);
        return response;
    }
}
