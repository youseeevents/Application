package com.example.youseeeventsv1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

/**
 * Created by Belal on 1/23/2018.
 */

public class MyEventsFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private DatabaseReference mDatabase;
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
        Button testButton = (Button) getView().findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDummyEvent();
            }
        });
    }
    private void makeDummyEvent() {
        System.out.println("Dummy event made");
        Event dummy = new Event("Test", "What if we... uploaded an event into firebase",new Date(5,8,2019), "0", "Geisel 1W", null);
        System.out.println(dummy);

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("Events");
        mDatabase = databaseRef.child(dummy.getName());
        mDatabase.setValue(dummy);
    }
}