package com.ralchik.demo.controller;

import com.ralchik.demo.domain.Role;
import com.ralchik.demo.domain.User;
import com.ralchik.demo.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final UserRepository userRepository;

    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String registration() {
        return "registration";
    }

    @PostMapping
    public String addUser(User user, Model model) {
        if (userRepository.existsByUsername(user.getUsername())) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.ADMIN));
        userRepository.save(user);

        return "redirect:/login";
    }
}
