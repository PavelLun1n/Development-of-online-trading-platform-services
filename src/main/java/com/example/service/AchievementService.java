package com.example.service;

import com.example.model.Achievement;
import com.example.model.User;
import com.example.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;

    public List<Achievement> getAchievementsByUser(User user) {
        return achievementRepository.findAll().stream()
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .toList();
    }

    public Achievement saveAchievement(Achievement achievement) {
        return achievementRepository.save(achievement);
    }
}
