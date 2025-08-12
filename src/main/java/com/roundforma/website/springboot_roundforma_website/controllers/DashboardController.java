package com.roundforma.website.springboot_roundforma_website.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.roundforma.website.springboot_roundforma_website.models.CustomUserDetails;


@Controller
public class DashboardController {

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard(Authentication auth, Model model) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("balance", user.getBalance());
        return "admin";
    }
    
    @GetMapping("/client/dashboard")
    @PreAuthorize("hasRole('CLIENT')")
    public String clientDashboard(Authentication auth, Model model) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("balance", user.getBalance());
        return "client";
    }
    
}
