package com.example.youseeeventsv1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class CreateEventActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    ImageButton createEventImageButton;
    EditText name;
    EditText description;
    EditText datetime;
    EditText location;
    EditText tags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        databaseRef = FirebaseDatabase.getInstance().getReference("Events");



        //get all the input
        name = findViewById(R.id.event_title);
        description = findViewById(R.id.description);
        datetime = findViewById(R.id.DateTime);
        location = findViewById(R.id.location);
        tags = findViewById(R.id.tags);

        createEventImageButton = (ImageButton) findViewById(R.id.image_createEvent);

        createEventImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(CreateEventActivity.this, "It works", Toast.LENGTH_LONG).show();

                //create new Event
                String name_text = name.getText().toString();
                String name_ns = name_text.replaceAll("\\s", "");
                String datetime_text = datetime.getText().toString();

                String description_text = description.getText().toString();
                Event dummy_event = new Event(name_ns + "",name_text, description_text,
                        datetime_text, datetime.getText().toString(), "0",
                        location.getText().toString(), tags.getText().toString());

                if(!name_ns.equals("")) {
                    databaseRef.child(name_ns).setValue(dummy_event);
                }
                else{
                    Toast.makeText(CreateEventActivity.this, "Some of your fields are invalid", Toast.LENGTH_LONG).show();
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


