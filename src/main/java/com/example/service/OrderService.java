package com.example.service;

import com.example.model.Product;
import com.example.model.User;
import com.example.repository.OrderRepository;
import com.example.model.Order;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, Order orderDetails) {
        return orderRepository.findById(id).map(order -> {
            order.setStatus(orderDetails.getStatus());
            return orderRepository.save(order);
        }).orElseThrow(() -> new RuntimeException("Order not found"));
    }
    public void importOrdersFromCSV(InputStream csvInputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvInputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) continue;
                Order order = new Order();
                order.setQuantity(Integer.parseInt(parts[1].trim()));
                order.setTotalPrice(new BigDecimal(parts[2].trim()));
                order.setOrderDate(LocalDateTime.parse(parts[3].trim()));
                Long buyerId = Long.parseLong(parts[4].trim());
                User buyer = userRepository.findById(buyerId).orElseThrow(() -> new RuntimeException("User with ID " + buyerId + " not found"));
                order.setBuyer(buyer);
                Long sellerId = Long.parseLong(parts[5].trim());
                User seller = userRepository.findById(sellerId).orElseThrow(() -> new RuntimeException("User with ID " + sellerId + " not found"));
                order.setSeller(seller);
                Long productId = Long.parseLong(parts[6].trim());
                Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product with ID " + productId + " not found"));
                order.setProduct(product);
                orderRepository.save(order);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при импорте заказов: " + e.getMessage(), e);
        }
    }



    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
