package com.codecademy.dinningreviewapi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.codecademy.dinningreviewapi.model.Review;
import com.codecademy.dinningreviewapi.model.ReviewStatus;

public interface ReviewRepository extends CrudRepository<Review, Long>{

    List<Review> findByStatus(ReviewStatus status);

    List<Review> findByStatusAndRestaurantId(ReviewStatus status, Long restaurantId);

}
