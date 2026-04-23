package com.remediationtracker.controller;

import com.remediationtracker.model.User;
import com.remediationtracker.model.Vulnerability;
import com.remediationtracker.repository.UserRepository;
import com.remediationtracker.services.VulnerabilityHandlerService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final VulnerabilityHandlerService vulnService;
    private final UserRepository userRepo;

    public DashboardController(VulnerabilityHandlerService vulnService, UserRepository userRepo) {
        this.vulnService = vulnService;
        this.userRepo = userRepo;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Authentication auth, Model model) {

        User currentUser = userRepo.findByUsername(auth.getName()).orElseThrow();
        List<Vulnerability> vulnerabilities = vulnService.getVulnerabilitiesForUser(currentUser);

        // Counting stats for the dashboard cards
        long total    = vulnerabilities.size();
        long critical = vulnerabilities.stream().filter(v -> "CRITICAL".equals(v.getSeverity())).count();
        long high     = vulnerabilities.stream().filter(v -> "HIGH".equals(v.getSeverity())).count();
        long resolved = vulnerabilities.stream().filter(v -> "RESOLVED".equals(v.getStatus())).count();
        
        model.addAttribute("totalCount",    total);
        model.addAttribute("criticalCount", critical);
        model.addAttribute("highCount",     high);
        model.addAttribute("resolvedCount", resolved);

        return "dashboard";  //  templates/dashboard.html
    }
}
