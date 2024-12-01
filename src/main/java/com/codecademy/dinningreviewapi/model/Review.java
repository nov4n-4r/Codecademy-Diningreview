package com.codecademy.dinningreviewapi.model;

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
@Table(name = "Reviews")
@RequiredArgsConstructor
public class Review {
    
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String submittedBy;
    
    @Column(nullable = false)
    private Long restaurantId;

    @Column(nullable = true)
    private String note;

    @Min(0)
    @Max(5)
    @Column(nullable = true)
    private Integer eggScore;

    @Min(0)
    @Max(5)
    @Column(nullable = true)
    private Integer peanutScore;
    
    @Min(0)
    @Max(5)
    @Column(nullable = true)
    private Integer dairyScore;

    private ReviewStatus status = ReviewStatus.PENDING;

}
