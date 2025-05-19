package com.example.controller;

import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributeAdvice {
    private final CartService cartService;

    @Autowired
    public GlobalModelAttributeAdvice(CartService cartService) {
        this.cartService = cartService;
    }

    @ModelAttribute("cartSize")
    public int cartSize() {
        return cartService.getCartItems()
                .values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}
