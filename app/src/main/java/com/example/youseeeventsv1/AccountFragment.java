package com.example.youseeeventsv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Belal on 1/23/2018.
 */

public class AccountFragment extends Fragment {
    private FirebaseAuth auth;

    private Button logoutButton, resetButton;
    private CheckBox ancFilter, fnwFilter, athFilter, semFilter, commFilter, wkndFilter, allFilter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        View view = inflater.inflate(R.layout.fragment_account, null);

        auth = FirebaseAuth.getInstance();

        logoutButton = (Button) view.findViewById(R.id.logoutButton);
        resetButton = (Button) view.findViewById(R.id.btnResetPassword);

        ancFilter = (CheckBox) view.findViewById(R.id.filterAnC);
        fnwFilter = (CheckBox) view.findViewById(R.id.filterFnW);
        athFilter = (CheckBox) view.findViewById(R.id.filterAthletics);
        semFilter = (CheckBox) view.findViewById(R.id.filterSeminars);
        commFilter = (CheckBox) view.findViewById(R.id.filterCommunity);
        wkndFilter = (CheckBox) view.findViewById(R.id.filterWeekend);
        allFilter = (CheckBox) view.findViewById(R.id.filterAll);

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

                }
                else {

                }
            }
        });

        fnwFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){

                }
                else {

                }
            }
        });

        athFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){

                }
                else {

                }
            }
        });

        semFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){

                }
                else {

                }
            }
        });

        commFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){

                }
                else {

                }
            }
        });

        wkndFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){

                }
                else {

                }
            }
        });

        allFilter.setOnClickListener(new View.OnClickListener() {
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
        });

        return view;
    }
}