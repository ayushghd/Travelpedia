package com.ganador.travelpedia.Review;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.ganador.travelpedia.HotelBook.HotelBooking;
import com.ganador.travelpedia.PlaceSearch.MainActivity;
import com.ganador.travelpedia.PlaceSearch.PlaceDetails;
import com.ganador.travelpedia.Utility.GridSpacingItemDecoration;
import com.ganador.travelpedia.LoginSignup.LoginActivity;
import com.ganador.travelpedia.Homepage;
import com.ganador.travelpedia.MyTrip.Mytrips;
import com.ganador.travelpedia.PlanUpcoming.PlanTrip;
import com.ganador.travelpedia.Profile;
import com.ganador.travelpedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<String> cityNames;
    private FirebaseAuth fAuth;
    private RecyclerView mReviewsList;
    private GridLayoutManager gridLayoutManager;
    private ProgressDialog dialog;
    private DatabaseReference fNotesDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dialog = new ProgressDialog(ReviewActivity.this);
        dialog.setMessage("Please wait...");
        dialog.show();
        cityNames = new ArrayList<>();
        mReviewsList = (RecyclerView) findViewById(R.id.reviews_list);

        gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        mReviewsList.setHasFixedSize(true);
        mReviewsList.setLayoutManager(gridLayoutManager);
        //gridLayoutManager.setReverseLayout(true);
        //gridLayoutManager.setStackFromEnd(true);
        mReviewsList.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Reviews");
        }

        updateUI();

        loadData();
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
        getMenuInflater().inflate(R.menu.review, menu);
        return true;
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
                    Intent intent = new Intent(ReviewActivity.this,PlaceDetails.class);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.add_review) {
            AlertDialog.Builder alert = new AlertDialog.Builder(ReviewActivity.this);
            final EditText edittext = new EditText(ReviewActivity.this);
            alert.setMessage("Enter the name of city");

            alert.setView(edittext);

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //What ever you want to do with the value
                    String YouEditTextValue = edittext.getText().toString().toLowerCase();
                    if (cityNames.contains(YouEditTextValue)){
                        Toast.makeText(ReviewActivity.this,"City already has some reviews. Click on appropriate card" +
                                " to proceed further", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent newIntent = new Intent(ReviewActivity.this, CityReview.class);
                        newIntent.putExtra("noteId2", YouEditTextValue);
                        newIntent.putExtra("noteId", "");
                        newIntent.putExtra("count", "0");
                        newIntent.putExtra("average", "0");
                        startActivity(newIntent);
                    }
                }
            });
            alert.show();
        }

        return super.onOptionsItemSelected(item);
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
        } else if (id == R.id.my_trips) {
            Intent intent = new Intent(this,  Mytrips.class);
            startActivity(intent);
        } else if (id == R.id.upcoming_trips) {
            Intent intent = new Intent(this, PlanTrip.class);
            startActivity(intent);
        } else if (id == R.id.hotelBook) {
            Intent intent = new Intent(this, HotelBooking.class);
            startActivity(intent);
        }else if (id == R.id.my_profile) {
            Intent intent = new Intent(this,  Profile.class);
            startActivity(intent);
        } else if (id == R.id.share) {
            Intent intent = new Intent(this, PlanTrip.class);
            startActivity(intent);
        } else if (id == R.id.nav_review) {

        } else if (id == R.id.suggestion) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("city","");
            intent.putExtra("placeList","Suggestion");
            startActivity(intent);
        }  else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadData() {
        Query query = fNotesDatabase.orderByValue();
        FirebaseRecyclerAdapter<ReviewModel, ReviewViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ReviewModel, ReviewViewHolder>(

                ReviewModel.class,
                R.layout.single_cityreview_layout,
                ReviewViewHolder.class,
                query

        ) {
            @Override
            protected void populateViewHolder(final ReviewViewHolder viewHolder, ReviewModel model, int position) {
                final String noteId = getRef(position).getKey();
                Log.e("noteId",noteId);
                fNotesDatabase.child(noteId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e("ddd",dataSnapshot.getKey());
                        if (dataSnapshot.hasChild("city")) {
                            final String city = dataSnapshot.child("city").getValue().toString();
                            final String count = dataSnapshot.child("count").getValue().toString();
                            final String avrating = dataSnapshot.child("average").getValue().toString();
                            float av = Float.parseFloat(avrating)/ Float.parseFloat(count);

                            cityNames.add(city.toLowerCase());
                            viewHolder.setCity("City -> "+city);
                            viewHolder.setAverage("Average Rating -> "+av);
                            viewHolder.setRevCount("Total Reviews -> " + count);
                            viewHolder.setAvRating(Float.toString(av));

                            viewHolder.reviewCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ReviewActivity.this, CityReview.class);
                                    intent.putExtra("noteId", noteId);
                                    intent.putExtra("noteId2","");
                                    intent.putExtra("count",count);
                                    intent.putExtra("average",avrating);

                                    startActivity(intent);
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
        mReviewsList.setAdapter(firebaseRecyclerAdapter);
        if (dialog.isShowing())
            dialog.dismiss();
    }

    private void updateUI(){


    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
