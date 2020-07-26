package com.ralchik.demo.controller;

import com.ralchik.demo.domain.Role;
import com.ralchik.demo.domain.User;
import com.ralchik.demo.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "userlist";
    }

    @GetMapping("{user}")
    public String userEditForm(User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "useredit";
    }

    @PostMapping
    public String userSave(User newUser, @RequestParam("id") User oldUser) {
        oldUser.setUsername(newUser.getUsername());
        oldUser.setRoles(newUser.getRoles());
        userRepository.save(oldUser);
        return "redirect:/user";
    }

}
