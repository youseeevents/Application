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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.Iterator;

public class SignUpActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword, inputUsername;
    private Button btnSignIn, btnSignUp, btnOrgSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    String username, password, email;
    DatabaseReference mDatabase;
    User user;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputUsername = (EditText) findViewById(R.id.username);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnOrgSignUp = (Button) findViewById(R.id.user_sign_up);

        btnOrgSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, OrganizerSignUp.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
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

                CharSequence com = ".com";
                CharSequence edu = ".edu";
                CharSequence net = ".net";
                CharSequence org = ".org";
                if (email.indexOf('@') == -1 || (!email.contains(com) && !email.contains(edu) && !email.contains(net) && !email.contains(org))) {
                    Toast.makeText(getApplicationContext(), "Email address is invalid! Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password is too short! Enter a minimum of 6 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Checks if a username is already taken
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Users");
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for(DataSnapshot childSnap : snapshot.getChildren()) {
                            if(childSnap.child("email").getValue().equals(email)) {
                                Toast.makeText(getApplicationContext(), "Email address is already in use! Please choose a different email address.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        if (snapshot.hasChild(username)) {
                            Toast.makeText(getApplicationContext(), "Username already exists! Please choose a different username.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            progressBar.setVisibility(View.VISIBLE);
                            //create user
                            auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            //Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            // If sign in fails, display a message to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                //finish();
                                                pushData(email);

                                                FirebaseUser user = auth.getCurrentUser();
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(username).build();
                                                user.updateProfile(profileUpdates);

                                                ref.child(username).child("preferences").child("ancFilter").setValue(false);
                                                ref.child(username).child("preferences").child("fnwFilter").setValue(false);
                                                ref.child(username).child("preferences").child("athFilter").setValue(false);
                                                ref.child(username).child("preferences").child("semFilter").setValue(false);
                                                ref.child(username).child("preferences").child("commFilter").setValue(false);
                                                ref.child(username).child("preferences").child("wkndFilter").setValue(false);

                                                startActivity(new Intent(SignUpActivity.this, LoginActivity
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
        user = new User(email, userId, username);
        mDatabase = ref.child(username);
        mDatabase.setValue(user);
        mDatabase.child("isOrg").setValue(false);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}