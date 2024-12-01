package com.codecademy.dinningreviewapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "Users")
@RequiredArgsConstructor
public class User {
    
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false, length = 5)
    private String zipCode;

    private Boolean hasPeanutAllergy;

    private Boolean hasDairyAllergy;

    private Boolean hasEggAllergy;

    public static User from(
        String name, 
        String city, 
        String state, 
        String zipCode, 
        Boolean hasPeanutAllergy, 
        Boolean hasDairyAllergy, 
        Boolean hasEggAllergy
    ){

        User user = new User();
        user.setName(name);
        user.setCity(city);
        user.setState(state);
        user.setZipCode(zipCode);
        user.setHasPeanutAllergy(hasPeanutAllergy);
        user.setHasDairyAllergy(hasDairyAllergy);
        user.setHasEggAllergy(hasEggAllergy);
        return user; 
    
    }

}
