package com.roundforma.website.springboot_roundforma_website.models;

import java.util.Collection;
import java.util.List;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public String getPassword() {return user.getPassword();}
    @Override
    public String getUsername() {return user.getUsername();}
    @Override
    public boolean isAccountNonExpired() {return true;}
    @Override
    public boolean isAccountNonLocked() {return true;}
    @Override
    public boolean isCredentialsNonExpired() {return true;}
    @Override
    public boolean isEnabled() {return true;}

    public Integer getUserId() {return user.getUserId();}
    
    public Double getBalance() {
        if(user instanceof StandardUser){
            return ((StandardUser)user).getBalance();
        }
        return 0.00;
        
    }
    public User getUser() {
        return user;
    }
    public void addFunds(double funds) {
        if(user instanceof StandardUser){
            ((StandardUser)user).setBalance(funds);
        } else {
            throw new RuntimeException("El usuario no es un StandardUser para poder obtener un balance.");
        }
    }

}
