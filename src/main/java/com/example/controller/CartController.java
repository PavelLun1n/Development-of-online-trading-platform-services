package com.example.controller;

import com.example.entity.enums.OrderStatus;
import com.example.form.CheckoutForm;
import com.example.model.Product;
import com.example.model.Order;
import com.example.model.User;
import com.example.service.CartService;
import com.example.service.OrderService;
import com.example.service.ProductService;
import com.example.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private final OrderService orderService;
    private final EmailService emailService;

    @Autowired
    public CartController(CartService cartService,
                          ProductService productService,
                          OrderService orderService,
                          EmailService emailService) {
        this.cartService = cartService;
        this.productService = productService;
        this.orderService = orderService;
        this.emailService = emailService;
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
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        productService.getProductById(id).ifPresent(cartService::addProduct);
        return "redirect:/cart";
    }

    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        productService.getProductById(id).ifPresent(cartService::removeProduct);
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

    @PostMapping("/checkout")
    public String processCheckout(
            @ModelAttribute CheckoutForm checkoutForm,
            HttpSession session,
            Model model
    ) {
        User buyer = (User) session.getAttribute("user");
        if (buyer == null) {
            return "redirect:/login";
        }

        // 1. Создаём заказы из содержимого корзины
        List<Order> orders = cartService.getCartItems().entrySet().stream()
                .map(entry -> {
                    Product p = entry.getKey();
                    int qty = entry.getValue();
                    Order order = new Order();
                    order.setBuyer(buyer);
                    order.setSeller(p.getSeller());
                    order.setProduct(p);
                    order.setQuantity(qty);
                    order.setTotalPrice(p.getPrice().multiply(BigDecimal.valueOf(qty)));
                    order.setStatus(OrderStatus.PENDING);
                    return orderService.createOrder(order);
                })
                .collect(Collectors.toList());

        // 2. Очищаем корзину
        cartService.clearCart();

        // 3. Генерация QR-кода с данными заказов
        String qrContent = orders.stream()
                .map(o -> "Order#" + o.getId())
                .collect(Collectors.joining(";"));
        byte[] qrImage = emailService.generateQrCode(qrContent, 200, 200);

        // 4. Определяем, куда отправить письмо (форма или email пользователя)
        String toAddress = checkoutForm.getEmail();
        if (toAddress == null || toAddress.isBlank()) {
            toAddress = buyer.getEmail();
        }

        // 5. Отправка email с QR-кодом и деталями
        emailService.sendOrderConfirmation(toAddress, checkoutForm, orders, qrImage);

        // 6. Выводим сообщение об успехе
        model.addAttribute("message", "Ваш заказ оформлен! Письмо с подтверждением отправлено на вашу почту.");
        return "checkout-success";
    }
}
