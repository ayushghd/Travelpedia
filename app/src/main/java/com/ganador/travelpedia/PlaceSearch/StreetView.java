package com.ganador.travelpedia.PlaceSearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;

import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.model.LatLng;

public class StreetView extends AppCompatActivity {

    private Double mLat,mLng;
    private static LatLng place_location ;
    private StreetViewPanorama mStreetView;
    private StreetViewPanoramaView mStreeView;
    private static final String STREETVIEW_BUNDLE_KEY = "StreetViewBundleKey";
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        dialog = new ProgressDialog(this);
        dialog.show();
        mLat = intent.getDoubleExtra("latitude",0.0);
        mLng = intent.getDoubleExtra("longitude",0.0);
        place_location = new LatLng(mLat,mLng);
        Log.e("instreetviewactivity",mLat + " " + mLng);
        StreetViewPanoramaOptions options = new StreetViewPanoramaOptions();
        if (savedInstanceState == null) {
            options.position(place_location);
        }
        mStreeView = new StreetViewPanoramaView(this, options);
        addContentView(mStreeView,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        Bundle bundle = null;
        if (savedInstanceState != null) {
            bundle = savedInstanceState.getBundle(STREETVIEW_BUNDLE_KEY);
        }
        mStreeView.onCreate(bundle);
        if (dialog.isShowing())
            dialog.dismiss();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mStreetViewBundle = outState.getBundle(STREETVIEW_BUNDLE_KEY);
        if (mStreetViewBundle == null) {
            mStreetViewBundle = new Bundle();
            outState.putBundle(STREETVIEW_BUNDLE_KEY, mStreetViewBundle);
        }

        mStreeView.onSaveInstanceState(mStreetViewBundle);
    }
    @Override
    protected void onResume() {
        mStreeView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mStreeView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mStreeView.onDestroy();
        super.onDestroy();
    }
}
