package com.remediationtracker.controller;

import com.remediationtracker.repository.AuditLogRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuditLogWebController {

    private final AuditLogRepository auditRepo;

    public AuditLogWebController(AuditLogRepository auditRepo) {
        this.auditRepo = auditRepo;
    }

    @GetMapping("/audit")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public String auditLogs(Model model) {
        model.addAttribute("logs", auditRepo.findAll());
        return "audit-logs";  //  templates/audit-logs.html
    }
}