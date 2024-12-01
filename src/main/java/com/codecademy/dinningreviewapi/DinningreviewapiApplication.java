package com.codecademy.dinningreviewapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.codecademy.dinningreviewapi.model.Restaurant;
import com.codecademy.dinningreviewapi.model.Review;
import com.codecademy.dinningreviewapi.model.ReviewStatus;
import com.codecademy.dinningreviewapi.model.User;
import com.codecademy.dinningreviewapi.repository.RestaurantRepository;
import com.codecademy.dinningreviewapi.repository.ReviewRepository;
import com.codecademy.dinningreviewapi.repository.UserRepository;

@SpringBootApplication
public class DinningreviewapiApplication {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	public static void main(String[] args) {
		SpringApplication.run(DinningreviewapiApplication.class, args);
	}

	@Bean
	UserRepository seedUser(){
		
		// Create the list of users
        List<User> users = new ArrayList<>();

        // Add users using User.from method
        users.add(User.from("Alice", "New York", "NY", "10001", true, false, true));
        users.add(User.from("Bob", "Houston", "TX", "77001", false, true, false));
        users.add(User.from("Fiona", "Phoenix", "AZ", "85001", true, true, false));
        users.add(User.from("Charlie", "Chicago", "IL", "60601", false, true, true));
        users.add(User.from("Helen", "Los Angeles", "CA", "90001", false, false, true));

        // Save users to confirm
        users.forEach(user -> {
            this.userRepository.save(user);
        });	

		return this.userRepository;
	}

	@Bean
	RestaurantRepository seedRestaurant(){
		
		// Create the list of restaurants
		List<Restaurant> restaurants = new ArrayList<>();

        // Add restaurants using Restaurant.from method
        restaurants.add(Restaurant.from("Joe's Diner", "New York", "NY", "10001", "123-456-7890"));
        restaurants.add(Restaurant.from("Texas BBQ", "Houston", "TX", "77001", "234-567-8901"));
        restaurants.add(Restaurant.from("Cactus Grill", "Phoenix", "AZ", "85001", "345-678-9012"));
        restaurants.add(Restaurant.from("Deep Dish Heaven", "Chicago", "IL", "60601", "456-789-0123"));
        restaurants.add(Restaurant.from("Sunset Cafe", "Los Angeles", "CA", "90001", "567-890-1234"));

        // Save restaurants to confirm
        restaurants.forEach(restaurant -> {
            this.restaurantRepository.save(restaurant);
        });

		return this.restaurantRepository;
	}

	@Bean
	ReviewRepository seedReview(){
		// Initialize Users and Restaurants (assuming these come from previous seeds)
        List<User> users = new ArrayList<User>();

		seedUser().findAll().forEach(user -> users.add(user));

        List<Restaurant> restaurants = new ArrayList<Restaurant>();

		seedRestaurant().findAll().forEach(restaurant -> restaurants.add(restaurant));

        // Generate 20 reviews
        List<Review> reviews = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            User randomUser = users.get(random.nextInt(users.size())); // Pick a random user
            Restaurant randomRestaurant = restaurants.get(random.nextInt(restaurants.size())); // Pick a random restaurant

            Review review = new Review();
            review.setSubmittedBy(randomUser.getName());
            review.setRestaurantId(randomRestaurant.getId());
            review.setNote("This is review #" + (i + 1) + " for " + randomRestaurant.getName());
            review.setEggScore(random.nextInt(6)); // Random score between 0 and 5
            review.setPeanutScore(random.nextInt(6)); // Random score between 0 and 5
            review.setDairyScore(random.nextInt(6)); // Random score between 0 and 5
            review.setStatus(ReviewStatus.PENDING); // Assuming an enum value for approved status

            reviews.add(review);
        }

        // Print reviews to confirm
        reviews.forEach(review -> {
            reviewRepository.save(review);
        });

		return this.reviewRepository;

    }


}
