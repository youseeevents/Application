package com.example.youseeeventsv1;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyEventsFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private DatabaseReference mDatabase;


    //private Button dummy_button;
    private Button createEvent;


    private final static int load_incr = 20;
    static int start_ind = 0;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static ProgressBar mProgressBar;
    private static ArrayList<Event> events = new ArrayList<>();

    private static boolean initial_load = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_events, null);

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("Events");
        mProgressBar = getActivity().findViewById(R.id.events_progress_bar);

        createEvent = getView().findViewById(R.id.eventCreate);
        createEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //go to new page
                Intent intent = new Intent(v.getContext(), CreateEventActivity.class);
                v.getContext().startActivity(intent);
            }
        });


        if(recyclerView == null) {
            fillEventsArray();
        }
        // Setting up the recycler view and filling it with objects in the events array.
        recyclerView = (RecyclerView) getView().findViewById(R.id.events_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        myAdapter = new MyAdapter(events, new MyAdapter.OnItemClickListener(){
            @Override public void onItemClick(Event event){
                // event -> EventActivity
                Intent intent = new Intent(getContext(), EventActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("Event", event);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void fillEventsArray(){
        System.out.println("READING FROM " + start_ind);
        mProgressBar.setVisibility(View.VISIBLE);
        // This is how we are supposedly querying the data from Firebase. It doesn't work right now.
        FirebaseDatabase.getInstance().getReference("Events")
                .orderByChild("date")
                // startAt(0)
                //.limitToFirst(20)
                .addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int event_ind = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    events.add(ds.getValue(Event.class));
                    events.get(event_ind).setEventId(ds.getKey());
                    // event_ind fills the events array, which is passed into the recycler view
                    event_ind = event_ind + 1;
                }
                // When the event_ind is 19, that means the events array is full. At this point,
                // display the events in the view and hide the progress bar.
                if(events.size() >= dataSnapshot.getChildrenCount()){
                    initial_load = true;
                    mProgressBar.setVisibility(View.GONE);
                    recyclerView.setAdapter(myAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
