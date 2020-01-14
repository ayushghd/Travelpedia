package com.ganador.travelpedia.PlanUpcoming;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ganador.travelpedia.R;

public class TripViewHolder extends RecyclerView.ViewHolder {

    View mView;

    TextView tripName;
    TextView tripMembers;
    TextView tripBudget;
    TextView tripDate;
    CardView tripCard;

    public TripViewHolder(View itemView) {
        super(itemView);

        mView = itemView;

        tripName = mView.findViewById(R.id.trip_name);
        tripMembers = mView.findViewById(R.id.trip_members);
        tripDate = mView.findViewById(R.id.trip_cost);
        tripCard = mView.findViewById(R.id.trip_card);
        tripBudget = mView.findViewById(R.id.tripDate);
    }

    public void setTripName(String id) {
        tripName.setText(id);
    }

    public void setTripBudget(String budget) { tripBudget.setText(budget);}
    public void setTripDate(String date) { tripDate.setText(date);}

    public void setTripMembers(String members) {
        tripMembers.setText(members);
    }
}
