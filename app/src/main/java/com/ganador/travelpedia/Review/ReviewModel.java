package com.ganador.travelpedia.Review;

public class ReviewModel {
    public String city;
    public String count;
    public String rating;
    public ReviewModel() {

    }

    public ReviewModel(String city, String count, String rating) {
        this.city = city;
        this.count = count;
        this.rating = rating;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
