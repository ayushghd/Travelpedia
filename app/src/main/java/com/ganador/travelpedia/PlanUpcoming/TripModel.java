package com.ganador.travelpedia.PlanUpcoming;


public class TripModel {

    public String tripName;
    public String tripMembers;

    public TripModel() {

    }

    public TripModel(String tripName, String tripMembers) {
        this.tripName = tripName;
        this.tripMembers = tripMembers;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName =  tripName;
    }

    public String getTripMembers() {
        return tripMembers;
    }

    public void setTripMembers(String tripMembers) {
        this.tripMembers = tripMembers;
    }
}
