package com.roundforma.website.springboot_roundforma_website.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.roundforma.website.springboot_roundforma_website.repository.UserRepository;

@Component
public class AdminTester implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Solo crearlo si no existe
        if (userRepository.findByUsername("admin").isEmpty()) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setEmail("polopaterio@gmail.com");
            admin.setPassword(passwordEncoder.encode("1234"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println("-- Admin creado con éxito --");
        } else {
            System.out.println("-- Admin ya existe --");
        }
    }

}
