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

/**
 * Created by Belal on 1/23/2018.
 */

public class HomeFragment extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private DatabaseReference mDatabase;


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
        return inflater.inflate(R.layout.fragment_home, null);

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("Events");
        mProgressBar = getActivity().findViewById(R.id.home_progress_bar);

        if(recyclerView == null) {
            fillEventsArray();
        }
        // Setting up the recycler view and filling it with objects in the events array.
        recyclerView = (RecyclerView) getView().findViewById(R.id.home_recycler_view);
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