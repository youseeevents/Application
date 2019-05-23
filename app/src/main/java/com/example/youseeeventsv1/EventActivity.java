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

        Bundle extras = getIntent().getExtras();
        Event event = null;
        if (extras != null) {
            event = extras.getParcelable("Event");
            //The key argument here must match that used in the other activity
        }
        System.out.println("EVENT PAGE OPENED");
        System.out.println("EVENT NAME: "+ event.getName());

        TextView title = findViewById(R.id.event_title);
        title.setText(event.getName());

        TextView date = findViewById(R.id.event_datetime);
        date.setText(event.getDate());

        TextView location = findViewById(R.id.event_location);
        location.setText(event.getLocation());

        TextView event_description = findViewById(R.id.event_description);
        event_description.setText(event.getDescription());

        System.out.println(event.getDescription());
    }

}
