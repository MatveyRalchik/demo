package com.ralchik.demo.controller;

import com.ralchik.demo.domain.Message;
import com.ralchik.demo.domain.User;
import com.ralchik.demo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    private final MessageRepository messageRepository;

    public MainController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/main")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model
    ) {
        Iterable<Message> messages = filter != null && !filter.isEmpty() ?
                messageRepository.findByTag(filter) : messageRepository.findAll();

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            Model model
    ) {
        if (!text.isEmpty() && !tag.isEmpty()) {
            Message message = new Message(text, tag, user);
            messageRepository.save(message);
        }

        model.addAttribute("messages", messageRepository.findAll());

        return "main";
    }

}