package com.example.youseeeventsv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.youseeeventsv1.MainActivity;
import com.example.youseeeventsv1.R;
import com.example.youseeeventsv1.SignUpActivity;
import com.example.youseeeventsv1.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText inputEmailUser, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    String emailUser, password, username;
    //Check to see if the input is a username or email
    boolean isEmail = false;
    DatabaseReference mDatabase;
    User user;
    boolean orgCheck;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmailUser = (EditText) findViewById(R.id.user_email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.youseeeventsv1.LoginActivity.this, SignUpActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailUser = inputEmailUser.getText().toString().trim();
                password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(emailUser)) {
                    Toast.makeText(getApplicationContext(), "Enter username or email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if( !emailUser.contains("@")) {
                    // Checks if a username is already taken
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Users");
                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (!snapshot.hasChild(emailUser)) {
                                Toast.makeText(getApplicationContext(), "Username does not exist! Please try again.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else{
                                username = emailUser;
                                emailUser = snapshot.child(emailUser).child("email").getValue(String.class).trim();
                                signIn( emailUser, password );
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    return;
                }
                else {
                    signIn( emailUser, password );
                }


            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });
    }

    private void signIn(String user_email, String password ) {
        progressBar.setVisibility(View.VISIBLE);
        //Sign in user
        auth.signInWithEmailAndPassword(emailUser, password)
                .addOnCompleteListener(com.example.youseeeventsv1.LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        /*if (!task.isSuccessful()) {
                            Toast.makeText(com.example.youseeeventsv1.LoginActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }*/
                        if( task.isSuccessful()) {
                            startActivity(new Intent(com.example.youseeeventsv1.LoginActivity.this, MainActivity.class));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(LoginActivity.this, "Incorrect password, please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
        });
    }

    public void setEmail( String email ) {
        emailUser = email;
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}