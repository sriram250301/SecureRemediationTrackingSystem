package com.remediationtracker.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.remediationtracker.model.User;
import com.remediationtracker.repository.UserRepository;
import com.remediationtracker.services.UserService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")   // Entire controller — ADMIN only
public class AdminController {

    private final UserRepository userRepo;
    private final UserService userService;

    public AdminController(UserRepository userRepo, UserService userService) {
        this.userRepo    = userRepo;
        this.userService = userService;
    }

    // ── GET /admin/users → show user management page ──────────────────
    @GetMapping("/users")
    public String usersPage(Model model) {
        model.addAttribute("users",   userRepo.findAll());  // All users for the table
        model.addAttribute("newUser", new User());          // Empty User for the create form
        return "admin-users";  // → templates/admin-users.html
    }

    // ── POST /admin/users/create → create a new user ──────────────────
    @PostMapping("/users/create")
    public String createUser(@ModelAttribute("newUser") User user,
                             RedirectAttributes redirectAttrs) {
        try {
            userService.registerUser(user);  // registerUser already BCrypt-hashes the password
            redirectAttrs.addFlashAttribute("successMessage",
                "User '" + user.getUsername() + "' created successfully.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMessage",
                "Failed to create user: username may already exist.");
        }
        return "redirect:/admin/users";
    }

    // ── POST /admin/users/{id}/role → update a user's role ────────────
    //
    // The table has an inline form per row with a role dropdown.
    // This handles that form submit.
    //
    @PostMapping("/users/{id}/role")
    public String updateRole(@PathVariable Long id,
                             @RequestParam String role,  // matches name="role" in the select
                             Authentication auth,
                             RedirectAttributes redirectAttrs) {

        User user = userRepo.findById(id).orElseThrow();

        // Prevent ADMIN from changing their own role (safety guard)
        if (user.getUsername().equals(auth.getName())) {
            redirectAttrs.addFlashAttribute("errorMessage", "You cannot change your own role.");
            return "redirect:/admin/users";
        }

        user.setRole(User.Role.valueOf(role));  // Convert String "ADMIN" → enum Role.ADMIN
        userRepo.save(user);

        redirectAttrs.addFlashAttribute("successMessage",
            "Role updated for '" + user.getUsername() + "'.");
        return "redirect:/admin/users";
    }

    // ── POST /admin/users/{id}/delete → delete a user ─────────────────
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id,
                             Authentication auth,
                             RedirectAttributes redirectAttrs) {

        User user = userRepo.findById(id).orElseThrow();

        // Prevent self-deletion
        if (user.getUsername().equals(auth.getName())) {
            redirectAttrs.addFlashAttribute("errorMessage", "You cannot delete your own account.");
            return "redirect:/admin/users";
        }

        userRepo.deleteById(id);
        redirectAttrs.addFlashAttribute("successMessage",
            "User '" + user.getUsername() + "' deleted.");
        return "redirect:/admin/users";
    }
}
