package com.ganador.travelpedia.PlanUpcoming.GroupChat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.ganador.travelpedia.LoginSignup.LoginActivity;
import com.ganador.travelpedia.MyTrip.MyTripNewNote;
import com.ganador.travelpedia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class groupChat extends AppCompatActivity {
    private static final int SIGN_IN_REQUEST_CODE = 111;
    private FirebaseListAdapter<ChatMessage> adapter;
    private ListView listView;
    private String loggedInUserName = "";
    private String tripId, uid, checkList;
    private DatabaseReference databaseReference;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final EditText input = (EditText) findViewById(R.id.input);
        listView = (ListView) findViewById(R.id.list_of_messages);
        tripId = getIntent().getStringExtra("tripId");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        displayChatMessages();
        getCheckList();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input.getText().toString().trim().equals("")) {
                    Toast.makeText(groupChat.this, "Please enter some texts!", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("PlanTrip").child(tripId).child("Messages")
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                    FirebaseAuth.getInstance().getCurrentUser().getUid())
                            );
                    input.setText("");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.delete_group){
            dialog = new ProgressDialog(groupChat.this);
            dialog.setMessage("Please wait...");
            dialog.show();
            deleteTrip();
        }
        else if(id == R.id.check_list){
            Intent i = new Intent(groupChat.this, Checklist.class);
            i.putExtra("tripId", tripId);
            i.putExtra("checklist", checkList);
            startActivity(i);
        }
        else if(id == R.id.change_budget){
//            AlertDialog.Builder alert = new AlertDialog.Builder(groupChat.this);
//            final EditText edittext = new EditText(groupChat.this);
//            alert.setMessage("Change Bugdet");
//
//            alert.setView(edittext);
//
//            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    //What ever you want to do with the value
//                    String YouEditTextValue = edittext.getText().toString();
//                }
//            });
//
//            alert.show();
        }
        else{

        }
        return true;
    }


    public String getLoggedInUserName() {
        return loggedInUserName;
    }

    private void deleteTrip(){
        databaseReference.child("Users").child(uid).child("Trips").child(tripId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (dialog.isShowing())
                    dialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(groupChat.this, "Trip Deleted", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e("MyTripNewNote", task.getException().toString());
                    Toast.makeText(groupChat.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void displayChatMessages(){
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference().child("PlanTrip").child(tripId).child("Messages").getRef()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    private void getCheckList(){
        FirebaseDatabase.getInstance().getReference().child("PlanTrip").child(tripId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Checklist")) {
                    checkList = dataSnapshot.child("Checklist").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
