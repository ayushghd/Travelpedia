package com.ganador.travelpedia.PlanUpcoming;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.ganador.travelpedia.EmergencyFeatures.Emergency;
import com.ganador.travelpedia.HotelBook.HotelBooking;
import com.ganador.travelpedia.PlaceSearch.MainActivity;
import com.ganador.travelpedia.PlaceSearch.PlaceDetails;
import com.ganador.travelpedia.Utility.GridSpacingItemDecoration;
import com.ganador.travelpedia.LoginSignup.LoginActivity;
import com.ganador.travelpedia.Homepage;
import com.ganador.travelpedia.MyTrip.Mytrips;
import com.ganador.travelpedia.PlanUpcoming.addMembers.AddMembers;
import com.ganador.travelpedia.Profile;
import com.ganador.travelpedia.R;
import com.ganador.travelpedia.Review.ReviewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ganador.travelpedia.PlanUpcoming.GroupChat.groupChat;
public class PlanTrip extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String uid;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView tripList;
    private GridLayoutManager gridLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlanTrip.this, AddMembers.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();
        tripList = findViewById(R.id.trip_list);
        gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);

        tripList.setHasFixedSize(true);
        tripList.setLayoutManager(gridLayoutManager);
        tripList.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));

        loadData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.plan_trip, menu);
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

        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("All")
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
                    Intent intent = new Intent(PlanTrip.this,PlaceDetails.class);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.home) {
            Intent intent = new Intent(this,  Homepage.class);
            startActivity(intent);
        }
        else if (id == R.id.search) {
            searchGlobal();
        }
        else if (id == R.id.my_trips) {
            Intent intent = new Intent(this,  Mytrips.class);
            startActivity(intent);
        } else if (id == R.id.upcoming_trips) {

        } else if (id == R.id.my_profile) {
            Intent intent = new Intent(this,  Profile.class);
            startActivity(intent);
        }  else if (id == R.id.nav_review) {
            Intent intent = new Intent(this, ReviewActivity.class);
            startActivity(intent);
        }else if (id == R.id.hotelBook) {
            Intent intent = new Intent(this, HotelBooking.class);
            startActivity(intent);
        }else if (id == R.id.emergency) {
            Intent intent = new Intent(this, Emergency.class);
            startActivity(intent);
        } else if (id == R.id.suggestion) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("city","");
            intent.putExtra("placeList", Homepage.interests);
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

    private void loadData(){
        Query query = databaseReference.child("Users").child(uid).child("Trips").orderByValue();
        FirebaseRecyclerAdapter<TripModel, TripViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TripModel, TripViewHolder>(

                TripModel.class,
                R.layout.single_usertrip_layout,
                TripViewHolder.class,
                query

        ) {
            @Override
            protected void populateViewHolder(final TripViewHolder viewHolder, TripModel model, int position) {
                final String noteId = getRef(position).getKey();
                databaseReference.child("Users").child(uid).child("Trips").child(noteId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("Members")) {
                            final String tripName = dataSnapshot.child("TripName").getValue().toString();
                            final String members = dataSnapshot.child("Members").getValue().toString();
                            final String budget = dataSnapshot.child("Budget").getValue().toString();
                            final String date = dataSnapshot.child("StartDate").getValue().toString();

                            viewHolder.setTripName(tripName);
                            viewHolder.setTripMembers("Members = "+members);
                            viewHolder.setTripDate(date);
                            viewHolder.setTripBudget("Budget = Rs."+budget);

                            viewHolder.tripCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(PlanTrip.this, groupChat.class);
                                    i.putExtra("tripId", noteId);
                                    startActivity(i);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        tripList.setAdapter(firebaseRecyclerAdapter);
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
