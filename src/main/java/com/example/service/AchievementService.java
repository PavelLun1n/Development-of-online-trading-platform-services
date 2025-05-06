package com.example.service;

import com.example.model.Achievement;
import com.example.model.User;
import com.example.repository.AchievementRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserRepository userRepository;

    public List<Achievement> getAchievementsByUser(User user) {
        return achievementRepository.findAll().stream()
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .toList();
    }

    public Achievement saveAchievement(Achievement achievement) {
        return achievementRepository.save(achievement);
    }
    public void importAchievementsFromCSV(InputStream csvInputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvInputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 4) continue;

                Achievement achievement = new Achievement();
                achievement.setAchievementName(parts[1].trim());
                achievement.setPoints(Integer.parseInt(parts[2].trim()));

                Long userId = Long.parseLong(parts[3].trim());
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Пользователь с ID " + userId + " не найден"));

                achievement.setUser(user);
                achievement.setAchievedAt(LocalDateTime.now());

                achievementRepository.save(achievement);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при импорте достижений: " + e.getMessage(), e);
        }
    }
}
