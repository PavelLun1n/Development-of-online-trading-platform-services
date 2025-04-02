package com.example.service;

import com.example.repository.SellerRatingRepository;
import com.example.model.SellerRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SellerRatingService {
    private final SellerRatingRepository sellerRatingRepository;

    @Autowired
    public SellerRatingService(SellerRatingRepository sellerRatingRepository) {
        this.sellerRatingRepository = sellerRatingRepository;
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

    public void deleteSellerRating(Long id) {
        sellerRatingRepository.deleteById(id);
    }
}
