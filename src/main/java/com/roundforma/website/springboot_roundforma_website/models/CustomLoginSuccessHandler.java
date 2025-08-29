package com.roundforma.website.springboot_roundforma_website.models;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {
        
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String redirectUrl = "login?error";

        for(GrantedAuthority authority: authorities){
            switch (authority.getAuthority()) {
                case "ROLE_ADMIN":
                    redirectUrl = "admin/dashboard";
                    break;
                case "ROLE_CLIENT":
                    redirectUrl = "client/dashboard";
                    break;
            }
        }
        response.sendRedirect(redirectUrl);
    }

}
