package com.roundforma.website.springboot_roundforma_website.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundforma.website.springboot_roundforma_website.models.Base;
import com.roundforma.website.springboot_roundforma_website.repository.BaseRepository;

@Service
public class BaseService {

    @Autowired
    private BaseRepository baseRepository;

    public Optional<Base> findById(Integer Id){
        return baseRepository.findById(Id);
    }

    public List<Base> findAll(){
        return baseRepository.findAll();
    }

    public Base saveBase(Base base){
        return baseRepository.save(base);
    }

}
