package com.example.youseeeventsv1;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private Button dummy_button;

    private final static int load_incr = 20;
    static int start_ind = 0;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static ProgressBar mProgressBar;
    private static ArrayList<Event> events = new ArrayList<>();
    private static DataSnapshot lastVisible;

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

        // Dummy button for testing out things
        dummy_button = (Button) getView().findViewById(R.id.dummy_button);
        dummy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_ind = start_ind + 20;
                recyclerView.setVisibility(View.GONE);
                fillEventsArray();
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

    /**
     * Method for creating a dummy event. Not needed anymore, but here it is for reference.
     */
    private void makeDummyEvent() {
        System.out.println("Dummy event made");
        Event dummy_event = new Event("ID", "Dummy", "What if we... " +
                "uploaded an event into firebase", "dummy_date", "dummy_readable", "0",
                "Geisel 1W", "tag");
        System.out.println(dummy_event);
        mDatabase = databaseRef.child(dummy_event.getName());
        mDatabase.setValue(dummy_event);
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
/*************************************************************
 * ARCHAIC METHOD - would read EVERYTHING from the database. *
 *************************************************************
private void readFromDatabase(){

    databaseRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            // Basically, this will retrieve every event object under the "Events" tab.
            for (DataSnapshot ds : dataSnapshot.getChildren()){
                // getValue() from a DataSnapshot returns the information in a hashmap. So you
                // need to make an event and set it equal to the getValue()
                Event e = ds.getValue(Event.class);
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.v("dummy", "failed to read value", databaseError.toException());
        }
    });
}
*/
