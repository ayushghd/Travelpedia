package com.ganador.travelpedia.PlaceSearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.ganador.travelpedia.AppBarStateChangeListener;
import com.ganador.travelpedia.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    Context context;
    List<NearByPlace> lstplace;
    List<NearByPlace> interestPlace;
    private ProgressDialog dialog;
    String city = "Allahabad";
    ImageView image = null;
    private String callingActivity, intersets;
    private String placeList;
    private CoordinatorLayout coordinatorLayout;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location = null;
    double latitude;
    double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar =  findViewById(R.id.toolbar1);

        placeList="111111111111111";
        city = getIntent().getStringExtra("city");

        if(city.equals("")) {
            placeList = getIntent().getStringExtra("placeList");
            city = "My Location";
            location = getLocation();
            if(location != null){
                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    city = addresses.get(0).getLocality();
                    Log.e("citytytyty4",city);
                }
                catch (Exception e){

                }
            }

        }
        toolbar.setTitle(city);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //   context = getBaseContext();
        image = findViewById(R.id.weather);
        coordinatorLayout =  findViewById(R.id.coordinateLayout);
        AppBarLayout appBarLayout =  findViewById(R.id.appBarLayout);

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if(state.equals(State.COLLAPSED)) {
                    toolbar.setBackgroundResource(R.drawable.custom_toolbar);
                }
                else if (state.equals(State.EXPANDED)) {
                    toolbar.setBackgroundResource(R.color.transparent);
                }
                else if ((state.equals(State.IDLE))){
                }
            }
        });

        lstplace = new ArrayList<>();
        lstplace.add(new NearByPlace("Point Of Interest","all+tourist+spot","Description NearByPlace", R.drawable.pointofinterest));
        lstplace.add(new NearByPlace("Monuments","all+monuments","Description NearByPlace", R.drawable.monuments));
        lstplace.add(new NearByPlace("Temples","all+hindu+temple","Description NearByPlace", R.drawable.temple));
        lstplace.add(new NearByPlace("Mosque","all+mosque","Description NearByPlace", R.drawable.mosque));
        lstplace.add(new NearByPlace("Church","all+church","Description NearByPlace", R.drawable.church));
        lstplace.add(new NearByPlace("ATM","all+atm","Description NearByPlace", R.drawable.atm));
        lstplace.add(new NearByPlace("Museum","all+museum","Description NearByPlace", R.drawable.museum));
        lstplace.add(new NearByPlace("Amusement Park","all+amusement+park","Description NearByPlace", R.drawable.amusementpark));
        lstplace.add(new NearByPlace("Hangout","all+hangout","Description NearByPlace", R.drawable.hangout));
        lstplace.add(new NearByPlace("Restaurants","all+restaurant","Description NearByPlace", R.drawable.restaurant));
        lstplace.add(new NearByPlace("Park","all+park","Description NearByPlace", R.drawable.park));
        lstplace.add(new NearByPlace("Hospitals","all+hospital","Description NearByPlace", R.drawable.hospital));
        lstplace.add(new NearByPlace("Airport","all+airport","Description NearByPlace", R.drawable.airport));
        lstplace.add(new NearByPlace("Bus Station","all+bus+station","Description NearByPlace", R.drawable.busstation));
        lstplace.add(new NearByPlace("Taxi Stand","all+taxi+stand","Description NearByPlace", R.drawable.taxistand));
        interestPlace = new ArrayList<>();
        interestPlace.add(new NearByPlace("Weather","weather","Description NearByPlace", R.drawable.weather_new));
        interestPlace.add(new NearByPlace("Hotels","all+hotel","Description NearByPlace", R.drawable.hotel));
        Log.e("placelistttt",placeList + " aaa");
        if(callingActivity == "Suggestion")
            placeList="111111111111111";
        if(placeList == null)
            placeList = "101010100011111";
        for(int i=0; i <15; i++){
            if(placeList.charAt(i) == '1')
                interestPlace.add(lstplace.get(i));
        }
        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        MainRecyclerView myAdapter = new MainRecyclerView(this,interestPlace,city);
        myrv.setLayoutManager(new GridLayoutManager(this,3));
        myrv.setAdapter(myAdapter);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
           // Toast.makeText(MainActivity.this,"aaaa",Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public Location getLocation() {
        try {
            locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.e("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    } catch (SecurityException e) {
                        Log.e("SecurityException", " 1 " + e.toString());
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        try {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.e("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        } catch (SecurityException e) {
                            Log.e("SecurityException", " 2 " + e.toString());
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
