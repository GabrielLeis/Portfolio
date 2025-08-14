package com.roundforma.website.springboot_roundforma_website.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.roundforma.website.springboot_roundforma_website.models.CustomUserDetails;
import com.roundforma.website.springboot_roundforma_website.models.Vehicle;
import com.roundforma.website.springboot_roundforma_website.repository.VehicleRepository;

@Service
public class VehicleService {
    
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private StandardUserService standardUserService;

    public Optional<Vehicle> findById(Integer Id){
        return vehicleRepository.findById(Id);
    }

    public List<Vehicle> findAll(){
        return vehicleRepository.findAll();
    }

    public Vehicle saveVehicle(Vehicle vehicle){
        return vehicleRepository.save(vehicle);
    }

    public Vehicle useVehicle(Vehicle vehicle, Authentication auth){
        //Se simula que el viaje le gasta un determinado numero de batería al vehículo.
        Random rm = new Random();
        vehicle.setBattery(rm.nextInt(101));
        //Se le hace el cargo al usuario.
        CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
        Double balance = user.getBalance() - vehicle.getFee();
        user.addFunds(balance);
        //Si el vehículo tiene menos del 20% de batería deja de estar disponible.
        if(vehicle.getBattery() < 20){
            vehicle.setAvailable(false);
        }
        try {
            //Este es es el que establece el balance del usuario en la base de datos.
            standardUserService.addBalance(user);
            saveVehicle(vehicle);

        } catch (Exception e) {
            System.out.println("Error al establecer balance del usuario al usar el vehículo: " + e);
        }

        return vehicle;
    }

}
