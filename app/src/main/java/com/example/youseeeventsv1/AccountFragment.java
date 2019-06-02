package com.example.youseeeventsv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Belal on 1/23/2018.
 */

public class AccountFragment extends Fragment {
    private FirebaseAuth auth;
    private String userName;
    private FirebaseDatabase database;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

    private Button logoutButton, resetButton;
    private CheckBox ancFilter, fnwFilter, athFilter, semFilter, commFilter, wkndFilter/*, allFilter*/;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        View view = inflater.inflate(R.layout.fragment_account, null);

        auth = FirebaseAuth.getInstance();
        userName = auth.getCurrentUser().getDisplayName();

        logoutButton = (Button) view.findViewById(R.id.logoutButton);
        resetButton = (Button) view.findViewById(R.id.btnResetPassword);

        ancFilter = (CheckBox) view.findViewById(R.id.filterAnC);
        fnwFilter = (CheckBox) view.findViewById(R.id.filterFnW);
        athFilter = (CheckBox) view.findViewById(R.id.filterAthletics);
        semFilter = (CheckBox) view.findViewById(R.id.filterSeminars);
        commFilter = (CheckBox) view.findViewById(R.id.filterCommunity);
        wkndFilter = (CheckBox) view.findViewById(R.id.filterWeekend);
        /*allFilter = (CheckBox) view.findViewById(R.id.filterAll);*/

        FirebaseDatabase.getInstance().getReference("Users").child(userName).child("preferences")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        Iterator<DataSnapshot> it = children.iterator();

                        if( it.next().getValue().equals(true) ) {
                            ancFilter.setChecked(true);
                        }
                        if( it.next().getValue().equals(true) ) {
                            athFilter.setChecked(true);
                        }
                        if( it.next().getValue().equals(true) ) {
                            commFilter.setChecked(true);
                        }
                        if( it.next().getValue().equals(true) ) {
                            fnwFilter.setChecked(true);
                        }
                        if( it.next().getValue().equals(true) ) {
                            semFilter.setChecked(true);
                        }
                        if( it.next().getValue().equals(true) ) {
                            wkndFilter.setChecked(true);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent( getActivity(), MainActivity.class));
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( getActivity(), ResetPasswordActivity.class));
            }
        });

        ancFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){
                    ref.child(userName).child("preferences").child("ancFilter").setValue(true);
                }
                else {
                    ref.child(userName).child("preferences").child("ancFilter").setValue(false);
                }
            }
        });

        fnwFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){
                    ref.child(userName).child("preferences").child("fnwFilter").setValue(true);
                }
                else {
                    ref.child(userName).child("preferences").child("fnwFilter").setValue(false);
                }
            }
        });

        athFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){
                    ref.child(userName).child("preferences").child("athFilter").setValue(true);
                }
                else {
                    ref.child(userName).child("preferences").child("athFilter").setValue(false);
                }
            }
        });

        semFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){
                    ref.child(userName).child("preferences").child("semFilter").setValue(true);
                }
                else {
                    ref.child(userName).child("preferences").child("semFilter").setValue(false);
                }
            }
        });

        commFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){
                    ref.child(userName).child("preferences").child("commFilter").setValue(true);
                }
                else {
                    ref.child(userName).child("preferences").child("commFilter").setValue(false);
                }
            }
        });

        wkndFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){
                    ref.child(userName).child("preferences").child("wkndFilter").setValue(true);
                }
                else {
                    ref.child(userName).child("preferences").child("wkndFilter").setValue(false);
                }
            }
        });

        //Add an option to check all boxes later

        /*allFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()) {
                    ancFilter.setChecked(true);
                    fnwFilter.setChecked(true);
                    athFilter.setChecked(true);
                    semFilter.setChecked(true);
                    commFilter.setChecked(true);
                    wkndFilter.setChecked(true);
                }
            }
        });*/

        return view;
    }
}