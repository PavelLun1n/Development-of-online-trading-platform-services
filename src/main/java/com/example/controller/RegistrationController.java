package com.example.controller;

import com.example.entity.enums.Role;
import com.example.model.User;
import com.example.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    // Показ формы регистрации
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userForm", new User());
        return "register.html";
    }
    
    @PostMapping("/register")
    public String processRegistration(
            @ModelAttribute("userForm") @Valid User userForm,
            BindingResult bindingResult,
            Model model,
            @RequestParam("confirmPassword") String confirmPassword) {

        if (!userForm.getPassword().equals(confirmPassword)) {
            bindingResult.rejectValue("password", "error.userForm", "Пароли не совпадают");
        }

        if (userService.findByUsernameOptional(userForm.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "error.userForm", "Пользователь с таким именем уже существует");
        }

        if (bindingResult.hasErrors()) {
            return "register.html";
        }
        userForm.setRole(Role.BUYER);
        userForm.setRating(0f);
        userForm.setBalance(BigDecimal.ZERO);
        userService.register(userForm);

        return "redirect:/login";
    }
}
