package com.roundforma.website.springboot_roundforma_website.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "coords")
public class Coord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double x;
    private double y;

    public Coord(){}

    public Coord(double x, double y){
        this.x = x;
        this.y = y;
    }

    //Getter y Setters
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }

    public void setCoord(double x, double y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return x + "," + y;
    }
    
}
