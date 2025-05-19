package com.example.controller;

import com.example.entity.enums.Role;
import com.example.model.User;
import com.example.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    // Показ формы логина, опционально с ошибкой
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Неверное имя пользователя или пароль.");
        }
        return "login";
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        try {
            User authenticated = userService.authenticate(user);
            session.setAttribute("user", authenticated);
            return "redirect:/";
        } catch (RuntimeException ex) {
            return "redirect:/login?error=true";
        }
    }


    // Гостевой вход
    @PostMapping("/guest-login")
    public String guestLogin(HttpSession session) {
        User guest = new User();
        guest.setUsername("Гость");
        guest.setRole(Role.GUEST);
        session.setAttribute("user", guest);
        return "redirect:/";
    }

    // Опциональный logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
