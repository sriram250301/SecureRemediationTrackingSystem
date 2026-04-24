package com.remediationtracker.controller;

import com.remediationtracker.dto.VulnerabilityRequest;
import com.remediationtracker.model.User;
import com.remediationtracker.model.Vulnerability;
import com.remediationtracker.repository.UserRepository;
import com.remediationtracker.repository.VulnerabilityRepository;
import com.remediationtracker.services.VulnerabilityHandlerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dev")
public class DevController {

    private final VulnerabilityHandlerService vulnService;
    private final VulnerabilityRepository     vulnRepo;
    private final UserRepository              userRepo;

    public DevController(VulnerabilityHandlerService vulnService,
                         VulnerabilityRepository vulnRepo,
                         UserRepository userRepo) {
        this.vulnService = vulnService;
        this.vulnRepo    = vulnRepo;
        this.userRepo    = userRepo;
    }

    // ── GET /dev/my-vulnerabilities → Developer's assigned list ───────
    //
    // This is the "Developer assigned vulnerabilities list" screenshot page.
    // getVulnerabilitiesForUser() already filters by assignedTo for non-ADMIN roles.
    //
    @GetMapping("/my-vulnerabilities")
    public String myVulnerabilities(Authentication auth, Model model) {
        User currentUser = userRepo.findByUsername(auth.getName()).orElseThrow();
        model.addAttribute("myVulnerabilities",
            vulnService.getVulnerabilitiesForUser(currentUser));
        return "dev-vulnerabilities";  // → templates/dev-vulnerabilities.html
    }

    // ── POST /dev/update/{id} → Developer updates status ──────────────
    //
    // This is the "remediation update action".
    // Developer submits the inline status form from dev-vulnerabilities.html.
    // We re-use VulnerabilityHandlerService.updateVulnerability() which already
    // enforces object-level security (only assigned user can update).
    //
    @PostMapping("/update/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,   // from the inline select
                               Authentication auth,
                               RedirectAttributes redirectAttrs) {

        User currentUser = userRepo.findByUsername(auth.getName()).orElseThrow();
        Vulnerability vuln = vulnRepo.findById(id).orElseThrow();

        // Object-level security: only the assigned developer (or ADMIN) can update
        if (vuln.getAssignedTo() == null ||
            !vuln.getAssignedTo().getId().equals(currentUser.getId()) &&
            currentUser.getRole() != User.Role.ADMIN) {

            redirectAttrs.addFlashAttribute("errorMessage", "Access denied.");
            return "redirect:/dev/my-vulnerabilities";
        }

        // Build a request DTO keeping all existing fields, only changing status
        VulnerabilityRequest req = new VulnerabilityRequest();
        req.title        = vuln.getTitle();
        req.description  = vuln.getDescription();
        req.severity     = vuln.getSeverity();
        req.status       = status;  // ← the new status from the form
        req.assignedToId = vuln.getAssignedTo() != null ? vuln.getAssignedTo().getId() : null;

        vulnService.updateVulnerability(id, req, currentUser);

        redirectAttrs.addFlashAttribute("successMessage", "Status updated successfully.");
        return "redirect:/dev/my-vulnerabilities";
    }
}
