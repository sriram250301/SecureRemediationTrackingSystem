package com.remediationtracker.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;

@SpringBootApplication (exclude = SecurityAutoConfiguration.class)
public class RemediationTracker {
    public static void main(String[] args) {
        SpringApplication.run(RemediationTracker.class, args);
    }
}