package com.example.service;

import com.example.model.Product;
import com.example.model.User;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    @Autowired
    public ProductService(ProductRepository productRepository,  UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;

    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id).map(product -> {
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setStock(productDetails.getStock());
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    public void importFromCSV(InputStream csvInputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvInputStream, StandardCharsets.UTF_8))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    if (line.toLowerCase().contains("name")) {
                        continue;
                    }
                }
                String[] parts = line.split(",", -1);
                if (parts.length < 7) {
                    continue;
                }
                Product product = new Product();
                product.setName(parts[0].trim());
                product.setDescription(parts[1].trim());
                try {
                    product.setPrice(new BigDecimal(parts[2].trim()));
                } catch (NumberFormatException ex) {
                    throw new RuntimeException("Неверный формат цены: " + parts[2], ex);
                }
                try {
                    product.setStock(Integer.parseInt(parts[3].trim()));
                } catch (NumberFormatException ex) {
                    throw new RuntimeException("Неверный формат stock: " + parts[3], ex);
                }
                Long sellerId;
                try {
                    sellerId = Long.parseLong(parts[4].trim());
                } catch (NumberFormatException ex) {
                    throw new RuntimeException("Неверный формат sellerId: " + parts[4], ex);
                }
                User seller = userRepository.findById(sellerId)
                        .orElseThrow(() -> new RuntimeException(
                                "User with ID " + sellerId + " not found"));
                product.setSeller(seller);
                try {
                    product.setCategoryId(Long.parseLong(parts[5].trim()));
                } catch (NumberFormatException ex) {
                    product.setCategoryId(null);
                }
                product.setCategoryCode(parts[6].trim().isEmpty()
                        ? null
                        : parts[6].trim());
                productRepository.save(product);
            }

        } catch (IOException e) {
            throw new UncheckedIOException("Ошибка чтения CSV", e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при импорте CSV: " + e.getMessage(), e);
        }
    }

}
