package com.example.youseeeventsv1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
    private String current_page_name = "Browse Events";
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Fragment fragment = null;
    private Boolean is_organizer = false;
    private int current_fragment_ind = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        setTitle(current_page_name);
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

        loadFragment(new HomeFragment(), 'd');

        BottomNavigationView bottom_navbar = (BottomNavigationView) findViewById(R.id.navigation);
        bottom_navbar.setOnNavigationItemSelectedListener(this);


    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int[] fragments = {R.id.menu_home, R.id.menu_search, R.id.menu_my_events, R.id.menu_account};
        int prev_id = current_fragment_ind;
        switch (item.getItemId()) {
            case R.id.menu_home:
                fragment = new HomeFragment();
                current_page_name = ("Browse Events");
                current_fragment_ind = 0;
                break;

            case R.id.menu_search:
                fragment = new SearchFragment();
                current_page_name = ("Search Events");
                current_fragment_ind = 1;
                break;

            case R.id.menu_my_events:
                current_fragment_ind = 2;
                if(is_organizer){
                    fragment = new CreatedEventsFragment();
                    current_page_name = ("Organized Events");
                } else {
                    fragment = new MyEventsFragment();
                    current_page_name = ("My Events");
                }
                break;
            case R.id.menu_account:
                current_fragment_ind = 3;
                user = auth.getCurrentUser();
                current_page_name = ("Account Settings");
                if(user != null) {
                    fragment = new AccountFragment();
                }
                else {
                    fragment = new NoAccountFragment();
                }
                break;
        }
        char tag;
        if(prev_id - current_fragment_ind > 0){
            // right to left
            tag = 'r';
        }
        else if(prev_id - current_fragment_ind < 0){
            // left to right
            tag = 'l';
        }
        else{
            // do nothing, same frqgment
            return false;
        }
        setTitle(current_page_name);
        return loadFragment(fragment, tag);
    }
    private boolean loadFragment(Fragment fragment, char tag) {
        //switching fragment
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            /* Transitions
            if(tag == 'l')
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            if(tag == 'r')
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
            */
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
            return true;
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
