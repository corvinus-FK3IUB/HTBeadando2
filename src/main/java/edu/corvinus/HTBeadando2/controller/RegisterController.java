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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

@Controller
public class RegisterController {

    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    private final UserRepository repository;


    @Autowired
    public RegisterController(UserRepository repository) {
        this.repository = repository;
    }


    @GetMapping("/")
    public String loginstart(User user) {
        return "login";
    }
    @GetMapping("/registration")
    public String registration(User user) {
        return "registration";
    }

    @PostMapping("/registration")
    public String userregistration(@Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            logger.info("Validation errors occurred!");
            return "registration";
        }

        final boolean userIsRegistered = repository.findById(user.getUsername()).isPresent();
        if (!userIsRegistered) {
            String hashedpw = sha256(user.getPassword());
            user.setPassword(hashedpw);

            User user_inserted = repository.save(user);
            return "/login";
        } else {
            return "/login";
        }
    }

    @GetMapping("/login")
    public String login(User user) {
        return "/login";
    }


    @PostMapping("/login")
    public String userlogin(@Valid User user, Model model) {

        Optional<User> userindb = repository.findById(user.getUsername());
        boolean userIsRegistered = userindb.isPresent();
        if (!userIsRegistered) {
            return "/registration";
        }
        else {
            String hashedpw = sha256(user.getPassword());
            boolean passwordiscorrect = userindb.get().getPassword().equals(hashedpw);
            if (passwordiscorrect) {

            model.addAttribute("username", user.getUsername());
            return "/welcome";
            }
        else {
                logger.info("Incorrect password!");
                model.addAttribute("pwerror","Rossz a jelszó, próbálja újra!");
                return "login";
            }
        }
    }

    public static String sha256(final String base) {
        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            final StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                final String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }


}
