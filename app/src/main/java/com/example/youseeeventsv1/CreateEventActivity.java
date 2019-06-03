package com.example.youseeeventsv1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private FirebaseAuth auth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        databaseRef = FirebaseDatabase.getInstance().getReference("Events");
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


        /*ArrayAdapter<CharSequence> tag_adapter = ArrayAdapter.createFromResource(this, R.array.tag_array, R.layout.support_simple_spinner_dropdown_item);
        tag_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        tag_spinner.setAdapter(tag_adapter);*/


        //create event button
        createEventImageButton = (ImageButton) findViewById(R.id.image_createEvent);

        createEventImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(CreateEventActivity.this, "It works", Toast.LENGTH_LONG).show();
                System.out.println(String.valueOf(tag_spinner.getSelectedItem()));

                //create new Event
                String name_text = name.getText().toString();
                String name_ns = name_text.replaceAll("\\s", "");
                //have to add 0 to day if it's less than 10
                String datetime_text = year_spinner.getSelectedItem().toString() + "-"  + month_spinner.getSelectedItem().toString()
                + "-" + day_spinner.getSelectedItem().toString() + "T" +"";
                String date_readable = month_spinner.getSelectedItem().toString() + day_spinner.getSelectedItem().toString();
                String time = hour1.getText().toString() + ":" + minute1.getText().toString() + (time_spinner1.getSelectedItem().toString()).toUpperCase() + " - "
                        + hour2.getText().toString() + ":" + minute2.getText().toString() + (time_spinner2.getSelectedItem().toString()).toUpperCase();
                String description_text = description.getText().toString();

                Event dummy_event = new Event(name_ns + "",name_text, description_text,
                        datetime_text, date_readable, time,
                        location.getText().toString(), tag_spinner.getSelectedItem().toString().toLowerCase());

                if(!name_ns.equals("")) {
                    databaseRef.child(name_ns).setValue(dummy_event);
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


