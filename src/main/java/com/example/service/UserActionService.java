package com.example.service;

import com.example.entity.enums.EventType;
import com.example.model.UserAction;
import com.example.repository.UserActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserActionService {

    private final UserActionRepository userActionRepository;

    @Autowired
    public UserActionService(UserActionRepository userActionRepository) {
        this.userActionRepository = userActionRepository;
    }

    public List<UserAction> getAllActions() {
        return userActionRepository.findAll();
    }

    public UserAction saveAction(UserAction action) {
        return userActionRepository.save(action);
    }

    public List<UserAction> getActionsByUserId(Long userId) {
        return userActionRepository.findAll()
                .stream()
                .filter(action -> userId.equals(action.getUserId()))
                .toList();
    }
    public void importUserActionsFromCSV(InputStream csvInputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvInputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 7) continue; // Проверяем, чтобы в строке было достаточно данных

                UserAction action = new UserAction();
                action.setEventTime(LocalDateTime.parse(parts[0].trim())); // Время события
                action.setEventType(EventType.valueOf(parts[1].trim().toUpperCase())); // Тип события
                action.setProductId(Long.parseLong(parts[2].trim())); // ID продукта
                action.setCategoryId(Long.parseLong(parts[3].trim())); // ID категории
                action.setCategoryCode(parts[4].trim()); // Код категории
                action.setBrand(parts[5].trim()); // Бренд
                action.setPrice(new BigDecimal(parts[6].trim())); // Цена
                action.setUserId(Long.parseLong(parts[7].trim())); // ID пользователя
                action.setUserSession(parts[8].trim()); // Сессия пользователя

                userActionRepository.save(action);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при импорте действий пользователя: " + e.getMessage(), e);
        }
    }


}
