package com.example.youseeeventsv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    EditText search_edit_text;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    ArrayList<Event> events;
    
    MyAdapter myAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, null);
    }

    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {

        search_edit_text = (EditText) getActivity().findViewById(R.id.search_edit_text);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.search_recycler_view);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        events = new ArrayList<>();

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()) {
                    setAdapter(s.toString());
                }
                else {
                    events.clear();
                    recyclerView.removeAllViews();
                }

            }
        });

    }

    private void setAdapter(final String searchedString) {
        databaseReference.child("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                events.clear();
                recyclerView.removeAllViews();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {


                    /* Fields used for search */
                    String event_name = snapshot.child("name").getValue(String.class);
                    String event_org = "University of California, San Diego";
                    if(snapshot.hasChild("organizer")){
                        event_org = snapshot.child("organizer").getValue(String.class);
                    }
                    String event_date = snapshot.child("date_readable").getValue(String.class);
                    String event_location = snapshot.child("location").getValue(String.class);
                    String event_tag = snapshot.child("tag").getValue(String.class);

                    /* Fields not used for search, used for event creation */
                    String event_description;
                    String event_time;
                    String eventId = snapshot.getKey();
                    String event_date_full = snapshot.child("date").getValue(String.class);
                    if((snapshot.child("description").getValue(String.class)) != null){
                        event_description = snapshot.child("description").getValue(String.class);
                    }
                    else{
                        event_description = "No Description Provided.";
                    }
                    if((snapshot.child("time").getValue(String.class)) != null){
                        event_time = snapshot.child("time").getValue(String.class);
                    }
                    else {
                        event_time = "No Time Provided.";
                    }
                    //int event_going = snapshot.child("counterGoing").getValue(int.class);
                    //int event_interested = snapshot.child("counterInterested").getValue(int.class);
                    //TODO//
                    int event_popularity = snapshot.child("counterGoing").getValue(Integer.class);

                    boolean event_added = false;


                    if(event_name.toLowerCase().contains(searchedString.toLowerCase())) {
                        if(!event_added) {
                            events.add(new Event(eventId, event_name, event_org, event_description, event_date_full, event_date, event_time, event_location, event_tag, event_popularity));
                            event_added = true;
                        }
                    }
                    else if(event_date.toLowerCase().contains(searchedString.toLowerCase())) {
                        if(!event_added) {
                            events.add(new Event(eventId, event_name, event_org, event_description, event_date_full, event_date, event_time, event_location, event_tag, event_popularity));
                            event_added = true;
                        }
                    }
                    else if(event_location.toLowerCase().contains(searchedString.toLowerCase())) {
                        if(!event_added) {
                            events.add(new Event(eventId, event_name, event_org, event_description, event_date_full, event_date, event_time, event_location, event_tag, event_popularity));
                            event_added = true;
                        }
                    }
                    else if(event_tag.toLowerCase().contains(searchedString.toLowerCase())) {
                        if(!event_added) {
                            events.add(new Event(eventId, event_name, event_org, event_description, event_date_full, event_date, event_time, event_location, event_tag, event_popularity));
                            event_added = true;
                        }
                    }

                }

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
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}