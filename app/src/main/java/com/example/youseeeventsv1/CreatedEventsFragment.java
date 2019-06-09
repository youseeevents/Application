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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreatedEventsFragment extends Fragment {

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
    private static ArrayList<Event> created_events = new ArrayList<>();
    private static ArrayList<String> created_events_names = new ArrayList<>();

    private static boolean initial_load = false;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private boolean refresh_on_resume = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_created_events, null);

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("Events");
        mProgressBar = getActivity().findViewById(R.id.created_progress_bar);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        createEvent = getView().findViewById(R.id.create_events_button);
        createEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //go to new page
                Intent intent = new Intent(v.getContext(), CreateEventActivity.class);
                v.getContext().startActivity(intent);
            }
        });


        String display_name = user.getDisplayName();
        if(recyclerView == null)
            fillSavedEvents(display_name);

        // Setting up the recycler view and filling it with objects in the events array.
        recyclerView = (RecyclerView) getView().findViewById(R.id.created_events_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        myAdapter = new MyAdapter(created_events, new MyAdapter.OnItemClickListener(){
            @Override public void onItemClick(Event event){
                // event -> EventActivity
                Intent intent = new Intent(getContext(), EventActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("Event", event);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }, new MyAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(Event v) {
                return true;
            }
        });

    }


    private void fillSavedEvents(String username){
        mProgressBar.setVisibility(View.VISIBLE);
        created_events_names.clear();
        created_events.clear();
        // Read all the saved events.
        FirebaseDatabase.getInstance().getReference("Users").child(username).child("created_events")
                .addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            created_events_names.add(ds.getKey());
                        }
                        // When you finish reading all the events from the user's saved events, you want to
                        // go through FirebaseDatabase and read all the events. If the event has the name of
                        // a saved event, display it.

                        if(created_events_names.size() >= dataSnapshot.getChildrenCount()) {
                            FirebaseDatabase.getInstance().getReference("Events")
                                    .orderByChild("date")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            /* So something I want to fix is here - for displaying saved
                                             * events, we are iterating through so many things - is there a
                                             * way we can access our specific events without having to
                                             * iterate through all of them?
                                             *
                                             * My idea was converting the datasnapshot into a hashmap and
                                             * accessing the values at key = saved_event_name, but I couldn't
                                             * get it to work.
                                             */
                                            for (String name : created_events_names) {
                                                /* So if the saved_event_name is not in the datasnapshot,
                                                 * remove it from the list as well as from the user's field.
                                                 */
                                                if (!dataSnapshot.hasChild(name)) {
                                                    created_events_names.remove(name);
                                                    FirebaseDatabase.getInstance().getReference("Users")
                                                            .child(user.getDisplayName())
                                                            .child("created_events").child(name)
                                                            .removeValue();
                                                }
                                            }
                                            int event_ind = 0;
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                if (created_events_names.contains(ds.getKey())) {
                                                    created_events.add(ds.getValue(Event.class));
                                                    created_events.get(event_ind).setEventId(ds.getKey());
                                                    event_ind += 1;
                                                }
                                            }
                                            if (created_events.size() >= created_events_names.size()) {
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
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

    }
    @Override
    public void onResume() {
        super.onResume();
        // Check should we need to refresh the fragment
        if(refresh_on_resume){
            // refresh fragment
            String display_name = user.getDisplayName();
            fillSavedEvents(display_name);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        refresh_on_resume = true;
    }

}
