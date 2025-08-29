package com.roundforma.website.springboot_roundforma_website.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.roundforma.website.springboot_roundforma_website.models.LoginForm;

import org.springframework.ui.Model;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm(Model model){
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }
}
