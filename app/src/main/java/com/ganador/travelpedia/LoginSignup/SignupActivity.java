package com.ganador.travelpedia.LoginSignup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ganador.travelpedia.Homepage;
import com.ganador.travelpedia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputName, inputNumber;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference firebasedatabase;
    private CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        firebasedatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        inputName = (EditText) findViewById(R.id.name);
        inputNumber = (EditText) findViewById(R.id.number);
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        cb5 = findViewById(R.id.cb5);
        cb6 = findViewById(R.id.cb6);
        cb7 = findViewById(R.id.cb7);
        cb8 = findViewById(R.id.cb8);
        cb9 = findViewById(R.id.cb9);


        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog = new ProgressDialog(SignupActivity.this);
                dialog.setMessage("Please wait...");
                dialog.show();

                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                if (dialog.isShowing())
                                    dialog.dismiss();
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    StringBuilder s = new StringBuilder("000000000000000");
                                    if(cb1.isChecked())
                                        s.setCharAt(1,'1');
                                    if(cb2.isChecked())
                                        s.setCharAt(2,'1');
                                    if(cb3.isChecked())
                                        s.setCharAt(4,'1');
                                    if(cb4.isChecked())
                                        s.setCharAt(9,'1');
                                    if(cb5.isChecked())
                                        s.setCharAt(3,'1');
                                    if(cb6.isChecked())
                                        s.setCharAt(12,'1');
                                    if(cb7.isChecked())
                                        s.setCharAt(7,'1');
                                    if(cb8.isChecked())
                                        s.setCharAt(6,'1');
                                    if(cb9.isChecked())
                                        s.setCharAt(0,'0');


                                    firebasedatabase.child(auth.getCurrentUser().getUid()).child("basic").child("email").setValue(email);
                                    firebasedatabase.child(auth.getCurrentUser().getUid()).child("basic").child("interests").setValue(s.toString());
                                    firebasedatabase.child(auth.getCurrentUser().getUid()).child("basic").child("pwd").setValue(password);
                                    firebasedatabase.child(auth.getCurrentUser().getUid()).child("basic").child("name").setValue(inputName.getText().toString().trim());
                                    firebasedatabase.child(auth.getCurrentUser().getUid()).child("basic").child("number").setValue(inputNumber.getText().toString().trim());
                                    firebasedatabase.child(auth.getCurrentUser().getUid()).child("basic").child("city").setValue("London");

                                    startActivity(new Intent(SignupActivity.this, Homepage.class));

                                    finish();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}