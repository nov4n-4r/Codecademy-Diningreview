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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.codecademy.dinningreviewapi.model.User;
import com.codecademy.dinningreviewapi.repository.UserRepository;

import jakarta.validation.Valid;

@RequestMapping("/users")
@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(
        UserRepository userRepository
    ){
        this.userRepository = userRepository;
    }


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public User addUser(
        @RequestBody @Valid User user
    ){
        try{

            Optional<User> optionalUser = this.userRepository.findUserByName(user.getName());

            if(optionalUser.isPresent()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with current name is exist");                
            }

            return this.userRepository.save(user);
        }
        catch(Error e){
            if(Error.class.getName() == ResponseStatusException.class.getName()) throw e;
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Error", e);
        }
    }

    @PutMapping("/detail/{name}")
    @ResponseStatus(code = HttpStatus.OK)
    public User updateUser(
        @RequestBody @Valid User updatedUser,
        @PathVariable String name
    ){

        Optional<User> optionalUser = this.userRepository.findUserByName(name);
        
        if(optionalUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else{
            User user = optionalUser.get();
            if(!ObjectUtils.isEmpty(user.getCity())) user.setCity(updatedUser.getCity());
            if(!ObjectUtils.isEmpty(user.getZipCode())) user.setZipCode(updatedUser.getZipCode());
            if(!ObjectUtils.isEmpty(user.getHasDairyAllergy())) user.setHasDairyAllergy(updatedUser.getHasDairyAllergy());
            if(!ObjectUtils.isEmpty(user.getHasEggAllergy())) user.setHasEggAllergy(updatedUser.getHasEggAllergy());
            if(!ObjectUtils.isEmpty(user.getHasPeanutAllergy())) user.setHasPeanutAllergy(updatedUser.getHasPeanutAllergy());
            if(!ObjectUtils.isEmpty(user.getState())) user.setState(updatedUser.getState());
            return this.userRepository.save(user);
            
        }
        
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<User> getAllUsers() {

        return this.userRepository.findAll();
    
    }

    @GetMapping("/detail/{name}")
    @ResponseStatus(code = HttpStatus.OK)
    public User getUserByName(
        @PathVariable String name
    ){

        Optional<User> optionalUser = this.userRepository.findUserByName(name);

        if(optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        User user = optionalUser.get();

        return user;

    }

}
