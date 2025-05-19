package com.example.service;

import com.example.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.*;

@Service
@SessionScope
public class CartService {
    private final Map<Product, Integer> cart = new LinkedHashMap<>();

    public void addProduct(Product product) {
        cart.put(product, cart.getOrDefault(product, 0) + 1);
    }

    public void removeProduct(Product product) {
        if (cart.containsKey(product)) {
            int count = cart.get(product);
            if (count > 1) {
                cart.put(product, count - 1);
            } else {
                cart.remove(product);
            }
        }
    }

    public Map<Product, Integer> getCartItems() {
        return Collections.unmodifiableMap(cart);
    }

    public BigDecimal getTotalPrice() {
        return cart.entrySet().stream()
                .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void clearCart() {
        cart.clear();
    }
}
