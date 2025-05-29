package com.example;

import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.entity.enums.OrderStatus;
import com.example.entity.enums.Role;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CsvLoader {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private final Map<Long, User> userCache = new HashMap<>();
    private final Map<Long, Product> productCache = new HashMap<>();

    @PostConstruct
    public void importAll() {
        //importUsers();
        //importProducts();
        //importOrders();
    }

    public void importUsers() {
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("csv/users.csv")))) {
            reader.readNext(); // skip header
            String[] line;
            while ((line = reader.readNext()) != null) {
                String username = line[1];
                String email = line[2];
                String password = line[3];
                Role role = Role.valueOf(line[4].toUpperCase());
                float rating = Float.parseFloat(line[6]);
                BigDecimal balance = new BigDecimal(line[7]);

                User user = User.builder()
                        .username(username)
                        .email(email)
                        .password(password)
                        .role(role)
                        .rating(rating)
                        .balance(balance)
                        .build();

                User saved = userRepository.save(user);
                userCache.put(saved.getId(), saved);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error importing users", e);
        }
    }

    public void importProducts() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("csv/products_for_db.csv")))) {
            reader.readNext(); // пропускаем заголовок
            String[] line;
            while ((line = reader.readNext()) != null) {
                String name = line[1];
                BigDecimal price = new BigDecimal(line[2].replace(" ", "").replace("₽", "").replace(" ", "").trim().replace(",", "."));
                int stock = Integer.parseInt(line[3]);
                String imageUrl = line[4];
                LocalDateTime createdAt = LocalDateTime.parse(line[5], formatter);
                Long categoryId = Long.parseLong(line[6]);
                String categoryCode = line[7];

                String description = line[8];
                if (description == null || description.isBlank()) {
                    description = "";
                }

                User seller = userRepository.findAll().stream().findFirst().orElseThrow();

                Product product = Product.builder()
                        .name(name)
                        .price(price)
                        .stock(stock)
                        .imageUrl(imageUrl)
                        .createdAt(createdAt)
                        .categoryId(categoryId)
                        .categoryCode(categoryCode)
                        .description(description)
                        .seller(seller)
                        .build();

                Product saved = productRepository.save(product);
                productCache.put(saved.getId(), saved);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error importing products", e);
        }
    }


    public void importOrders() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("csv/orders.csv")))) {
            reader.readNext(); // skip header
            String[] line;
            while ((line = reader.readNext()) != null) {
                Long buyerOldId = Long.parseLong(line[1]);
                Long sellerOldId = Long.parseLong(line[2]);
                Long productOldId = Long.parseLong(line[3]);
                int quantity = Integer.parseInt(line[4]);
                BigDecimal totalPrice = new BigDecimal(line[5]);
                OrderStatus status = OrderStatus.valueOf(line[6].toUpperCase());
                LocalDateTime orderDate = LocalDateTime.parse(line[7].substring(0, 19), formatter);

                User buyer = userCache.get(buyerOldId);
                User seller = userCache.get(sellerOldId);
                Product product = productCache.get(productOldId);

                Order order = Order.builder()
                        .buyer(buyer)
                        .seller(seller)
                        .product(product)
                        .quantity(quantity)
                        .totalPrice(totalPrice)
                        .status(status)
                        .orderDate(orderDate)
                        .build();

                orderRepository.save(order);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error importing orders", e);
        }
    }

}
