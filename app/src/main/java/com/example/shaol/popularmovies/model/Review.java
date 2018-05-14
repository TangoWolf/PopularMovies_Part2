package com.example.shaol.popularmovies.model;

/**
 * Created by shaol on 5/13/2018.
 */

public class Review {
    private String reviewer;
    private String review;

    public Review(String reviewer, String review) {
        this.reviewer = reviewer;
        this.review = review;
    }

    public String getReviewer() {return reviewer;}

    public void setReviewer(String reviewer) {this.reviewer = reviewer;}

    public String getReview() {return review;}

    public void setReview(String review) {this.review = review;}
}