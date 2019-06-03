package com.example.youseeeventsv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        auth = FirebaseAuth.getInstance();
        loadFragment(new HomeFragment());

        BottomNavigationView bottom_navbar = (BottomNavigationView) findViewById(R.id.navigation);
        bottom_navbar.setOnNavigationItemSelectedListener(this);


    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.menu_home:
                fragment = new HomeFragment();
                setTitle("Home");
                break;

            case R.id.menu_search:
                fragment = new SearchFragment();
                setTitle("Search");
                break;

            case R.id.menu_my_events:
                fragment = new MyEventsFragment();
                setTitle("My Events");
                break;
            case R.id.menu_account:

                user = auth.getCurrentUser();

                setTitle("Account");
                if(user != null) {
                    fragment = new AccountFragment();
                }
                else {
                    fragment = new NoAccountFragment();
                }

                break;
        }
        return loadFragment(fragment);
    }
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
