package com.ganador.travelpedia.Review;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ganador.travelpedia.R;

public class UserReviewHolder extends RecyclerView.ViewHolder {
    View mView;

    TextView name, review;
    RatingBar rating;
    CardView userReviewCard;

    public UserReviewHolder(View itemView) {
        super(itemView);

        mView = itemView;
        userReviewCard = mView.findViewById(R.id.user_review_card);
        name = mView.findViewById(R.id.user_name);
        rating = mView.findViewById(R.id.user_rating);
        review = mView.findViewById(R.id.user_review);
    }

    public void setName(String mName) {
        name.setText(mName);
    }
    public void setRating(float mRating) { rating.setRating(mRating); }
    public void setReview(String mReview) {
        review.setText(mReview);
    }
}

