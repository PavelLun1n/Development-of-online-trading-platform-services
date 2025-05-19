package com.example.controller;

import com.example.model.Product;
import com.example.service.CartService;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {

    private final ProductService productService;
    private CartService cartService;

    @Autowired
    public MainController(ProductService productService,CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    /**
     * Главная страница.
     * Если передан параметр ?query=… — выполняем поиск.
     */
    @GetMapping("/")
    public String showMainPage(
            @RequestParam(name = "query", required = false) String query,
            Model model
    ) {
        List<Product> products;
        if (query == null || query.isBlank()) {
            products = productService.getAllProducts();
        } else {
            products = productService.searchByName(query);
            model.addAttribute("query", query);
        }
        model.addAttribute("products", products);
        return "index";     // возвращаем имя шаблона без расширения
    }

    /**
     * Страница детали товара по ID.
     */
    @GetMapping("/products/{id}")
    public String showProductPage(@PathVariable Long id, Model model) {
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "product";
                })
                .orElse("error/404");
    }

}
