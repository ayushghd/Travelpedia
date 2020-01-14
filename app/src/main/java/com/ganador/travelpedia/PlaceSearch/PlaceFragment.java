package com.ganador.travelpedia.PlaceSearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.ganador.travelpedia.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlaceFragment extends AppCompatActivity implements Serializable {
    private Double mLat,mLng;
    private TextView mTVPhotosCount = null;
    private TextView mTVVicinity = null;
    private ViewFlipper mFlipper = null;
    private TextView mTVname = null;
    private RatingBar mRatingBar;
    private String mPlace = null;
    private String url;
    private String placeDescription;
    private Double rating = 0.0;
    private String old_url;
    private Button findRoute;
    private Button streetView;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_fragment);
        Intent intent =getIntent();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.show();
        mPlace = intent.getStringExtra("placeName");
        url = intent.getStringExtra("url");
        rating = intent.getDoubleExtra("rating",0.0);
        mLat = intent.getDoubleExtra("latitude",0.0);
        mLng = intent.getDoubleExtra("longitude",0.0);
        placeDescription = intent.getStringExtra("placeDescription");
        Log.e("notttttttt", mPlace);
        mTVname = findViewById(R.id.tv_name);
        mTVVicinity = findViewById(R.id.tv_vicinity);
        mFlipper = findViewById(R.id.flipper);
        mRatingBar = findViewById(R.id.tv_rating);
        findRoute = findViewById(R.id.findRoute);
        streetView = findViewById(R.id.streetView);
        mRatingBar.setRating(rating.floatValue());
        mTVname.setText(mPlace);
        mTVVicinity.setText("Address: " + placeDescription);
        if(url != null)
        {
            DisplayMetrics mMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
            int width = (int)(mMetrics.widthPixels*2);
            int height = (int)(mMetrics.heightPixels*1)/2;
            old_url = "https://maps.googleapis.com/maps/api/place/photo?";
            String key = "key=AIzaSyBetBer1jCeuflwEc_EnbxUnFiWFabxJZg";
            String sensor = "sensor=true";
            String maxWidth="maxwidth=" + width;
            String maxHeight = "maxheight=" + height;
            old_url = old_url + "&" + key + "&" + sensor + "&" + maxWidth + "&" + maxHeight;
            url = old_url + url;
            ImageDownloadTask imageDownloadTask = new ImageDownloadTask();
            imageDownloadTask.execute(url);
        }
       findRoute.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.e("findroute","findroute");
                String uri = "http://maps.google.com/maps?daddr=" + placeDescription ;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        } );
        streetView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.e("streetview",mLat + " " + mLng);
                Intent intent = new Intent(PlaceFragment.this,StreetView.class);
                intent.putExtra("latitude",mLat);
                intent.putExtra("longitude",mLng);
                startActivity(intent);
            }
        } );
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private Bitmap downloadImage(String strUrl) throws IOException {
        Bitmap bitmap=null;
        InputStream iStream = null;
        try{
            Log.e("imageurl",strUrl);
            URL url = new URL(strUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(iStream);

        }catch(Exception e){
            Log.d("Exception  url", e.toString());
        }finally{
            iStream.close();
        }
        return bitmap;
    }

    private class ImageDownloadTask extends AsyncTask<String, Integer, Bitmap> {
        Bitmap bitmap = null;
        @Override
        protected Bitmap doInBackground(String... url) {
            try{
                bitmap = downloadImage(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView iView = new ImageView(getApplicationContext());
            iView.setImageBitmap(result);
            mFlipper.addView(iView);
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }
}
