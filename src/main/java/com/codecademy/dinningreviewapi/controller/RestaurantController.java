package com.codecademy.dinningreviewapi.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.codecademy.dinningreviewapi.model.Restaurant;
import com.codecademy.dinningreviewapi.repository.RestaurantRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping
    public Iterable<Restaurant> getAllRestaurants() {
        return this.restaurantRepository.findAll();
    }

    @PostMapping
    public Restaurant addRestaurant(
        @RequestBody @Valid Restaurant restaurant
    ) {
        return this.restaurantRepository.save(restaurant);
    }

    @GetMapping("/detail/{id}")
    public Restaurant getRestaurantById(
        @PathVariable String id
    ) {
        try{
            Long restaurantId = Long.parseLong(id);
            Optional<Restaurant> optionalRestaurant = this.restaurantRepository.findById(restaurantId);
            if(!optionalRestaurant.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);  
            Restaurant restaurant = optionalRestaurant.get();
            return restaurant;
        }catch(NumberFormatException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/detail/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Restaurant updateRestaurantById(
        @RequestBody @Valid Restaurant updatedRestaurant,
        @PathVariable String id
    ){

        try{
            
            Long restaurantId = Long.parseLong(id);
            Optional<Restaurant> optionalRestaurant = this.restaurantRepository.findById(restaurantId);

            if(optionalRestaurant.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }else{
                Restaurant restaurant = optionalRestaurant.get();
                if(!ObjectUtils.isEmpty(restaurant.getCity())) restaurant.setCity(updatedRestaurant.getCity());
                if(!ObjectUtils.isEmpty(restaurant.getZipCode())) restaurant.setZipCode(updatedRestaurant.getZipCode());
                if(!ObjectUtils.isEmpty(restaurant.getState())) restaurant.setState(updatedRestaurant.getState());
                if(!ObjectUtils.isEmpty(restaurant.getPhoneNumber())) restaurant.setPhoneNumber(updatedRestaurant.getPhoneNumber());
                if(!ObjectUtils.isEmpty(restaurant.getName())) restaurant.setName(updatedRestaurant.getName());
                return this.restaurantRepository.save(restaurant);
            }

        }catch(NumberFormatException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        
    }

    @GetMapping("/search")
    public Iterable<Restaurant> getRestaurants(
        @RequestParam String allergy,
        @RequestParam String zipCode
    ){
        if(zipCode != null){
            if(allergy != null) {
                if(allergy.equals("dairy")) {
                    return this.restaurantRepository.findRestaurantsByZipCodeAndDairyScoreNotNullOrderByDairyScore(zipCode);
                }else if(allergy.equals("peanut")) {
                    return this.restaurantRepository.findRestaurantsByZipCodeAndPeanutScoreNotNullOrderByPeanutScore(zipCode);
                }else if(allergy.equals("egg")) {
                    return this.restaurantRepository.findRestaurantsByZipCodeAndEggScoreNotNullOrderByEggScore(zipCode);
                }else{
                    throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid allergy, allergy should be one of 3 values: dairy, gluten, peanut"
                    );
                }
            }else{
                return this.restaurantRepository.findByZipCode(zipCode);
            }
        }else{
            return this.restaurantRepository.findAll();
        }
    }

}
