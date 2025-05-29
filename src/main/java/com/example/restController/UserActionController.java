package com.example.restController;

import com.example.dto.UserActionDto;
import com.example.model.UserAction;
import com.example.service.UserActionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/user-actions")
public class UserActionController {

    private final UserActionService userActionService;

    @Autowired
    public UserActionController(UserActionService userActionService) {
        this.userActionService = userActionService;
    }

    @PostMapping
    public void logUserAction(@RequestBody UserActionDto dto, HttpServletRequest request) {
        String userSession = dto.getUserSession();
        UserAction action = UserAction.builder()
                .eventTime(LocalDateTime.now())
                .eventType(dto.getEventType())
                .productId(dto.getProductId())
                .userId(dto.getUserId())
                .userSession(userSession)
                .build();
        userActionService.saveAction(action);
    }
}
