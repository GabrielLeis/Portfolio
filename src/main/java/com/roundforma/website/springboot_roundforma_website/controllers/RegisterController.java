package com.roundforma.website.springboot_roundforma_website.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.roundforma.website.springboot_roundforma_website.models.RegisterForm;
import com.roundforma.website.springboot_roundforma_website.models.StandardUser;
import com.roundforma.website.springboot_roundforma_website.services.StandardUserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class RegisterController {

    @Autowired
    private StandardUserService standardService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String getRegisterRoute(Model model) {
        RegisterForm form = new RegisterForm();
        model.addAttribute("registerForm", form);
        return "register";
    }

    @PostMapping("/register")
    public String postRegisterForm(@RequestParam String username, @RequestParam String password, @RequestParam String email) {
        if(standardService.findByUsername(username).isPresent()){
            return "redirect:/register?error"; // ya existe
        }

        StandardUser st = new StandardUser();
        st.setUsername(username);
        st.setPassword(passwordEncoder.encode(password));
        st.setEmail(email);
        st.setRole("CLIENT");
        standardService.saveUser(st);

        return "redirect:/dashboard";
    }
    
    
}
