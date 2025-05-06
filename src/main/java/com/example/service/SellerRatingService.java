package com.example.service;

import com.example.model.User;
import com.example.repository.SellerRatingRepository;
import com.example.model.SellerRating;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class SellerRatingService {
    private final SellerRatingRepository sellerRatingRepository;
    private final UserRepository userRepository;

    @Autowired
    public SellerRatingService(SellerRatingRepository sellerRatingRepository, UserRepository userRepository) {
        this.sellerRatingRepository = sellerRatingRepository;
        this.userRepository = userRepository;
    }

    public List<SellerRating> getAllSellerRatings() {
        return sellerRatingRepository.findAll();
    }

    public Optional<SellerRating> getSellerRatingById(Long id) {
        return sellerRatingRepository.findById(id);
    }

    public SellerRating createSellerRating(SellerRating sellerRating) {
        return sellerRatingRepository.save(sellerRating);
    }

    public SellerRating updateSellerRating(Long id, SellerRating sellerRatingDetails) {
        return sellerRatingRepository.findById(id).map(sellerRating -> {
            sellerRating.setRating(sellerRatingDetails.getRating());
            sellerRating.setReview(sellerRatingDetails.getReview());
            return sellerRatingRepository.save(sellerRating);
        }).orElseThrow(() -> new RuntimeException("Seller rating not found"));
    }
    public void importSellerRatingsFromCSV(InputStream csvInputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvInputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue; // Проверяем, чтобы в строке было достаточное количество данных

                SellerRating rating = new SellerRating();
                rating.setRating(Float.parseFloat(parts[1].trim())); // Оценка
                rating.setReview(parts[2].trim()); // Отзыв

                // Получаем продавца по его ID
                Long sellerId = Long.parseLong(parts[3].trim());
                User seller = userRepository.findById(sellerId)
                        .orElseThrow(() -> new RuntimeException("Продавец с ID " + sellerId + " не найден"));

                // Получаем покупателя по его ID
                Long buyerId = Long.parseLong(parts[4].trim());
                User buyer = userRepository.findById(buyerId)
                        .orElseThrow(() -> new RuntimeException("Покупатель с ID " + buyerId + " не найден"));

                // Устанавливаем продавца и покупателя
                rating.setSeller(seller);
                rating.setBuyer(buyer);

                // Сохраняем оценку в базе данных
                sellerRatingRepository.save(rating);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при импорте оценок продавцов: " + e.getMessage(), e);
        }
    }



    public void deleteSellerRating(Long id) {
        sellerRatingRepository.deleteById(id);
    }
}
