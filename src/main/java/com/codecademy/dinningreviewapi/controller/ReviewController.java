package com.codecademy.dinningreviewapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.codecademy.dinningreviewapi.model.AdminReviewAction;
import com.codecademy.dinningreviewapi.model.Restaurant;
import com.codecademy.dinningreviewapi.model.Review;
import com.codecademy.dinningreviewapi.model.ReviewStatus;
import com.codecademy.dinningreviewapi.model.User;
import com.codecademy.dinningreviewapi.repository.RestaurantRepository;
import com.codecademy.dinningreviewapi.repository.ReviewRepository;
import com.codecademy.dinningreviewapi.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    
    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;
    private ReviewRepository reviewRepository;

    public ReviewController(
        UserRepository userRepository,
        RestaurantRepository restaurantRepository,
        ReviewRepository reviewRepository
    ){
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping
    public Iterable<Review> getAllReviews(){
        return this.reviewRepository.findAll();
    }

    @PostMapping
    public Review addReview(
        @RequestBody @Valid Review newReview
    ){

        Optional<User> optionalSubmittedBy = this.userRepository.findUserByName(newReview.getSubmittedBy());

        if(!optionalSubmittedBy.isPresent()) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Submitted user not found in the database");

        Optional<Restaurant> optionalRestaurant = this.restaurantRepository.findById(newReview.getRestaurantId());

        if(!optionalRestaurant.isPresent()) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Restaurant with current id not found in the database");

        return this.reviewRepository.save(newReview);

    }

    @PutMapping("/admin/{id}")
    public Review updateReviewAsAdmin(
        @PathVariable Long id,
        @RequestBody @Valid AdminReviewAction adminReviewAction
    ){


        Optional<Review> optionalReview = this.reviewRepository.findById(id);

        if(!optionalReview.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Review review = optionalReview.get();

        if(!adminReviewAction.getIsAccept()){
            review.setStatus(ReviewStatus.REJECTED);
            return this.reviewRepository.save(review);
        }

        if(
            adminReviewAction.getIsAccept() &&
            review.getStatus() != ReviewStatus.APPROVED
        ){
            
            review.setStatus(ReviewStatus.APPROVED);

            this.reviewRepository.save(review);

            // Update retaurant's review score

            Optional<Restaurant> optionalRestaurant = this.restaurantRepository.findById(review.getRestaurantId());
            if(!optionalRestaurant.isPresent()) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Restaurant with current id not found in the database");
            Restaurant restaurant = optionalRestaurant.get();

            List<Review> reviews = this.reviewRepository.findByStatusAndRestaurantId(ReviewStatus.APPROVED, review.getRestaurantId());

            List<Double> overallScores = new ArrayList<Double>();
            List<Integer> dairyScores = new ArrayList<Integer>();
            List<Integer> eggScores = new ArrayList<Integer>();
            List<Integer> peanutScores = new ArrayList<Integer>();

            reviews.forEach(
                re -> {
                    if(re.getDairyScore() != null) dairyScores.add(re.getDairyScore());
                    if(re.getEggScore() != null) eggScores.add(re.getEggScore());
                    if(re.getPeanutScore() != null) peanutScores.add(re.getPeanutScore()); 
                }
            );

            if(dairyScores.size() > 0){
                Double avgDairyScore = dairyScores.stream().mapToInt(Integer::intValue).average().getAsDouble();
                restaurant.setDairyScore(avgDairyScore);  
                overallScores.add(avgDairyScore);
            }

            if(eggScores.size() > 0){
                Double avgEggScore = eggScores.stream().mapToInt(Integer::intValue).average().getAsDouble();
                restaurant.setEggScore(avgEggScore);
                overallScores.add(avgEggScore);
            }

            if(peanutScores.size() > 0){
                Double avgPeanutScore = peanutScores.stream().mapToInt(Integer::intValue).average().getAsDouble();
                restaurant.setPeanutScore(avgPeanutScore);
                overallScores.add(avgPeanutScore);
            }

            restaurant.setOverallScore(
                overallScores.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0)
            );

            this.restaurantRepository.save(restaurant);

            Logger logger = LoggerFactory.getLogger(ReviewController.class);

            logger.info("Overall score length: ".concat(String.valueOf(overallScores.size())));
            logger.info(restaurant.toString());

        }

        return review;

    }


}
