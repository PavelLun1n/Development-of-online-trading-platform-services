package com.example.controller;

import com.example.entity.enums.EventType;
import com.example.model.Product;
import com.example.model.User;
import com.example.model.UserAction;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.service.UserActionService; // ← добавьте этот импорт
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class MainController {

    private final ProductService productService;
    private final CartService cartService;
    private final UserActionService userActionService; // ← добавили поле

    @Autowired
    public MainController(ProductService productService,
                          CartService cartService,
                          UserActionService userActionService) { // ← внедряем через конструктор
        this.productService = productService;
        this.cartService = cartService;
        this.userActionService = userActionService;
    }

    @GetMapping("/")
    public String showMainPage(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "category", required = false) String category,
            Model model
    ) {
        List<Product> products;

        if (query != null && !query.isBlank()) {
            products = productService.searchByName(query);
            model.addAttribute("query", query);
        } else if (category != null && !category.isEmpty()) {
            products = productService.getProductsByCategory(category);
            model.addAttribute("selectedCategory", category);
        } else {
            products = productService.getAllProducts();
        }

        List<String> categories = productService.getAllCategoryCodes();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);

        return "index";
    }


    @GetMapping("/products/{id}")
    public String showProductPage(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);

                    if (user != null) { // на всякий случай, если пользователь не авторизован
                        UserAction action = UserAction.builder()
                                .eventTime(LocalDateTime.now())
                                .eventType(EventType.view)
                                .productId(product.getId())
                                .userId(user.getId())
                                .userSession(session.getId())
                                .build();
                        userActionService.saveAction(action);
                    }

                    return "product";
                })
                .orElse("error/404");
    }
    @GetMapping("/home")
    public String viewHomePage(@RequestParam(value = "category", required = false) String category, Model model) {
        List<Product> products;
        if (category != null && !category.isEmpty()) {
            products = productService.getProductsByCategory(category);
        } else {
            products = productService.getAllProducts();
        }

        // Получить уникальные категории для вкладок
        List<String> categories = productService.getAllCategoryCodes();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", category);

        return "index";
    }
}
