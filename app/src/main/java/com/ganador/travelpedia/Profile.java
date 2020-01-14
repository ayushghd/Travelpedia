package com.ganador.travelpedia;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ganador.travelpedia.HotelBook.HotelBooking;
import com.ganador.travelpedia.LoginSignup.LoginActivity;
import com.ganador.travelpedia.MyTrip.Mytrips;
import com.ganador.travelpedia.PlaceSearch.MainActivity;
import com.ganador.travelpedia.PlaceSearch.PlaceDetails;
import com.ganador.travelpedia.PlanUpcoming.PlanTrip;
import com.ganador.travelpedia.Review.ReviewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView tv_name;
    TextView tv_number;
    TextView tv_email;

    private ImageView profilepic;
    private int PICK_IMAGE_REQUEST = 1;
    public String userID;
    public String UserName;
    public TextView mID, mID2;
    public FirebaseAuth auth;
    public DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_number = (TextView)findViewById(R.id.tv_number);
        tv_email = findViewById(R.id.email);
        profilepic = findViewById(R.id.profile_pic);
        userID= FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        auth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mID2   = (TextView)navigationView.getHeaderView(0).findViewById(R.id.userName);

        mDatabaseReference.child(auth.getCurrentUser().getUid()).child("basic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("sss",dataSnapshot.getValue().toString());
                UserName = dataSnapshot.child("name").getValue().toString();
                mID2.setText(UserName);
                tv_name.setText(UserName);
                tv_number.setText(dataSnapshot.child("number").getValue().toString());
                tv_email.setText(dataSnapshot.child("email").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mID   = (TextView)navigationView.getHeaderView(0).findViewById(R.id.userID);
        mID.setText(userID);

        profilepic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent();
// Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                return false;
            }
        });

        userID= FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        UserName="nnn";
        mID   = (TextView)navigationView.getHeaderView(0).findViewById(R.id.userID);
        mID.setText(userID);
        mID   = (TextView)navigationView.getHeaderView(0).findViewById(R.id.userName);
        mID.setText(UserName);
        profilepic = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.profilepic);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                CircleImageView imageView = findViewById(R.id.profile_pic);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        getMenuInflater().inflate(R.menu.profile, menu);
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
                    Intent intent = new Intent(Profile.this,PlaceDetails.class);
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

        if(id == R.id.home){
            Intent intent = new Intent(this,  Homepage.class);
            startActivity(intent);
        }
        else if (id == R.id.search) {
            searchGlobal();
        } else if (id == R.id.my_trips) {
            Intent intent = new Intent(this,  Mytrips.class);
            startActivity(intent);
        } else if (id == R.id.upcoming_trips) {
            Intent intent = new Intent(this,  PlanTrip.class);
            startActivity(intent);
        } else if (id == R.id.my_profile) {

        } else if (id == R.id.hotelBook) {
            Intent intent = new Intent(this, HotelBooking.class);
            startActivity(intent);
        }else if (id == R.id.nav_review) {
            Intent intent = new Intent(this, ReviewActivity.class);
            startActivity(intent);
        }else if (id == R.id.suggestion) {
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
}
