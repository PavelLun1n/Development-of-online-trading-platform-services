package com.example.service;

import com.example.model.User;
import com.example.model.Order;
import com.example.model.Bid;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;

    // заглушки для демонстрации
    private final Map<Long, List<Order>> orderHistory = new HashMap<>();
    private final Map<Long, List<Bid>> bidHistory = new HashMap<>();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User register(User user) {
        return userRepository.save(user);
    }

    public String authenticate(User user) {
        Optional<User> foundUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        return foundUser.isPresent() ? "Login successful" : "Invalid credentials";
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


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
