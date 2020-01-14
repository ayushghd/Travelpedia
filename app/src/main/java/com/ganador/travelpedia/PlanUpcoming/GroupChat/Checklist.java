package com.ganador.travelpedia.PlanUpcoming.GroupChat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ganador.travelpedia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Checklist extends AppCompatActivity {

    private String prevList, tripId;
    private EditText checkList;
    private Button save;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        prevList = getIntent().getStringExtra("checklist");
        tripId = getIntent().getStringExtra("tripId");
        save = findViewById(R.id.saveList);
        checkList = findViewById(R.id.checkList);
        checkList.setText(prevList);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newList = checkList.getText().toString();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("PlanTrip").child(tripId);
                databaseReference.child("Checklist").setValue(newList).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Checklist.this, "Checklist updates!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Checklist.this, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
