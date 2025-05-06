package com.example.restController;

import com.example.model.Achievement;
import com.example.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementRepository repository;

    @GetMapping
    public List<Achievement> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Achievement getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    @PostMapping
    public Achievement create(@RequestBody Achievement achievement) {
        return repository.save(achievement);
    }

    @PutMapping("/{id}")
    public Achievement update(@PathVariable Long id, @RequestBody Achievement updated) {
        Achievement achievement = repository.findById(id).orElseThrow();
        updated.setId(id);
        return repository.save(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
