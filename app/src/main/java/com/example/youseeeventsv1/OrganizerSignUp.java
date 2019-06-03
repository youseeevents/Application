package com.example.youseeeventsv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrganizerSignUp extends AppCompatActivity {
    private EditText inputEmail, inputPassword, inputUsername;
    private Button btnSignIn, btnSignUp, btnUserSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    String username, password, email;
    DatabaseReference mDatabase;
    User org;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_sign_up);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        btnUserSignUp = (Button) findViewById(R.id.user_sign_up);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputUsername = (EditText) findViewById(R.id.username);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnUserSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerSignUp.this, SignUpActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerSignUp.this, LoginActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();
                username = inputUsername.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "Enter username!", Toast.LENGTH_SHORT).show();
                    return;
                }

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
                // Checks if a username is already taken
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Users");
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.hasChild(username)) {
                            Toast.makeText(getApplicationContext(), "Username already exists!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            progressBar.setVisibility(View.VISIBLE);
                            //create user
                            auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(OrganizerSignUp.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            //Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            // If sign in fails, display a message to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(OrganizerSignUp.this, "Authentication failed." + task.getException(),
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                auth.getCurrentUser().sendEmailVerification()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if( task.isSuccessful()){
                                                                    Toast.makeText( OrganizerSignUp.this, "Registered successfully. Please check your email to verify your account",
                                                                            Toast.LENGTH_LONG).show();
                                                                    inputEmail.setText("");
                                                                    inputUsername.setText("");
                                                                    inputPassword.setText("");
                                                                } else {
                                                                    Toast.makeText( OrganizerSignUp.this, task.getException().getMessage(),
                                                                            Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                                pushData(email);
                                                FirebaseUser user = auth.getCurrentUser();
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(username).build();
                                                user.updateProfile(profileUpdates);
                                                startActivity(new Intent(OrganizerSignUp.this, LoginActivity
                                                        .class));
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return;

            }
        });
    }
    private void pushData ( String email) {
        String userId =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        org = new User(email, userId, username, true);
        mDatabase = ref.child(username);
        mDatabase.setValue(org);
        // FOR NOW
        mDatabase.child("isOrg").setValue(true);
        mDatabase.child("preferences").child("ancFilter").setValue(false);
        mDatabase.child("preferences").child("fnwFilter").setValue(false);
        mDatabase.child("preferences").child("athFilter").setValue(false);
        mDatabase.child("preferences").child("semFilter").setValue(false);
        mDatabase.child("preferences").child("commFilter").setValue(false);
        mDatabase.child("preferences").child("wkndFilter").setValue(false);
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}