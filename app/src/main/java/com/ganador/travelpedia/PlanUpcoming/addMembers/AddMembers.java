package com.ganador.travelpedia.PlanUpcoming.addMembers;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.ganador.travelpedia.Utility.GridSpacingItemDecoration;
import com.ganador.travelpedia.Homepage;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class AddMembers extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Button createGroup;
    private EditText tripName, tripBudget;
    private TextView tripStartDate;
    private DatePickerDialog mDatePickerDialog;
    private ArrayList<String> groupUsers, uids;
    private DatabaseReference databaseReference, ref2;
    private FirebaseAuth firebaseAuth;
    private RecyclerView membersList;
    private GridLayoutManager gridLayoutManager;
    private String uid;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();
        groupUsers = new ArrayList<>();
        uids = new ArrayList<>();
        membersList = findViewById(R.id.members_list);
        createGroup = findViewById(R.id.create_group);
        tripName = findViewById(R.id.tripName);
        tripStartDate = findViewById(R.id.trip_date);
        final Calendar calendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(
                AddMembers.this,
                AddMembers.this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        tripStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
            }
        });
        tripBudget = findViewById(R.id.trip_budget);

        gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);

        membersList.setHasFixedSize(true);
        membersList.setLayoutManager(gridLayoutManager);
        //gridLayoutManager.setReverseLayout(true);
        //gridLayoutManager.setStackFromEnd(true);
        membersList.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));

        loadData();
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tripName.getText().length() > 0 && tripBudget.getText().length() > 0) {
                    dialog = new ProgressDialog(AddMembers.this);
                    dialog.setMessage("Please wait...");
                    dialog.show();
                    ref2 = databaseReference.child("Users").child(uid).push();
                    final String k = ref2.getKey();
                    final Map map = new HashMap();
                    map.put("id", k);
//                    Thread mainThread = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            ref2.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(AddMembers.this, "New Trip added", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(AddMembers.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//
//                                }
//                            });
//                        }
//                    });
//                    mainThread.start();
                    final Map map2 = new HashMap();

                    map2.put("TripName", tripName.getText().toString());
                    map2.put("Members", Integer.toString(groupUsers.size()));
                    map.put("TripName", tripName.getText().toString());
                    map.put("Members", Integer.toString(groupUsers.size()));
                    map.put("Budget", tripBudget.getText().toString().trim());
                    map.put("StartDate", tripStartDate.getText().toString().trim());

                    for (int i = 0; i < groupUsers.size(); i++) {
                        map2.put("id" + i, groupUsers.get(i).toString());
                    }

                    for (int i = 0; i < groupUsers.size(); i++) {
                        final int j = i;
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final int ii = j;
                                databaseReference.child("Users").child(uids.get(ii)).child("Trips").child(k).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }
                        });
                        t.start();
                        if (i == groupUsers.size() - 1) {
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    databaseReference.child("PlanTrip").child(k).updateChildren(map2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (dialog.isShowing())
                                                dialog.dismiss();
                                            if (task.isSuccessful()) {
                                                Intent i = new Intent(AddMembers.this, Homepage.class);
                                                startActivity(i);
                                                finish();
                                            } else {
                                                Toast.makeText(AddMembers.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                            thread.start();
                        }
                    }
                }

            }
        });
    }

    private void loadData(){
        Query query = databaseReference.child("Users").orderByValue();
        FirebaseRecyclerAdapter<MemberModel, MemberModelHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MemberModel, MemberModelHolder>(

                MemberModel.class,
                R.layout.single_available_users_layout,
                MemberModelHolder.class,
                query

        ) {
            @Override
            protected void populateViewHolder(final MemberModelHolder viewHolder, MemberModel model, int position) {
                final String noteId = getRef(position).getKey();
                databaseReference.child("Users").child(noteId).child("basic").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("email")) {

                            final String email = dataSnapshot.child("email").getValue().toString();
//                            if (noteId.equals(uid)){
//                                groupUsers.add(email);
//                            }
//                            else {
                                viewHolder.setMemberId(email);
                                Log.e("id", email);
                                viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (viewHolder.checkBox.isChecked()) {
                                            groupUsers.add(email);
                                            uids.add(noteId);
                                        } else {
                                            if (groupUsers.contains(email)) {
                                                groupUsers.remove(email);
                                                uids.remove(noteId);
                                            }
                                        }
                                    }
                                });
                            //}
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        membersList.setAdapter(firebaseRecyclerAdapter);
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        tripStartDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
    }
}
