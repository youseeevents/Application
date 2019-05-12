package com.example.youseeeventsv1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Belal on 1/23/2018.
 */

public class MyEventsFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private DatabaseReference mDatabase;

    private TextView dummy_text;
    private TextView dummy_location;
    private TextView dummy_date;
    private Button dummy_button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        return inflater.inflate(R.layout.fragment_my_events, null);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        dummy_button = (Button) getView().findViewById(R.id.dummy_button);
        dummy_text = (TextView) getView().findViewById(R.id.event_name_text);
        dummy_date = (TextView) getView().findViewById(R.id.event_date_text);
        dummy_location = (TextView) getView().findViewById(R.id.event_location_text);

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("Events");
        /*
            Button sends Dummy Event into Firebase
         */
        dummy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDummyEvent();
            }
        });

        /*
            Trying to read Dummy Event from Database and putting it onto a TextView Object
         */
        readFromDatabase();
    }
    private void makeDummyEvent() {
        System.out.println("Dummy event made");
        Event dummy_event = new Event("Dummy", "What if we... " +
                "uploaded an event into firebase",new Date(), "0",
                "Geisel 1W", null);
        System.out.println(dummy_event);

        mDatabase = databaseRef.child(dummy_event.getName());
        mDatabase.setValue(dummy_event);
    }
    private void readFromDatabase(){

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Basically, this will retrieve every event object under the "Events" tab.
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    /* For debugging
                    System.out.println(ds.getKey());
                    System.out.println(ds.getValue());
                    */
                    // getValue() from a DataSnapshot returns the information in a hashmap. So you
                    // need to make an event and set it equal to the getValue()
                    Event eve = ds.getValue(Event.class);
                    dummy_text.setText(eve.getName());
                    dummy_location.setText(eve.getLocation());
                    dummy_date.setText(eve.getDate().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v("dummy", "failed to read value", databaseError.toException());
            }
        });
    }
}