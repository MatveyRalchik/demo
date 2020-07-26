package com.ralchik.demo.controller;

import com.ralchik.demo.domain.Message;
import com.ralchik.demo.domain.User;
import com.ralchik.demo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class MainController {
    @Value("${upload.path}")
    private String uploadPath;

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
            @RequestParam MultipartFile file,
            Model model
    ) throws IOException {
        if (!text.isEmpty() && !tag.isEmpty()) {
            Message message = new Message(text, tag, user);
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir();

                String resultFileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
                File uploadFile = new File(uploadDir.getAbsolutePath() + "/" + resultFileName);
                file.transferTo(uploadFile);

                message.setFilename(resultFileName);
            }
            messageRepository.save(message);
        }
        model.addAttribute("messages", messageRepository.findAll());

        return "main";
    }

}
