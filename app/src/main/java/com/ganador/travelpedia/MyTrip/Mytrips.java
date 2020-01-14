package com.ganador.travelpedia.MyTrip;

import android.annotation.SuppressLint;
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
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.ganador.travelpedia.EmergencyFeatures.Emergency;
import com.ganador.travelpedia.HotelBook.HotelBooking;
import com.ganador.travelpedia.PlaceSearch.MainActivity;
import com.ganador.travelpedia.PlaceSearch.PlaceDetails;
import com.ganador.travelpedia.Utility.GetTimeAgo;
import com.ganador.travelpedia.Utility.GridSpacingItemDecoration;
import com.ganador.travelpedia.LoginSignup.LoginActivity;
import com.ganador.travelpedia.Homepage;
import com.ganador.travelpedia.PlanUpcoming.PlanTrip;
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

public class Mytrips extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth fAuth;
    private RecyclerView mNotesList;
    private GridLayoutManager gridLayoutManager;

    private DatabaseReference fNotesDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytrips);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mNotesList = (RecyclerView) findViewById(R.id.notes_list);

        gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        mNotesList.setHasFixedSize(true);
        mNotesList.setLayoutManager(gridLayoutManager);
        //gridLayoutManager.setReverseLayout(true);
        //gridLayoutManager.setStackFromEnd(true);
        mNotesList.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());
        }

        updateUI();

        loadData();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mytrips, menu);
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
        if (id == R.id.main_new_note_btn) {
            Intent newIntent = new Intent(Mytrips.this, MyTripNewNote.class);
            startActivity(newIntent);
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
                    Intent intent = new Intent(Mytrips.this,PlaceDetails.class);
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

        if (id == R.id.home){
            Intent intent = new Intent(this,  Homepage.class);
            startActivity(intent);
        }
        else  if (id == R.id.search) {
            searchGlobal();
        } else if (id == R.id.my_trips) {


        } else if (id == R.id.upcoming_trips) {
            Intent intent = new Intent(this,  PlanTrip.class);
            startActivity(intent);
        } else if (id == R.id.my_profile) {
            Intent intent = new Intent(this,  Profile.class);
            startActivity(intent);
        } else if (id == R.id.nav_review) {
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

    private void loadData() {
        Query query = fNotesDatabase.orderByValue();
        FirebaseRecyclerAdapter<NoteModel, NoteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NoteModel, NoteViewHolder>(

                NoteModel.class,
                R.layout.single_note_layout,
                NoteViewHolder.class,
                query

        ) {
            @Override
            protected void populateViewHolder(final NoteViewHolder viewHolder, NoteModel model, int position) {
                final String noteId = getRef(position).getKey();

                fNotesDatabase.child(noteId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("title") && dataSnapshot.hasChild("timestamp")) {
                            String title = dataSnapshot.child("title").getValue().toString();
                            String timestamp = dataSnapshot.child("timestamp").getValue().toString();
                            String startDate = dataSnapshot.child("start").getValue().toString();
                            String content = dataSnapshot.child("content").getValue().toString();
                            String cost = dataSnapshot.child("cost").getValue().toString();
                            String url1="No image", url2="No image";
                            if (dataSnapshot.hasChild("images") && dataSnapshot.child("images").hasChild("images1")){
                                url1 = dataSnapshot.child("images").child("images1").child("imageUrl").getValue().toString();
                            }
                            if (dataSnapshot.hasChild("images") && dataSnapshot.child("images").hasChild("images2")){
                                url2 = dataSnapshot.child("images").child("images2").child("imageUrl").getValue().toString();
                            }

                            final String send = "City Visited -> "+title+"\nDate of Visit -> "+startDate+"\nExpenditure -> Rs. "+ cost +"\nImage 1 ->"+url1+"\nImage 2 ->"+url2+"\nExperience -> "+content+"\n---------------\nExperience shared by "
                                    +FirebaseAuth.getInstance().getCurrentUser().getEmail()+" via TRAVELPEDIA";

                            viewHolder.setNoteTitle(title);
                            //viewHolder.setNoteTime(timestamp);

                            GetTimeAgo getTimeAgo = new GetTimeAgo();
                            viewHolder.setNoteTime(getTimeAgo.getTimeAgo(Long.parseLong(timestamp), getApplicationContext()));
                            viewHolder.setNoteDate(startDate);

                            viewHolder.noteCard.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                    shareIntent.setType("text/plain");
                                    shareIntent.putExtra(Intent.EXTRA_TEXT,send);
                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "The title");
                                    startActivity(Intent.createChooser(shareIntent, "Share your experience"));
                                    return false;
                                }
                            });
                            viewHolder.noteCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Mytrips.this, MyTripNewNote.class);
                                    intent.putExtra("noteId", noteId);
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
        mNotesList.setAdapter(firebaseRecyclerAdapter);
    }

    private void updateUI(){


    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
