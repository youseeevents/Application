package com.example.youseeeventsv1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
    private Event event;
    private static ToggleButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event);
        saveButton = (ToggleButton) findViewById(R.id.saveButton);

        // Here, we are taking the bundle passed from MyEventFragment and creating an Event object
        // it. We do this because we cannot pass an Event object through activity creation
        Bundle bundle = getIntent().getExtras();
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

        TextView organizer = findViewById(R.id.event_organizer);
        organizer.setText("Not implemented.");

        TextView tag = findViewById(R.id.event_tag);
        tag.setText(event.getTag());

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

        // check if the event is already saved under the user and set the buttons toggle
        if(user != null) {
            DatabaseReference user_events_ref = ref.child(user.getDisplayName()).child("events");
            // user is logged in
            user_events_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild(event.getEventId())) {
                        saveButton.setChecked(true);
                    } else {
                        saveButton.setChecked(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        saveButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    if(user != null) {
                        DatabaseReference user_events_ref = ref.child(user.getDisplayName()).child("events");
                        // user is logged in
                        user_events_ref.child(event.getEventId()).setValue("");
                        Toast.makeText(getApplicationContext(), "Event Saved!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // user is not logged in
                        buttonView.setChecked(false);
                        startActivity(new Intent( EventActivity.this, LoginActivity.class));
                    }
                } else {
                    final DatabaseReference user_events_ref = ref.child(user.getDisplayName()).child("events");
                    // The toggle is disabled
                    user_events_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            ToggleButton saveButton = (ToggleButton) findViewById(R.id.saveButton);
                            if (snapshot.hasChild(event.getEventId())) {
                                user_events_ref.child(event.getEventId()).removeValue();
                                Toast.makeText(getApplicationContext(), "Event Unsaved!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
}
