package com.example.restController;

import com.example.model.SellerRating;
import com.example.repository.SellerRatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class SellerRatingController {

    private final SellerRatingRepository repository;

    @GetMapping
    public List<SellerRating> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public SellerRating getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    @PostMapping
    public SellerRating create(@RequestBody SellerRating rating) {
        return repository.save(rating);
    }

    @PutMapping("/{id}")
    public SellerRating update(@PathVariable Long id, @RequestBody SellerRating updated) {
        updated.setId(id);
        return repository.save(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
