package com.roundforma.website.springboot_roundforma_website.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundforma.website.springboot_roundforma_website.models.Admin;
import com.roundforma.website.springboot_roundforma_website.models.User;
import com.roundforma.website.springboot_roundforma_website.repository.UserRepository;

@Service
public class AdminService extends UserService{

    @Autowired
    private UserRepository userRepository;

    public boolean login(String userName, String rawPassword){
        Optional<User> user = userRepository.findByUsername(userName);
        if (user.isPresent() && user.get().getClass() == Admin.class) {
            Admin admin = (Admin)user.get();
            return admin.getPassword().equals(rawPassword);
        }

        return false;
    }

}
