    package com.example.model;

    import jakarta.persistence.*;
    import lombok.*;
    import java.time.LocalDateTime;

    @Entity
    @Table(name = "achievements")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Achievement {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        private String achievementName;

        private int points;

        private LocalDateTime achievedAt = LocalDateTime.now();
    }
