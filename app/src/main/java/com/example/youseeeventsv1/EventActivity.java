package com.example.youseeeventsv1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        auth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Here, we are taking the bundle passed from MyEventFragment and creating an Event object
        // it. We do this because we cannot pass an Event object through activity creation
        Bundle bundle = getIntent().getExtras();
        Event event = null;
        if (bundle != null) {
            event = bundle.getParcelable("Event");
            //The key argument here must match that used in the other activity
        }
        else if (event == null) {
            finish();
        }

        // This sets the text fields on the EventActivity page.
        TextView title = findViewById(R.id.event_title);
        title.setText(event.getName());

        TextView date = findViewById(R.id.event_datetime);
        date.setText(event.getDate());

        TextView location = findViewById(R.id.event_location);
        location.setText(event.getLocation());

        TextView event_description = findViewById(R.id.event_description);
        event_description.setText(event.getDescription());

        FloatingActionButton shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://youseeevents.github.io/");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        FloatingActionButton saveButton = findViewById(R.id.saveButton);
        final Event finalEvent = event;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = auth.getCurrentUser();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

                // user is logged in
                if(user != null) {
                    ref.child(user.getDisplayName()).child("events").push().setValue(finalEvent.getEventId());
                    System.out.println(finalEvent.getEventId());
                }
                // user is not logged in
                else {
                    startActivity(new Intent( EventActivity.this, LoginActivity.class));
                }
            }
        });
    }

}
