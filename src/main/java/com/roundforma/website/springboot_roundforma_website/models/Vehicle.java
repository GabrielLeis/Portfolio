package com.roundforma.website.springboot_roundforma_website.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vehicleId;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coord_id", referencedColumnName = "id")
    private Coord coords;
    @ManyToOne
    @JoinColumn(name = "base_id")
    @JsonBackReference
    private Base base;
    private String name;
    private boolean breakdownState = false;
    private boolean available = true;
    private Integer battery = 100;
    private double fee;

    public Vehicle(String name, Coord coord, double fee, Base base){
        this.name = name;
        this.coords = coord;
        this.fee = fee;
        this.base = base;
    }

}
