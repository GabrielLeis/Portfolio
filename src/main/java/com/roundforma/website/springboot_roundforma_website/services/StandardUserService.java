package com.roundforma.website.springboot_roundforma_website.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundforma.website.springboot_roundforma_website.models.CustomUserDetails;
import com.roundforma.website.springboot_roundforma_website.models.StandardUser;
import com.roundforma.website.springboot_roundforma_website.models.User;
import com.roundforma.website.springboot_roundforma_website.repository.UserRepository;


@Service
public class StandardUserService extends UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByUsername(String userName){
        return userRepository.findByUsername(userName);
    }

    public StandardUser saveUser(StandardUser user){
        return userRepository.save(user);
    }

    public Double addBalance(CustomUserDetails user) throws Exception {
        System.out.println(user.getBalance());
        Optional<User> dbUser = super.findById(user.getUserId());
        if(dbUser.isPresent()){
            StandardUser standard = (StandardUser)dbUser.get();
            standard.setBalance(user.getBalance());
            userRepository.save(standard);
            return standard.getBalance();
        } else {
            throw new Exception("User not Found");
        }
        
    }
}
