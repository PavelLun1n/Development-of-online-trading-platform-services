package com.example.controller;

import com.example.entity.enums.EventType;
import com.example.entity.enums.OrderStatus;
import com.example.form.CheckoutForm;
import com.example.model.Product;
import com.example.model.User;
import com.example.model.UserAction;
import com.example.service.CartService;
import com.example.service.OrderService;
import com.example.service.ProductService;
import com.example.service.UserActionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private final OrderService orderService;
    private final UserActionService userActionService; // Добавлено

    @Autowired
    public CartController(CartService cartService,
                          ProductService productService,
                          OrderService orderService,
                          UserActionService userActionService) {
        this.cartService = cartService;
        this.productService = productService;
        this.orderService = orderService;
        this.userActionService = userActionService; // Добавлено
    }

    @GetMapping
    public String showCart(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        model.addAttribute("items", cartService.getCartItems());
        model.addAttribute("total", cartService.getTotalPrice());
        return "cart";
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        productService.getProductById(id).ifPresent(product -> {
            cartService.addProduct(product);

            // Логирование действия
            UserAction action = UserAction.builder()
                    .eventTime(LocalDateTime.now())
                    .eventType(EventType.cart)
                    .productId(product.getId())
                    .userId(user.getId())
                    .userSession(session.getId())
                    .build();
            userActionService.saveAction(action);
        });

        return "redirect:/cart";
    }

    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        productService.getProductById(id).ifPresent(product -> {
            cartService.removeProduct(product);

            // Логирование действия
            UserAction action = UserAction.builder()
                    .eventTime(LocalDateTime.now())
                    .eventType(EventType.remove_from_cart)
                    .productId(product.getId())
                    .userId(user.getId())
                    .userSession(session.getId())
                    .build();
            userActionService.saveAction(action);
        });

        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        cartService.clearCart();
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String showCheckoutForm(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        model.addAttribute("checkoutForm", new CheckoutForm());
        return "checkout";
    }
}
