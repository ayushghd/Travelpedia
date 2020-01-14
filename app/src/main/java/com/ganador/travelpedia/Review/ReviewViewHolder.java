package com.ganador.travelpedia.Review;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ganador.travelpedia.R;

public class ReviewViewHolder extends RecyclerView.ViewHolder {
    View mView;

    TextView city, rev_count, average;
    RatingBar av_rating;
    CardView reviewCard;

    public ReviewViewHolder(View itemView) {
        super(itemView);

        mView = itemView;

        city = mView.findViewById(R.id.rev_city);
        rev_count = mView.findViewById(R.id.rev_count);
        av_rating = mView.findViewById(R.id.rev_avrating);
        average = mView.findViewById(R.id.rev_avr);
        reviewCard = mView.findViewById(R.id.review_card);
    }

    public void setCity(String mCity) {
        city.setText(mCity);
    }
    public void setRevCount(String mRevCount) { rev_count.setText(mRevCount); }
    public void setAvRating(String mAvRating) {
        av_rating.setRating(Float.parseFloat(mAvRating));
    }
    public void setAverage(String mAvRating) {
        average.setText(mAvRating);
    }

}
