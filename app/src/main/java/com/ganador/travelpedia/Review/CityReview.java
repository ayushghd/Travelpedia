package com.ganador.travelpedia.Review;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.ganador.travelpedia.LoginSignup.LoginActivity;
import com.ganador.travelpedia.Utility.GridSpacingItemDecoration;
import com.ganador.travelpedia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class CityReview extends AppCompatActivity
{

    private FirebaseAuth fAuth;
    private RecyclerView mReviewsList;
    private GridLayoutManager gridLayoutManager;
    private Button submit;
    private RatingBar ratingBar;
    private EditText review;
    private DatabaseReference fNotesDatabase;
    private ProgressDialog dialog;
    Float Rating=0f;
    int flag = 0;
    private String noteID, userID, noteID2, prevCount, prevAverage;
    private boolean isExist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_review);

        mReviewsList = (RecyclerView) findViewById(R.id.user_reviews_list);

        gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);

        mReviewsList.setHasFixedSize(true);
        mReviewsList.setLayoutManager(gridLayoutManager);
        //gridLayoutManager.setReverseLayout(true);
        //gridLayoutManager.setStackFromEnd(true);
        mReviewsList.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));

        try {
            noteID = getIntent().getStringExtra("noteId");
            noteID2 = getIntent().getStringExtra("noteId2");
            prevCount = getIntent().getStringExtra("count");
            prevAverage = getIntent().getStringExtra("average");

            //Toast.makeText(this, noteID, Toast.LENGTH_SHORT).show();

            if (!noteID.trim().equals("")) {
                isExist = true;
            } else {
                isExist = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        dialog = new ProgressDialog(CityReview.this);
        dialog.setMessage("Please wait...");
        dialog.show();
        submit = findViewById(R.id.submit);
        ratingBar = findViewById(R.id.rating_bar);
        review = findViewById(R.id.review);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mReview = review.getText().toString();
                String mRating = Float.toString(ratingBar.getRating());
                Log.e(Rating.toString(), prevAverage);
                if (!TextUtils.isEmpty(mReview) && !TextUtils.isEmpty(mRating)) {
                    createNote(mReview, mRating);
                } else {
                    Snackbar.make(v, "Fill empty fields", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        if (noteID2.equals("")) {
            fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Reviews").child(noteID);
        }
        else
            fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Reviews").push();

        updateUI();

        loadData();

        putData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void putData() {

        if (isExist) {
            fNotesDatabase.child("rvs").child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("rating")) {
                        Rating = Float.parseFloat(dataSnapshot.child("rating").getValue().toString());
                        flag = 1;
                        //prevAverage=fNotesDatabase.child("average").getValue().toString();
                        String mReview = dataSnapshot.child("review").getValue().toString();
                        ratingBar.setRating(Rating);
                        review.setText(mReview);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            fNotesDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("average")) {
                        prevAverage = dataSnapshot.child("average").getValue().toString();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void createNote(String mReview, String mRating) {

        if (fAuth.getCurrentUser() != null) {

            if (isExist) {
                Map updateMap = new HashMap();
                final Map map2 = new HashMap();

                final String newCount = Integer.toString(1+ Integer.parseInt(prevCount));


                //Log.e("dsdss", fNotesDatabase.child(userID).getKey());
                String newAverage = Float.toString(Float.parseFloat(mRating)+ Float.parseFloat(prevAverage)-Rating);

                map2.put("average", newAverage.trim());
                if(flag==0)
                    map2.put("count", newCount.trim());
                updateMap.put("name", fAuth.getCurrentUser().getEmail());
                updateMap.put("rating", Float.toString(ratingBar.getRating()));
                updateMap.put("review", review.getText().toString().trim());
                fNotesDatabase.child("rvs").child(userID).updateChildren(updateMap);
                fNotesDatabase.updateChildren(map2);

                Toast.makeText(this, "Review updated", Toast.LENGTH_SHORT).show();
            } else {
                // CREATE A NEW NOTE
                final DatabaseReference newNoteRef = fNotesDatabase.child("rvs").child(userID);//, newNoteRef2 = fNotesDatabase;

                final Map noteMap = new HashMap(), map2 = new HashMap();
                String newCount = Integer.toString(1+ Integer.parseInt(prevCount));
                String newAverage = Float.toString(Float.parseFloat(mRating)+ Float.parseFloat(prevAverage));

                map2.put("city", noteID2.trim());
                map2.put("count",newCount.trim());
                map2.put("average",newAverage.trim());
                noteMap.put("name", fAuth.getCurrentUser().getEmail());
                noteMap.put("rating", mRating);
                noteMap.put("review", mReview);
                Thread mainThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        newNoteRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CityReview.this, "Review added", Toast.LENGTH_SHORT).show();
                                    final DatabaseReference newNoteRef2 = fNotesDatabase.getRef();
                                    Thread mainThread2 = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            newNoteRef2.updateChildren(map2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CityReview.this, "New City added", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(CityReview.this, ReviewActivity.class);
                                                        startActivity(i);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(CityReview.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });
                                        }
                                    });
                                    mainThread2.start();
                                } else {
                                    Toast.makeText(CityReview.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });

                mainThread.start();
                //mainThread2.start();
            }



        } else {
            Toast.makeText(this, "USERS IS NOT SIGNED IN", Toast.LENGTH_SHORT).show();
        }

    }
    private void loadData() {
        Query query = fNotesDatabase.child("rvs").orderByValue();
        FirebaseRecyclerAdapter<UserReviewModel, UserReviewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserReviewModel, UserReviewHolder>(

                UserReviewModel.class,
                R.layout.single_userreview_layout,
                UserReviewHolder.class,
                query

        ) {
            @Override
            protected void populateViewHolder(final UserReviewHolder viewHolder, UserReviewModel model, int position) {
                final String noteId = getRef(position).getKey();
                Log.e("noteid", noteId);
                fNotesDatabase.child("rvs").child(noteId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("review")) {
                            Log.e("inside ", " reviews ");
                            String name = dataSnapshot.child("name").getValue().toString();
                            String rating = dataSnapshot.child("rating").getValue().toString();
                            String review = dataSnapshot.child("review").getValue().toString();
                            viewHolder.setName(name);
                            viewHolder.setRating(Float.parseFloat(rating));
                            viewHolder.setReview(review);
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