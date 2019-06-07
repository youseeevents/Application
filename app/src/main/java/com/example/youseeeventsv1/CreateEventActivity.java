package com.example.youseeeventsv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.KeyStore;

public class CreateEventActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private DatabaseReference databaseRefUsers;
    ImageButton createEventImageButton;
    EditText name;
    EditText description;
    EditText location;
    EditText hour1;
    EditText minute1;
    EditText hour2;
    EditText minute2;
    Spinner tag_spinner;
    Spinner month_spinner;
    Spinner day_spinner;
    Spinner year_spinner;
    Spinner time_spinner1;
    Spinner time_spinner2;
    TextView organizer;
    String[] month_array = {"Jan", "Feb", "Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String[] month_format_array = {"01","02","03","04","05","06","07","08","09","10","11","12"};
    String name_ns;
    String date_readable;

    private FirebaseAuth auth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        databaseRef = FirebaseDatabase.getInstance().getReference("Events");
        databaseRefUsers = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();




        //get all the input
        name = findViewById(R.id.event_title);
        description = findViewById(R.id.description);
        location = findViewById(R.id.location);
        organizer = findViewById(R.id.organizerName);
        tag_spinner = findViewById(R.id.tag_spinner);
        month_spinner = findViewById(R.id.spin_month);
        day_spinner = findViewById(R.id.spin_day);
        year_spinner = findViewById(R.id.spin_year);
        time_spinner1 = findViewById(R.id.amORpm1);
        time_spinner2 = findViewById(R.id.amORpm2);
        hour1 = findViewById(R.id.hour1);
        hour2 = findViewById(R.id.hour2);
        minute1 = findViewById(R.id.min1);
        minute2 = findViewById(R.id.min2);


        //set organizer name
        organizer.setText(user.getDisplayName());


        //create event button
        createEventImageButton = (ImageButton) findViewById(R.id.image_createEvent);

        createEventImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String name_text = name.getText().toString();
                name_ns = name_text.replaceAll("\\s", "");

                //boolean of inputs
                boolean all_inputs = true;

                //check for nonempty fields
                if(name_ns.equals("")) {
                    all_inputs = false;
                }
                if(month_spinner.getSelectedItem().toString().equals("Month")) {
                    all_inputs = false;
                }
                if(day_spinner.getSelectedItem().toString().equals("Day")) {
                    all_inputs = false;
                }
                if(year_spinner.getSelectedItem().toString().equals("Year")) {
                    all_inputs = false;
                }
                if(!((hour1.getText().toString().length() == 2 || hour1.getText().toString().length() == 1) &&
                        Integer.parseInt(hour1.getText().toString()) <= 12  && Integer.parseInt(hour1.getText().toString()) >= 1)) {
                    all_inputs = false;
                }
                if( !(minute1.getText().toString().length() == 2 &&
                        Integer.parseInt(minute1.getText().toString()) <= 60  && Integer.parseInt(minute1.getText().toString()) >= 0)) {
                    all_inputs = false;
                }
                if(time_spinner1.getSelectedItem().toString().equals("----")) {
                    all_inputs = false;
                }
                if(!((hour2.getText().toString().length() == 2 || hour2.getText().toString().length() == 1) &&
                        Integer.parseInt(hour2.getText().toString()) <= 12  && Integer.parseInt(hour2.getText().toString()) >= 1)) {
                    all_inputs = false;
                }
                if(!(minute2.getText().toString().length() == 2 &&
                        Integer.parseInt(minute2.getText().toString()) <= 60  && Integer.parseInt(minute2.getText().toString()) >= 0)) {
                    all_inputs = false;
                }
                if(time_spinner2.getSelectedItem().toString().equals("----")) {
                    all_inputs = false;
                }
                if(location.getText().toString().equals("")) {
                    all_inputs = false;
                }
                if(tag_spinner.getSelectedItem().toString().equals("Select Tag")) {
                    all_inputs = false;
                }
                if(description.getText().toString().equals("")) {
                    all_inputs = false;
                }



                if(all_inputs == true) {
                    //create new Event
                    String date_month_convert = "";
                    String date_day_convert = "";
                    String time_convert = "";

                    //have to add 0 to month if it's less than 10
                    for(int x = 0; x < month_array.length; x++){
                        if(month_spinner.getSelectedItem().toString().equals(month_array[x])){
                            date_month_convert = month_format_array[x];
                            break;
                        }
                    }

                    //same for day
                    if(day_spinner.getSelectedItem().toString().length() == 1){
                        date_day_convert = "0" + day_spinner.getSelectedItem().toString();
                    }

                    //time converter
                    if(time_spinner1.getSelectedItem().toString().equals("pm")){
                        time_convert = String.valueOf(Integer.parseInt(hour1.getText().toString()) + 12);
                    }
                    else {
                        if(hour1.getText().toString().length() == 1){
                            time_convert = "0" + hour1.getText().toString();
                        }
                        else {
                            time_convert = hour1.getText().toString();
                        }

                    }


                    String datetime_text = year_spinner.getSelectedItem().toString() + "-"  + date_month_convert
                            + "-" + date_day_convert + "T" + time_convert + ":" + minute1.getText().toString() + ":00";


                    date_readable = month_spinner.getSelectedItem().toString() + day_spinner.getSelectedItem().toString();
                    String time = hour1.getText().toString() + ":" + minute1.getText().toString() + (time_spinner1.getSelectedItem().toString()).toUpperCase() + " - "
                            + hour2.getText().toString() + ":" + minute2.getText().toString() + (time_spinner2.getSelectedItem().toString()).toUpperCase();
                    String description_text = description.getText().toString().trim();
                    String location_text = location.getText().toString().trim();
                    String selected_tag = tag_spinner.getSelectedItem().toString().toLowerCase();

                    //same name check
                    databaseRef.child(name_ns + date_readable).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.getValue() != null) {
                                Event f = snapshot.getValue(Event.class);
                                //user exists, do something
                                System.out.println("I already exist" + f.getDate());
                                //add another read to check for year

                            } else {
                                //user does not exist, do something else
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });


                   /* Event new_event = new Event(name_ns + date_readable ,name_text, description_text,
                            datetime_text, date_readable, time,
                            location_text, selected_tag);

                    databaseRefUsers.child(user.getDisplayName()).child("created_events").child(new_event.getEventId()).setValue("");
                    FirebaseDatabase.getInstance().getReference().child("Events").child(new_event.getEventId()).setValue(new_event);

                    //go to new page
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    v.getContext().startActivity(intent);*/

                }
                else{
                    Toast.makeText(CreateEventActivity.this, "Some of your fields are invalid.", Toast.LENGTH_LONG).show();
                }


            }
        });

    }
}

    /*
    --side note, we have to figure out what to do with the tags(as it won't just be one string), and
    I believe the date and time text inputs should be seperate in the xml


        1. Create the create event button in the correct xml with the id(i used id:eventCreate in
        the code below

       2. //declare this above the onCreate function in whichever file we are putting it in
       Button createEvent;

       3.//so the code below works when placed in an onCreate function for a specific class,
       this is what leads us to the activity_create_event.xml

        createEvent = findViewById(R.id.eventCreate);
        createEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //go to new page
                Intent intent = new Intent(v.getContext(), CreateEventActivity.class);
                v.getContext().startActivity(intent);
            }
        });
     */


