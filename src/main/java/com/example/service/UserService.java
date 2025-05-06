    package com.example.service;

    import com.example.entity.enums.Role;
    import com.example.model.Bid;
    import com.example.model.Order;
    import com.example.model.User;
    import com.example.repository.UserRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.io.BufferedReader;
    import java.io.InputStream;
    import java.io.InputStreamReader;
    import java.math.BigDecimal;
    import java.nio.charset.StandardCharsets;
    import java.util.*;

    @Service
    public class UserService {

        private final UserRepository userRepository;

        // Заглушки для демонстрации
        private final Map<Long, List<Order>> orderHistory = new HashMap<>();
        private final Map<Long, List<Bid>> bidHistory = new HashMap<>();

        @Autowired
        public UserService(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        public User setUser(User user) {
            return userRepository.save(user);
        }

        public List<User> getAllUsers() {
            return userRepository.findAll();
        }

        public User getUserById(Long id) {
            return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        }

        public User register(User user) {
            // Просто сохраняем пароль как есть
            return userRepository.save(user);
        }

        public User authenticate(User user) {
            Optional<User> foundUser = userRepository.findByUsername(user.getUsername());
            if (foundUser.isEmpty()) {
                throw new RuntimeException("Пользователь не найден");
            }

            User dbUser = foundUser.get();
            if (!user.getPassword().equals(dbUser.getPassword())) {
                throw new RuntimeException("Неверный пароль");
            }
            return dbUser;
        }

        public User updateUser(Long id, User userDetails) {
            return userRepository.findById(id).map(user -> {
                user.setUsername(userDetails.getUsername());
                user.setEmail(userDetails.getEmail());
                user.setRating(userDetails.getRating());
                return userRepository.save(user);
            }).orElseThrow(() -> new RuntimeException("User not found"));
        }

        public List<Order> getOrdersByUserId(Long id) {
            return orderHistory.getOrDefault(id, new ArrayList<>());
        }

        public List<Bid> getBidsByUserId(Long id) {
            return bidHistory.getOrDefault(id, new ArrayList<>());
        }

        public User findByUsername(String username) {
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        }

        public void importUsersFromCSV(InputStream csvInputStream) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvInputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 5) continue;

                    User user = new User();
                    user.setUsername(parts[0].trim());
                    user.setPassword(parts[1].trim()); // Пароль сохраняется как есть
                    user.setEmail(parts[2].trim());
                    user.setRating(Float.parseFloat(parts[3].trim()));
                    user.setRole(Role.GUEST);
                    user.setBalance(BigDecimal.ZERO);

                    userRepository.save(user);
                }
            } catch (Exception e) {
                throw new RuntimeException("Ошибка при импорте пользователей: " + e.getMessage(), e);
            }
        }
        public Optional<User> findByUsernameOptional(String username) {
            return userRepository.findByUsername(username);
        }

        public void deleteUser(Long id) {
            userRepository.deleteById(id);
        }
    }
