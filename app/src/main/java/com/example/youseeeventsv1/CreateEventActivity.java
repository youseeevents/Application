package com.example.youseeeventsv1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateEventActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private DatabaseReference databaseRefUsers;
    Button createEventImageButton;
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
    String error_message;
    private String name_text;
    private String datetime_text;
    private String date_readable;
    private String time;
    private String description_text;
    private String location_text;
    private String selected_tag;
    private String event_Id;


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
        createEventImageButton = (Button)findViewById(R.id.create_events_button);

        createEventImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //reset error message
                error_message = "The following fields are invalid:";


                name_text = name.getText().toString();
                String name_ns = name_text.replaceAll("\\s", "");


                //boolean of inputs
                boolean all_inputs = true;

                //check for nonempty fields and error checking
                if(name_ns.equals("")) {
                    all_inputs = false;
                    error_message += "\n-Event Title";
                }
                if(month_spinner.getSelectedItem().toString().equals("Month")) {
                    all_inputs = false;
                    error_message += "\n-Month";
                }
                if(day_spinner.getSelectedItem().toString().equals("Day")) {
                    all_inputs = false;
                    error_message += "\n-Day";
                }
                if(year_spinner.getSelectedItem().toString().equals("Year")) {
                    all_inputs = false;
                    error_message += "\n-Year";
                }
                if(!((hour1.getText().toString().length() == 2 || hour1.getText().toString().length() == 1) &&
                        Integer.parseInt(hour1.getText().toString()) <= 12  && Integer.parseInt(hour1.getText().toString()) >= 1)) {
                    all_inputs = false;
                    error_message += "\n-Starting Hour";
                }
                if( !(minute1.getText().toString().length() == 2 &&
                        Integer.parseInt(minute1.getText().toString()) < 60  && Integer.parseInt(minute1.getText().toString()) >= 0)) {
                    all_inputs = false;
                    error_message += "\n-Starting Minute";
                }
                if(time_spinner1.getSelectedItem().toString().equals("----")) {
                    all_inputs = false;
                    error_message += "\n-Starting AM or PM";
                }
                if(!((hour2.getText().toString().length() == 2 || hour2.getText().toString().length() == 1) &&
                        Integer.parseInt(hour2.getText().toString()) <= 12  && Integer.parseInt(hour2.getText().toString()) >= 1)) {
                    all_inputs = false;
                    error_message += "\n-Ending Hour";
                }
                if(!(minute2.getText().toString().length() == 2 &&
                        Integer.parseInt(minute2.getText().toString()) < 60  && Integer.parseInt(minute2.getText().toString()) >= 0)) {
                    all_inputs = false;
                    error_message += "\n-Ending Minute";
                }
                if(time_spinner2.getSelectedItem().toString().equals("----")) {
                    all_inputs = false;
                    error_message += "\n-Ending AM or PM";
                }
                if(location.getText().toString().equals("")) {
                    all_inputs = false;
                    error_message += "\n-Location";
                }
                if(tag_spinner.getSelectedItem().toString().equals("Select Tag")) {
                    all_inputs = false;
                    error_message += "\n-Tag";
                }
                if(description.getText().toString().equals("")) {
                    all_inputs = false;
                    error_message += "\n-Description";
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
                    } else {
                        date_day_convert = day_spinner.getSelectedItem().toString();
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

                    datetime_text = year_spinner.getSelectedItem().toString() + "-"  + date_month_convert
                            + "-" + date_day_convert + "T" + time_convert + ":" + minute1.getText().toString() + ":00";


                    date_readable = month_spinner.getSelectedItem().toString() + day_spinner.getSelectedItem().toString();
                    time = hour1.getText().toString() + ":" + minute1.getText().toString() + (time_spinner1.getSelectedItem().toString()).toUpperCase() + " - "
                            + hour2.getText().toString() + ":" + minute2.getText().toString() + (time_spinner2.getSelectedItem().toString()).toUpperCase();
                    description_text = description.getText().toString().trim();
                    location_text = location.getText().toString().trim();
                    selected_tag = tag_spinner.getSelectedItem().toString().toLowerCase();
                    String[] event_tags_arr = {"arts & culture","fitness & well-being","athletics","seminars & info-sessions","community","weekend event"};
                    switch(selected_tag){
                        case "arts and culture":
                            selected_tag = event_tags_arr[0];
                            break;
                        case "fitness and wellbeing":
                            selected_tag = event_tags_arr[1];
                            break;
                        case "athletics":
                            break;
                        case "seminars":
                            selected_tag = event_tags_arr[3];
                            break;
                        case "community":
                            break;
                        case "weekend event":
                            break;
                    }
                    event_Id = name_ns + date_readable + year_spinner.getSelectedItem().toString();

                    //same event check
                    databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            boolean add_created_event = true;
                            String former_id = "";
                            String year_convert = "";

                            //set error message
                            error_message = "An event with this title already exists on this day. Please change the event title.";

                            //System.out.println("Before check");
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Event check_event = ds.getValue(Event.class);
                                //get year
                                if(check_event.getDate() != null && check_event.getDate().length() >= 4) {
                                    year_convert = check_event.getDate().substring(0, 4);
                                    System.out.println(year_convert);
                                }

                                //set up new event_Id
                                if(check_event.getEventId().equals(event_Id)){
                                    event_Id = event_Id + "*";
                                }

                                //check if an event exists in firebase, this checks the actual fields and not the firebase classification eventId
                                if(check_event.getName().equals(name_text) && check_event.getDate_readable().equals(date_readable)
                                        && year_convert.equals(year_spinner.getSelectedItem().toString())){
                                    Toast.makeText(CreateEventActivity.this, error_message, Toast.LENGTH_LONG).show();
                                    //former_id = check_event.getEventId();

                                    add_created_event = false;
                                    System.out.println("I already exist");
                                    break;

                                }
                            }

                            //if not found, then add event
                            if(add_created_event == true) {

                                /*System.out.println("ready to add event");
                                create new event and push to events and the user's created events*/
                                Event new_event = new Event(event_Id, name_text, organizer.getText().toString(), description_text,
                                        datetime_text, date_readable, time,
                                        location_text, selected_tag);

                                databaseRefUsers.child(user.getDisplayName()).child("created_events").child(new_event.getEventId()).setValue("");
                                FirebaseDatabase.getInstance().getReference().child("Events").child(new_event.getEventId()).setValue(new_event);

                                //end activity
                                finish();

                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });

                }
                else{
                    Toast.makeText(CreateEventActivity.this, error_message, Toast.LENGTH_LONG).show();
                }


            }
        });

    }
}




