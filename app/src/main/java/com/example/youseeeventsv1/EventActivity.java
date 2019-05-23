package com.example.youseeeventsv1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

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

        // This sets the text fields on the EventActivity page.
        TextView title = findViewById(R.id.event_title);
        title.setText(event.getName());

        TextView date = findViewById(R.id.event_datetime);
        date.setText(event.getDate());

        TextView location = findViewById(R.id.event_location);
        location.setText(event.getLocation());

        TextView event_description = findViewById(R.id.event_description);
        event_description.setText(event.getDescription());
    }

}
