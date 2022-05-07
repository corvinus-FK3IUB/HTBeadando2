package edu.corvinus.HTBeadando2.controller;

import edu.corvinus.HTBeadando2.models.User;
import edu.corvinus.HTBeadando2.repository.UserRepository;
import org.aspectj.bridge.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.swing.*;
import javax.validation.Valid;
import java.awt.*;

@Controller
public class RegisterController {

    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    private final UserRepository repository;


    @Autowired
    public RegisterController(UserRepository repository) {
        this.repository = repository;
    }

    /*@GetMapping("/")
    public String login() {
        return "login";
    }*/

    @GetMapping("/")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String userregistration(@ModelAttribute @Valid User user, Model model, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            logger.info("Validation errors occurred!");
            return "registration";
        }

        final boolean userIsRegistered = repository.findById(user.getUsername()).isPresent();
        if (!userIsRegistered) {
            User user_inserted = repository.save(user);
            return "login";
        }
        return "login";
    }

    @GetMapping("/login")
    public String userlogin(@ModelAttribute @Valid User user, Model model) {

        boolean userIsRegistered = repository.findById(user.getUsername()).isPresent();
        if (!userIsRegistered) {
            return "registration";
        }
        else {
            model.addAttribute("username", user.getUsername());
            return "welcome";
        }
    }



}
