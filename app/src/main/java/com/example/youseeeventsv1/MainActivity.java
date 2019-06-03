package com.example.youseeeventsv1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Fragment fragment = null;
    private Boolean is_organizer = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user != null){
            String username = user.getDisplayName();
            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(username)
                    .child("isOrg")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue().equals(true)){
                                is_organizer = true;
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        }

        loadFragment(new HomeFragment());

        BottomNavigationView bottom_navbar = (BottomNavigationView) findViewById(R.id.navigation);
        bottom_navbar.setOnNavigationItemSelectedListener(this);


    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                if(is_organizer){
                    fragment = new CreatedEventsFragment();
                    setTitle("Organized Events");
                } else {
                    fragment = new MyEventsFragment();
                    setTitle("My Events");
                }
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
        System.out.println(fragment);
        return loadFragment(fragment);
    }
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            System.out.println(fragment);
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
