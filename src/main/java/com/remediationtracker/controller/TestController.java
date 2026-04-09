package com.remediationtracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/admin")
    public String admin() {
        return "Admin access";
    }

    @GetMapping("/analyst")
    public String analyst() {
        return "Analyst access";
    }

    @GetMapping("/dev")
    public String dev() {
        return "Developer access";
    }
}
