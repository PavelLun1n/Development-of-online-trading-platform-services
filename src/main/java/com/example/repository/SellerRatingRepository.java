package com.example.repository;

import com.example.model.SellerRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRatingRepository extends JpaRepository<SellerRating, Long> {
}
