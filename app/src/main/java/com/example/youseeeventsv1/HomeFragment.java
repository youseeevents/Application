package com.example.youseeeventsv1;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Belal on 1/23/2018.
 */

public class HomeFragment extends Fragment {
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private DatabaseReference mDatabase;


    private final static int load_incr = 20;
    static int start_ind = 0;

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static ProgressBar mProgressBar;
    private static ArrayList<Event> events = new ArrayList<>();
    private static ArrayList<Event> tempEvents = new ArrayList<>();

    private static boolean initial_load = false;

    private static ArrayList<Event> arts = new ArrayList<>();
    private static ArrayList<Event> fitness = new ArrayList<>();
    private static ArrayList<Event> seminars = new ArrayList<>();
    private static ArrayList<Event> community = new ArrayList<>();
    private static ArrayList<Event> athletics = new ArrayList<>();
    private static ArrayList<Event> weekend = new ArrayList<>();

    private boolean toggleA, toggleAC, toggleFW, toggleC, toggleW, toggleS = false;
    private Button A, AC, FW, W, C, S, CL;

    private boolean filterFirstClick = false;

    private Spinner sort_or_filter_spinner;
    private Spinner sort_list_spinner;
    private HorizontalScrollView home_tag_buttons;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, null);

    }

    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState){
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("Events");
        mProgressBar = getActivity().findViewById(R.id.home_progress_bar);

        sort_or_filter_spinner = getView().findViewById(R.id.sort_or_filter_spinner);
        final String[] sort_or_filter = {"Filter", "Sort"};

        home_tag_buttons = getView().findViewById(R.id.home_tag_buttons);
        sort_list_spinner = getView().findViewById(R.id.sort_spinner);
        final String[] sort_list = {"Please select a value to sort by.", "Date", "Popularity"};

        sort_or_filter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = sort_or_filter[position];
                switch(choice){
                    case "Sort":
                        home_tag_buttons.setVisibility(View.GONE);
                        sort_list_spinner.setVisibility(View.VISIBLE);
                        break;
                    case "Filter":
                        home_tag_buttons.setVisibility(View.VISIBLE);
                        sort_list_spinner.setVisibility(View.GONE);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sort_list_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = sort_list[position];
                LinearLayoutManager layoutManager;
                switch(choice){
                    case "Please select a value to sort by":
                        break;
                    case "Date":
                        fillEventsArrayBySort("date");
                        layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        break;
                    case "Popularity":
                        fillEventsArrayBySort("counterGoing");
                        layoutManager = new LinearLayoutManager(getActivity());
                        layoutManager.setReverseLayout(true);
                        layoutManager.setStackFromEnd(true);
                        recyclerView.setLayoutManager(layoutManager);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        A = getView().findViewById(R.id.home_tag_A_button);
        AC = getView().findViewById(R.id.home_tag_AC_button);
        FW = getView().findViewById(R.id.home_tag_FW_button);
        W = getView().findViewById(R.id.home_tag_W_button);
        C = getView().findViewById(R.id.home_tag_C_button);
        S = getView().findViewById(R.id.home_tag_S_button);
        CL = getView().findViewById(R.id.home_tag_ALL_button);

        if(recyclerView == null) {
            fillEventsArray();
        }
        // Setting up the recycler view and filling it with objects in the events array.
        recyclerView = (RecyclerView) getView().findViewById(R.id.home_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        myAdapter = new MyAdapter(events, new MyAdapter.OnItemClickListener(){
            @Override public void onItemClick(Event event){
                // event -> EventActivity
                Intent event_info_intent = new Intent(getContext(), EventActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("Event", event);
                event_info_intent.putExtras(bundle);
                startActivity(event_info_intent);
            }
        });
        if(auth.getCurrentUser() != null) {
            String userName = auth.getCurrentUser().getDisplayName();
            FirebaseDatabase.getInstance().getReference("Users").child(userName).child("preferences")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean all_true = true;
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if (ds.getValue().equals(false)){
                                    all_true = false;
                                }
                            }
                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            if (!all_true) {
                                Iterator<DataSnapshot> it = children.iterator();
                                if (it.next().getValue().equals(true)) {
                                    if (filterFirstClick == false) {
                                        filterFirstClick = true;
                                        fillEachArray();
                                        myAdapter.swap(arts);
                                    } else {
                                        myAdapter.append(arts);
                                    }
                                    AC.setBackgroundResource(R.drawable.button_border_pressed);
                                    AC.setEnabled(false);
                                }
                                if (it.next().getValue().equals(true)) {
                                    if (filterFirstClick == false) {
                                        filterFirstClick = true;
                                        fillEachArray();
                                        myAdapter.swap(athletics);
                                    } else {
                                        myAdapter.append(athletics);
                                    }
                                    A.setBackgroundResource(R.drawable.button_border_pressed);
                                    A.setEnabled(false);
                                }
                                if (it.next().getValue().equals(true)) {
                                    if (filterFirstClick == false) {
                                        filterFirstClick = true;
                                        fillEachArray();
                                        myAdapter.swap(community);
                                    } else {
                                        myAdapter.append(community);
                                    }
                                    C.setBackgroundResource(R.drawable.button_border_pressed);
                                    C.setEnabled(false);
                                }
                                if (it.next().getValue().equals(true)) {
                                    if (filterFirstClick == false) {
                                        filterFirstClick = true;
                                        fillEachArray();
                                        myAdapter.swap(fitness);
                                    } else {
                                        myAdapter.append(fitness);
                                    }
                                    FW.setBackgroundResource(R.drawable.button_border_pressed);
                                    FW.setEnabled(false);
                                }
                                if (it.next().getValue().equals(true)) {
                                    if (filterFirstClick == false) {
                                        filterFirstClick = true;
                                        fillEachArray();
                                        myAdapter.swap(seminars);
                                    } else {
                                        myAdapter.append(seminars);

                                    }
                                    S.setBackgroundResource(R.drawable.button_border_pressed);
                                    S.setEnabled(false);
                                }
                                if (it.next().getValue().equals(true)) {
                                    if (filterFirstClick == false) {
                                        filterFirstClick = true;
                                        fillEachArray();
                                        myAdapter.swap(weekend);
                                    } else {
                                        myAdapter.append(weekend);
                                    }
                                    W.setBackgroundResource(R.drawable.button_border_pressed);
                                    W.setEnabled(false);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        }

        AC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(filterFirstClick == false){
                    filterFirstClick = true;
                    fillEachArray();
                    myAdapter.swap(arts);
                }
                else{
                    myAdapter.append(arts);
                }
                AC.setBackgroundResource(R.drawable.button_border_pressed);
                AC.setEnabled(false);
            }
        });

        FW.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(filterFirstClick == false){
                    filterFirstClick = true;
                    fillEachArray();
                    myAdapter.swap(fitness);
                }
                else {
                    myAdapter.append(fitness);
                }
                FW.setBackgroundResource(R.drawable.button_border_pressed);
                FW.setEnabled(false);

            }
        });

        S.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(filterFirstClick == false){
                    filterFirstClick = true;
                    fillEachArray();
                    myAdapter.swap(seminars);
                }
                else{
                    myAdapter.append(seminars);

                }
                S.setBackgroundResource(R.drawable.button_border_pressed);
                S.setEnabled(false);


            }
        });

        C.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(filterFirstClick == false){
                    filterFirstClick = true;
                    fillEachArray();
                    myAdapter.swap(community);
                }
                else{
                    myAdapter.append(community);
                }
                C.setBackgroundResource(R.drawable.button_border_pressed);
                C.setEnabled(false);
            }
        });

        W.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(filterFirstClick == false) {
                    filterFirstClick = true;
                    fillEachArray();
                    myAdapter.swap(weekend);
                }
                else {
                    myAdapter.append(weekend);
                }
                W.setBackgroundResource(R.drawable.button_border_pressed);
                W.setEnabled(false);
            }
        });

        A.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(filterFirstClick == false){
                    filterFirstClick = true;
                    fillEachArray();
                    myAdapter.swap(athletics);
                }
                else {
                    myAdapter.append(athletics);
                }
                A.setBackgroundResource(R.drawable.button_border_pressed);
                A.setEnabled(false);
            }
        });

        CL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int resId = R.drawable.button_border;
                A.setBackgroundResource(resId);
                FW.setBackgroundResource(resId);
                AC.setBackgroundResource(resId);
                C.setBackgroundResource(resId);
                S.setBackgroundResource(resId);
                W.setBackgroundResource(resId);

                A.setEnabled(true);
                AC.setEnabled(true);
                FW.setEnabled(true);
                S.setEnabled(true);
                C.setEnabled(true);
                W.setEnabled(true);

                fillEventsArray();

                myAdapter.swap(events);
                filterFirstClick = false;
            }
        });
    }

    private void fillEventsArray(){
        Date curr = new Date();
        System.out.println(curr.toString());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currDate = format.format(curr).replace(' ','T');

        mProgressBar.setVisibility(View.VISIBLE);
        events.clear();
        FirebaseDatabase.getInstance().getReference("Events")
                .orderByChild("date")
                .startAt(currDate)
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
                        // At this point, we have read in all the events so we need to
                        // display the events in the view and hide the progress bar.
                        if(events.size() >= dataSnapshot.getChildrenCount()){
                            initial_load = true;
                            mProgressBar.setVisibility(View.GONE);
                            recyclerView.setAdapter(myAdapter);
                            recyclerView.setVisibility(View.VISIBLE);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(layoutManager);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void fillEventsArrayBySort(final String sortedBy){
        mProgressBar.setVisibility(View.VISIBLE);
        events.clear();
        Query q = FirebaseDatabase.getInstance().getReference("Events")
                .orderByChild(sortedBy);
        if(sortedBy == "date"){
            Date curr = new Date();
            System.out.println(curr.toString());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currDate = format.format(curr).replace(' ','T');
            q = q.startAt(currDate);
        }
                // startAt(0)
                //.limitToFirst(20)
        q.addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int event_ind = 0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            events.add(ds.getValue(Event.class));
                            events.get(event_ind).setEventId(ds.getKey());
                            // event_ind fills the events array, which is passed into the recycler view
                            event_ind = event_ind + 1;
                        }
                        // At this point, we have read in all the events so we need to
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

    private void fillEachArray(){
        arts.clear();
        fitness.clear();
        weekend.clear();
        seminars.clear();
        community.clear();
        athletics.clear();
        for(int i = 0; i < events.size(); i++) {
            Event newEvent = events.get(i);
            if(newEvent.tag != null) {
                if (newEvent.tag.equals("arts & culture")) {
                    arts.add(newEvent);
                }
                if (newEvent.tag.equals("fitness & well-being")) {
                    fitness.add(newEvent);
                }
                if (newEvent.tag.equals("seminars & info-sessions")) {
                    seminars.add(newEvent);
                }
                if (newEvent.tag.equals("community")) {
                    community.add(newEvent);
                }
                if (newEvent.tag.equals("weekend event")) {
                    weekend.add(newEvent);
                }
                if (newEvent.tag.equals("athletics")) {
                    athletics.add(newEvent);
                }
            }
        }
    }
}