package com.roundforma.website.springboot_roundforma_website.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StandardUserDTO {
    private Double balance;

    public StandardUserDTO(StandardUser user){
        this.balance = user.getBalance();
    }
}
