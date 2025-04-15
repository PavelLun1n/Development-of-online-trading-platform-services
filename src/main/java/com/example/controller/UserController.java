package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userService.authenticate(user);
    }

    @GetMapping("/{id}")
    public User getUserProfile(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public User updateProfile(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    @GetMapping("/{id}/orders")
    public List<?> getUserOrders(@PathVariable Long id) {
        return userService.getOrdersByUserId(id);
    }

    @GetMapping("/{id}/bids")
    public List<?> getUserBids(@PathVariable Long id) {
        return userService.getBidsByUserId(id);
    }
}
