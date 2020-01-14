package com.ganador.travelpedia;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ganador.travelpedia.EmergencyFeatures.Emergency;
import com.ganador.travelpedia.HotelBook.HotelBooking;
import com.ganador.travelpedia.LoginSignup.LoginActivity;
import com.ganador.travelpedia.MyTrip.Mytrips;
import com.ganador.travelpedia.PlaceSearch.MainActivity;
import com.ganador.travelpedia.PlanUpcoming.PlanTrip;
import com.ganador.travelpedia.Review.ReviewActivity;
import com.ganador.travelpedia.Service.NotificationService;
import com.ganador.travelpedia.Utility.ImageModel;
import com.ganador.travelpedia.Utility.SlidingImages_Adapter;
import com.ganador.travelpedia.PlaceSearch.PlaceDetails;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Homepage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LocationListener,View.OnClickListener {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;

    private int[] myImageList = new int[]{R.drawable.cityback, R.drawable.city2,
            R.drawable.city3,R.drawable.city4
            ,R.drawable.city5};

    private ProgressDialog dialog;
    private String currentCityName = "Allahabad", firebaseCityName;
    private Button search;
    private EditText city;
    private ImageView profilepic;
    public String userID, cityName;
    public static String interests;
    public String UserName;
    public TextView mID, mID2;
    public FirebaseAuth auth;
    private String myCity;
    private TextView textVIew1,textView2,textView3,textView4;
    private Button searchTopPlace;
    private Button searchTopTourist;
    private Button searchTopMonuments;
    private Button searchTopBuildings;
    private ImageView city1,city2,city3,city4,city5,city6,city7,city8;
    private Location location;
    private String country;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    double latitude;
    double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    protected LocationManager locationManager;
    public DatabaseReference mDatabaseReference;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        checkAndRequestPermissions();
        dialog = new ProgressDialog(this);
        search = findViewById(R.id.searchbt);
        searchTopPlace = findViewById(R.id.searchTopPlace);
        searchTopTourist = findViewById(R.id.searchTopTourist);
        searchTopMonuments = findViewById(R.id.searchTopMonuments);
        searchTopBuildings = findViewById(R.id.searchTopBuildings);
        city1 = findViewById(R.id.paris1);
        city2 = findViewById(R.id.sydney1);
        city3 = findViewById(R.id.newdelhi1);
        city4 = findViewById(R.id.newyorkcity1);
        city5 = findViewById(R.id.prague1);
        city6 = findViewById(R.id.dubai1);
        city7 = findViewById(R.id.rome1);
        city8 = findViewById(R.id.london1);
        city = findViewById(R.id.search);
        textVIew1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        location = getLocation();
        cityName = "Allahabad";
        country = "India";
        if(location != null){
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                country = addresses.get(0).getCountryName();
                cityName = addresses.get(0).getLocality();
            }
            catch (Exception e){

            }
        }
        textVIew1.setText("TOP PLACES TO VISIT IN " + cityName.toUpperCase());
        textView2.setText("TOP TOURIST SPOTS IN " + country.toUpperCase());
        textView3.setText("TOP MONUMENTS IN " + country.toUpperCase());
        textView4.setText("TOP BUILDINGS IN " + country.toUpperCase());
        Intent serviceIntent = new Intent(getBaseContext(), NotificationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(serviceIntent);
        }
        else
            this.startService(serviceIntent);
        search.setOnClickListener(this);
        searchTopPlace.setOnClickListener(this);
        searchTopTourist.setOnClickListener(this);
        searchTopMonuments.setOnClickListener(this);
        searchTopBuildings.setOnClickListener(this);
        city1.setOnClickListener(this);
        city2.setOnClickListener(this);
        city3.setOnClickListener(this);
        city4.setOnClickListener(this);
        city5.setOnClickListener(this);
        city6.setOnClickListener(this);
        city7.setOnClickListener(this);
        city8.setOnClickListener(this);
        userID= FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        auth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mID2   = (TextView)navigationView.getHeaderView(0).findViewById(R.id.userName);

        mDatabaseReference.child(auth.getCurrentUser().getUid()).child("basic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("sss",dataSnapshot.child("name").getValue().toString());
                UserName = dataSnapshot.child("name").getValue().toString();
                interests = dataSnapshot.child("interests").getValue().toString();
                firebaseCityName = "london";
                mID2.setText(UserName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mID   = (TextView)navigationView.getHeaderView(0).findViewById(R.id.userID);
        mID.setText(userID);


        profilepic = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.profilepic);
//        Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString()).into(profilepic);

        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

        init();
    }

    private  boolean checkAndRequestPermissions() {
        int contact = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int call = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (contact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (call != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }

    private void init() {

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImages_Adapter(Homepage.this,imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_search1) {
            searchCities();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void searchCities() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this);
            Log.e("insidetry","insidetry");
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e("firstcatch","first");
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        }catch (GooglePlayServicesNotAvailableException e) {
            Log.e("secondcatch","second");
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("all")
    private void searchGlobal() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Search For Places");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setMaxLines(1);
        input.setSingleLine(true);
        alert.setView(input, 32, 0, 32, 0);
        alert.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String result = input.getText().toString();
                if (!result.isEmpty()) {
                    Intent intent = new Intent(Homepage.this,PlaceDetails.class);
                    intent.putExtra("Title",result);
                    intent.putExtra("City","");
                    intent.putExtra("Category",result);
                    intent.putExtra("Description","Global Search");
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("Place Selected: " ,place.getName().toString());
                Intent intent = new Intent(Homepage.this, MainActivity.class);
                intent.putExtra("city",place.getName().toString());
                intent.putExtra("placeList","MainActivity");
                startActivity(intent);
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    Log.e("error",Html.fromHtml(attributions.toString()).toString());
                } else {

                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e("Error: Status = " ,status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home){

        }
        else if (id == R.id.search) {
            searchGlobal();
        } else if (id == R.id.my_trips) {
            Intent intent = new Intent(this,  Mytrips.class);
            startActivity(intent);
        } else if (id == R.id.upcoming_trips) {
            Intent intent = new Intent(this,  PlanTrip.class);
            startActivity(intent);
        }else if (id == R.id.my_profile) {
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
        }else if (id == R.id.suggestion) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("city","");
            intent.putExtra("placeList",interests);
            startActivity(intent);
        }else if (id == R.id.hotelBook) {
           Intent intent = new Intent(this, HotelBooking.class);
           startActivity(intent);
        }else if (id == R.id.emergency) {
           Intent intent = new Intent(this, Emergency.class);
           startActivity(intent);
        } else if (id == R.id.share) {

        } else if (id == R.id.nav_review) {
            Intent intent = new Intent(this, ReviewActivity.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.paris1:
                Intent intent = new Intent(Homepage.this, MainActivity.class);
                intent.putExtra("city","Paris");
                intent.putExtra("placeList","MainActivity");
                startActivity(intent);
                break;
            case R.id.sydney1:
                intent = new Intent(Homepage.this, MainActivity.class);
                intent.putExtra("city","Sydney");
                intent.putExtra("placeList","MainActivity");
                startActivity(intent);
                break;
            case R.id.newdelhi1:
                intent = new Intent(Homepage.this, MainActivity.class);
                intent.putExtra("city","New Delhi");
                intent.putExtra("placeList","MainActivity");
                startActivity(intent);
                break;
            case R.id.newyorkcity1:
                intent = new Intent(Homepage.this, MainActivity.class);
                intent.putExtra("city","New York City");
                intent.putExtra("placeList","MainActivity");
                startActivity(intent);
                break;
            case R.id.prague1:
                intent = new Intent(Homepage.this, MainActivity.class);
                intent.putExtra("city","Prague");
                intent.putExtra("placeList","MainActivity");
                startActivity(intent);
                break;
            case R.id.dubai1:
                intent = new Intent(Homepage.this, MainActivity.class);
                intent.putExtra("city","Dubai");
                intent.putExtra("placeList","MainActivity");
                startActivity(intent);
                break;
            case R.id.rome1:
                intent = new Intent(Homepage.this, MainActivity.class);
                intent.putExtra("city","Rome");
                intent.putExtra("placeList","MainActivity");
                startActivity(intent);
                break;
            case R.id.london1:
                intent = new Intent(Homepage.this, MainActivity.class);
                intent.putExtra("city","London");
                intent.putExtra("placeList","MainActivity");
                startActivity(intent);
                break;
            case R.id.searchbt:
                dialog.setMessage("Please wait...");
                dialog.show();
                intent = new Intent(Homepage.this,PlaceDetails.class);
                intent.putExtra("Title",city.getText().toString());
                intent.putExtra("City","");
                intent.putExtra("Category",city.getText().toString());
                intent.putExtra("Description","Global Search");
                startActivity(intent);
                if (dialog.isShowing())
                    dialog.dismiss();
                break;
            case R.id.searchTopPlace:
                String result="top places to visit in ";
                result = result + cityName;
                intent = new Intent(Homepage.this,PlaceDetails.class);
                intent.putExtra("Title","Top Places to Visit In " + cityName);
                intent.putExtra("City","");
                intent.putExtra("Category",result);
                intent.putExtra("Description","Global Search");
                startActivity(intent);
                break;
            case R.id.searchTopTourist:
                result="top tourist spots in ";
                result = result + country;
                intent = new Intent(Homepage.this,PlaceDetails.class);
                intent.putExtra("Title","Top Tourist Spots In " + country);
                intent.putExtra("City","");
                intent.putExtra("Category",result);
                intent.putExtra("Description","Global Search");
                startActivity(intent);
                break;
            case R.id.searchTopMonuments:
                result="top monuments in ";
                result = result + country;
                intent = new Intent(Homepage.this,PlaceDetails.class);
                intent.putExtra("Title","Top Monuments In " + country);
                intent.putExtra("City","");
                intent.putExtra("Category",result);
                intent.putExtra("Description","Global Search");
                startActivity(intent);
                break;
            case R.id.searchTopBuildings:
                result="top buildings in ";
                result = result + country;
                intent = new Intent(Homepage.this,PlaceDetails.class);
                intent.putExtra("Title","Top Buildings In " + country);
                intent.putExtra("City","");
                intent.putExtra("Category",result);
                intent.putExtra("Description","Global Search");
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
