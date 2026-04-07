package com.remediationtracker.controller;
 
import com.remediationtracker.model.User;
import com.remediationtracker.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }
}
