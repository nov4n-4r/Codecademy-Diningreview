package com.codecademy.dinningreviewapi.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.codecademy.dinningreviewapi.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    
    Optional<User> findUserByName(String name);

}
