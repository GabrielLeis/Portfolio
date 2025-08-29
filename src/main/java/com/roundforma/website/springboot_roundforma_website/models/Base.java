package com.roundforma.website.springboot_roundforma_website.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "bases")
public class Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "base", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Vehicle> vehicles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coord_id", referencedColumnName = "id")
    private Coord coords;
    private boolean breakdownState = false;


}
