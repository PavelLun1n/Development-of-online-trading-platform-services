package com.example.service;

import com.example.entity.enums.EventType;
import com.example.model.UserAction;
import com.example.repository.UserActionRepository;
import jakarta.transaction.Transactional;
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
    @Transactional
    public UserAction saveAction(UserAction action) {
        return userActionRepository.save(action);
    }


    public List<UserAction> getActionsByUserId(Long userId) {
        return userActionRepository.findAll()
                .stream()
                .filter(action -> userId.equals(action.getUserId()))
                .toList();
    }
}
