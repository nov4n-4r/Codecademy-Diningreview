package com.codecademy.dinningreviewapi.model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Restaurants")
@RequiredArgsConstructor
public class Restaurant {
    
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false, length = 5)
    private String zipCode;

    @Column(nullable = false)
    private String phoneNumber;

    private Double overallScore;

    @Min(0)
    @Max(5)
    @Column(nullable = true)
    private Double eggScore;

    @Min(0)
    @Max(5)
    @Column(nullable = true)
    private Double peanutScore;

    @Min(0)
    @Max(5)
    @Column(nullable = true)
    private Double dairyScore;

    public static Restaurant from(
        String name,
        String city,
        String state,
        String zipCode,
        String phoneNumber
    ){
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setCity(city);
        restaurant.setState(state);
        restaurant.setZipCode(zipCode);
        restaurant.setPhoneNumber(phoneNumber);
        restaurant.setDairyScore(0.0);
        restaurant.setEggScore(0.0);
        restaurant.setPeanutScore(0.0);
        restaurant.setOverallScore(0.0);

        return restaurant;
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

}
