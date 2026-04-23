package com.remediationtracker.controller;

import com.remediationtracker.model.User;
import com.remediationtracker.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;    
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebAuthController {

    private final UserService userService;

    public WebAuthController(UserService userService) {
        this.userService = userService;
    }

    // Spring Security shows /login automatically, but we need this to serve our template
    @GetMapping("/login")
    public String loginPage() {
        return "login";  //  looks for templates/login.html
    }

    // Show empty register form
    @GetMapping("/register")
    public String registerForm(Model model) {
        // We add an empty User object so th:object="${user}" in the template works
        model.addAttribute("user", new User());
        return "register";  //  templates/register.html
    }

    // Handle register form submission
    @PostMapping("/register")
    public String registerSubmit(@Valid @ModelAttribute("user") User user,
                                 BindingResult result,  // holds any validation errors
                                 Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(user);
            // Redirect to login with a success flag in URL
            return "redirect:/login?registered";
        } catch (Exception e) {
            model.addAttribute("error", "Username already exists.");
            return "register";
        }
    }
}
