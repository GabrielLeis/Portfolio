package com.roundforma.website.springboot_roundforma_website.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundforma.website.springboot_roundforma_website.models.User;
import com.roundforma.website.springboot_roundforma_website.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //Lista todos los StandardUser
    public List<User> listUsers(){
        return userRepository.findAll();
    }
    //Encuentra un StandardUser por Id
    public Optional<User> findById(int id){
        return userRepository.findById(id);
    }
    //Guarda un StandardUser en la base de datos.
    public User saveUser(User st) {
        return userRepository.save(st);
    }
    //Elimina un StandardUser por Id
    public void deleteUser(int id){
        userRepository.deleteById(id);
    }

}
