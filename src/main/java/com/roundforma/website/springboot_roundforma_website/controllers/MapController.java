package com.roundforma.website.springboot_roundforma_website.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roundforma.website.springboot_roundforma_website.models.Base;
import com.roundforma.website.springboot_roundforma_website.models.Coord;
import com.roundforma.website.springboot_roundforma_website.models.CustomUserDetails;
import com.roundforma.website.springboot_roundforma_website.models.StandardUserDTO;
import com.roundforma.website.springboot_roundforma_website.models.Vehicle;
import com.roundforma.website.springboot_roundforma_website.services.BaseService;
import com.roundforma.website.springboot_roundforma_website.services.StandardUserService;
import com.roundforma.website.springboot_roundforma_website.services.VehicleService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;





@RestController
@RequestMapping("/api")
public class MapController {

    @Autowired
    private StandardUserService standardUserService;
    @Autowired
    private BaseService baseService;
    @Autowired
    private VehicleService vehicleService;

    //Ruta para obtener todas las bases de la base de datos
    @GetMapping("/bases")
    public List<Base> getBases() {
        return baseService.findAll();
    }
    //Ruta para establecer una base a la base de datos.
    @PostMapping("/base")
    public ResponseEntity<Base> setBase(@RequestBody Base base) {
        //Se crea la lista con los vechiculos predeterminados para la base
        List<Vehicle> vehicles = List.of(
            new Vehicle("Bicycle", new Coord(base.getCoords().getX(), base.getCoords().getY()),12.0, base),
            new Vehicle("Scooter", new Coord(base.getCoords().getX(), base.getCoords().getY()),7.5, base)
        );
        base.setVehicles(vehicles);
        Base savedBase = baseService.saveBase(base);

        return new ResponseEntity<Base>(savedBase, HttpStatus.CREATED);
    }
    //Ruta para establecer una bicicleta en la base de datos
    @PostMapping("/set-bicycle")
    public ResponseEntity<Vehicle> setBicycle(@RequestBody Base base) {

        Vehicle bicycle = new Vehicle("Bicycle", new Coord(base.getCoords().getX(), base.getCoords().getY()), 7.5, base);
        Optional<Base> optionalBase = baseService.findById(base.getId());

        if(optionalBase.isPresent()){
            Base foundBase = optionalBase.get();
            foundBase.getVehicles().add(bicycle);
            baseService.saveBase(foundBase);
        }

        return new ResponseEntity<>(bicycle, HttpStatus.CREATED);
    }
    //Ruta para establecer un scooter en la base de datos
    @PostMapping("/set-scooter")
    public ResponseEntity<Vehicle> setScooter(@RequestBody Base base) {

        Vehicle scooter = new Vehicle("Scooter", new Coord(base.getCoords().getX(), base.getCoords().getY()), 7.5, base);
        Optional<Base> optionalBase = baseService.findById(base.getId());

        if(optionalBase.isPresent()){
            Base foundBase = optionalBase.get();
            foundBase.getVehicles().add(scooter);
            baseService.saveBase(foundBase);
        }

        return new ResponseEntity<>(scooter, HttpStatus.CREATED);
    }
    //Ruta para establecer el balance del usuario autenticado.
    @PostMapping("/add-balance")
    public ResponseEntity<Double> postMethodName(@RequestBody StandardUserDTO userDTO, Authentication auth) {
        CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
        Double userBalance = user.getBalance() + userDTO.getBalance();
        try {
            user.addFunds(userBalance);
            standardUserService.addBalance(user);
            return new ResponseEntity<>(userDTO.getBalance(), HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    //Ruta para obtener el balance del usuario autenticado.
    @GetMapping("/add-balance")
    public Double showFunds(Authentication auth) {
        CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
        return user.getBalance();
    }
    //Ruta para simular el uso de un vehiculo
    @PutMapping("/use-vehicle/{id}")
    public ResponseEntity<Vehicle> putMethodName(@PathVariable Integer id, Authentication auth) {
        Optional<Vehicle> vehicleOpt = vehicleService.findById(id);

        if(vehicleOpt.isPresent()){
            Vehicle vehicle = vehicleOpt.get();
            vehicleService.useVehicle(vehicle, auth);
            return new ResponseEntity<>(vehicle, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
