package com.example.dto;

import com.example.entity.enums.EventType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class UserActionDto implements Serializable {
    private EventType eventType;
    private Long productId;
    private Long userId;
    private String userSession;
}
