package com.ganador.travelpedia.Review;

public class UserReviewModel {
    public String name;
    public String rating;
    public String review;
    public UserReviewModel() {

    }

    public UserReviewModel(String name, String rating, String review) {
        this.name = name;
        this.rating = rating;
        this.review = review;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String count) { this.rating = rating; }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
