package com.roundforma.website.springboot_roundforma_website.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    //Endpoint del home page
    @GetMapping("/inicio")
    public String getHomePage(){
        return "index";
    }
}
