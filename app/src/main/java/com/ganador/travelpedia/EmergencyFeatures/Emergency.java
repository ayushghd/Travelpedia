package com.ganador.travelpedia.EmergencyFeatures;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import com.ganador.travelpedia.PlaceSearch.PlaceDetails;
import com.ganador.travelpedia.R;

@SuppressWarnings("all")
public class Emergency extends AppCompatActivity implements LocationListener {
    private CardView emergency1,emergency2,emergency3,emergency4,emergency5,emergency6;
    private String result,url;
    private Location location;
    private String country;
    private String cityName;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    double latitude;
    double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    private LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        emergency1 = findViewById(R.id.emergencyCard1);
        emergency2 = findViewById(R.id.emergencyCard2);
        emergency3 = findViewById(R.id.emergencyCard3);
        emergency4 = findViewById(R.id.emergencyCard4);
        emergency5 = findViewById(R.id.emergencyCard5);
        emergency6 = findViewById(R.id.emergencyCard6);
        location = getLocation();
        cityName = "Allahabad";
        country = "India";
        latitude = 25.496115482282974;
        longitude = 81.86855681240559;
        if(location != null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        result = "https://maps.googleapis.com/maps/api/place/textsearch/json?location=";
        result = result + latitude + "," + longitude;
        emergency1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = result + "&radius=5000&query=hospitals&sensor=true&key=AIzaSyBetBer1jCeuflwEc_EnbxUnFiWFabxJZg";
                Intent intent = new Intent(Emergency.this,PlaceDetails.class);
                intent.putExtra("Title","Nearest Hospital");
                intent.putExtra("City","Emergency");
                intent.putExtra("Category",url);
                intent.putExtra("Description","Global Search");
                startActivity(intent);
            }
        });
        emergency2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currencyConverter();
            }
        });
        emergency3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:77706959699"));
                startActivity(intent);
            }
        });
        emergency4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:77706959699"));
                startActivity(intent);
            }
        });
        emergency5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:77706959699"));
                startActivity(intent);
            }
        });
        emergency6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = result + "&radius=5000&query=washroom&sensor=true&key=AIzaSyBetBer1jCeuflwEc_EnbxUnFiWFabxJZg";
                Intent intent = new Intent(Emergency.this,PlaceDetails.class);
                intent.putExtra("Title","Nearest WashRoom");
                intent.putExtra("City","Emergency");
                intent.putExtra("Category",url);
                intent.putExtra("Description","Global Search");
                startActivity(intent);
            }
        });
    }
    private void currencyConverter() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Currency Converter");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setMaxLines(1);
        input.setSingleLine(true);
        input.setHint("Rupee to Dollar");
        alert.setView(input, 32, 0, 32, 0);
        alert.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String result = input.getText().toString();
                if (!result.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY,result + " convert");
                    startActivity(intent);
                }
            }
        });
        alert.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled
            }
        });
        alert.show();
    }
    public Location getLocation() {
        try {
            locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                    } catch (SecurityException e) {
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        try {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            }
                        } catch (SecurityException e) {
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
