package com.ganador.travelpedia.HotelBook;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ganador.travelpedia.R;

public class HotelBooking extends AppCompatActivity implements View.OnClickListener {
    private CardView hotelCard1,hotelCard2,hotelCard3,hotelCard4,hotelCard5,hotelCard6;
    private ImageView hotelImage1,hotelImage2,hotelImage3,hotelImage4,hotelImage5,hotelImage6;
    private TextView hotelText1,hotelText2,hotelText3,hotelText4,hotelText5,hotelText6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_booking);
        hotelCard1 = findViewById(R.id.hotelCard1);
        hotelCard2 = findViewById(R.id.hotelCard2);
        hotelCard3 = findViewById(R.id.hotelCard3);
        hotelCard4 = findViewById(R.id.hotelCard4);
        hotelCard5 = findViewById(R.id.hotelCard5);
        hotelCard6 = findViewById(R.id.hotelCard6);
        hotelImage1 = findViewById(R.id.hotelImage1);
        hotelImage2 = findViewById(R.id.hotelImage2);
        hotelImage3 = findViewById(R.id.hotelImage3);
        hotelImage4 = findViewById(R.id.hotelImage4);
        hotelImage5 = findViewById(R.id.hotelImage5);
        hotelImage6 = findViewById(R.id.hotelImage6);
        hotelText1 = findViewById(R.id.hotelText1);
        hotelText2 = findViewById(R.id.hotelText2);
        hotelText3 = findViewById(R.id.hotelText3);
        hotelText4 = findViewById(R.id.hotelText4);
        hotelText5 = findViewById(R.id.hotelText5);
        hotelText6 = findViewById(R.id.hotelText6);
        hotelCard1.setOnClickListener(this);
        hotelCard2.setOnClickListener(this);
        hotelCard3.setOnClickListener(this);
        hotelCard4.setOnClickListener(this);
        hotelCard5.setOnClickListener(this);
        hotelCard6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hotelCard1:
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, "https://www.makemytrip.com");
                startActivity(intent);
                break;
            case R.id.hotelCard2:
                intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, "https://www.oyorooms.com");
                startActivity(intent);
                break;
            case R.id.hotelCard3:
                intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, "https://www.irctc.co.in");
                startActivity(intent);
                break;
            case R.id.hotelCard4:
                intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, "https://www.booking.com");
                startActivity(intent);
                break;
            case R.id.hotelCard5:
                intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, "https://www.goibibo.com");
                startActivity(intent);
                break;
            case R.id.hotelCard6:
                intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, "https://www.trivago.com");
                startActivity(intent);
                break;
        }
    }
}
