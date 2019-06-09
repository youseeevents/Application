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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditEventActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private DatabaseReference databaseRefUsers;

    Button editEventImageButton;
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
    String[] event_Tags_arr = {"Select Tag","arts & culture","fitness & well-being","athletics","seminars & info-sessions","community","weekend events"};
    String[] event_Years_arr = {"Year", "2019", "2020"};
    String[] event_Month_arr = {"Month", "Jan", "Feb", "Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String[] event_Day_arr = {"Day", "1", "2", "3", "4", "5", "6", "7", "8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
    String[] AMorPM_arr = {"----", "am", "pm"};
    String error_message;
    private String name_text;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private Event event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //will need to change to activity_edit_event
        setContentView(R.layout.activity_create_event);

        databaseRef = FirebaseDatabase.getInstance().getReference("Events");
        databaseRefUsers = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            event = bundle.getParcelable("Event");
            //The key argument here must match that used in the other activity
        }
        else if (event == null) {
            finish();
        }


        //get all the input
        name = findViewById(R.id.event_title);           //done
        description = findViewById(R.id.description);    //done
        location = findViewById(R.id.location);          //done
        organizer = findViewById(R.id.organizerName);    //done
        tag_spinner = findViewById(R.id.tag_spinner);    //done
        month_spinner = findViewById(R.id.spin_month);   //done
        day_spinner = findViewById(R.id.spin_day);       //done
        year_spinner = findViewById(R.id.spin_year);     //done
        time_spinner1 = findViewById(R.id.amORpm1);      //done
        time_spinner2 = findViewById(R.id.amORpm2);      //done
        hour1 = findViewById(R.id.hour1);                //done
        hour2 = findViewById(R.id.hour2);                //done
        minute1 = findViewById(R.id.min1);               //done
        minute2 = findViewById(R.id.min2);               //done

        //set organizer name and other fields
        organizer.setText(user.getDisplayName());
        name.setText(event.getName());
        location.setText(event.getLocation());
        description.setText(event.getDescription());

        String eventTime = event.getDate();
        String eventYear = eventTime.substring(0,4);
        String eventDay = eventTime.substring(5,7);
        String eventHour = eventTime.substring(11,13);
        String eventMin = eventTime.substring(14,16);

        System.out.println(eventTime);
        System.out.println(eventHour);
        int eventHourNum = Integer.parseInt(eventHour);
        //in pm
        if(eventHourNum > 12)
        {
            eventHourNum = eventHourNum - 12;
            hour1.setText(String.valueOf(eventHourNum));

            /*eventHour = Integer.toString(eventHourNum);
            hour1.setText(eventHour);*/

            time_spinner1.setSelection(2);
        }
        else{
            //in am
            if(eventHourNum < 10) {
                hour1.setText(String.valueOf(eventHourNum));
            }
            else{
                hour1.setText(eventHour);
            }
            time_spinner1.setSelection(1);
        }

        minute1.setText(eventMin);

        String eventTag = event.getTag();
        int eventTagIndex = 0;
        while(eventTagIndex < event_Tags_arr.length)
        {
            if((event_Tags_arr[eventTagIndex].toLowerCase()).equals(eventTag))
            {
                break;
            }
            eventTagIndex++;
        }
        tag_spinner.setSelection(eventTagIndex);

        int eventYearIndex = 0;
        while(eventYearIndex < event_Years_arr.length)
        {
            if((event_Years_arr[eventYearIndex]).equals(eventYear))
            {
                break;
            }
            eventYearIndex++;
        }
        year_spinner.setSelection(eventYearIndex);

        String eventMonth = event.getDate_readable().substring(0,3);
        int eventMonthIndex = 0;
        while(eventMonthIndex < event_Month_arr.length)
        {
            if((event_Month_arr[eventMonthIndex]).equals(eventMonth))
            {
                break;
            }
            eventMonthIndex++;
        }
        month_spinner.setSelection(eventMonthIndex);

        String eventDayRead = event.getDate_readable().substring(3);
        int eventDayIndex = 0;
        while(eventDayIndex < event_Day_arr.length)
        {
            if((event_Day_arr[eventDayIndex]).equals(eventDayRead))
            {
                break;
            }
            eventDayIndex++;
        }
        day_spinner.setSelection(eventDayIndex);

        String eventTime2 = event.getTime();

        int hifenIndex = eventTime2.indexOf('-');

        int colonIndex = eventTime2.indexOf(':', (hifenIndex + 1));
        String eventhour2 = eventTime2.substring(hifenIndex +1, colonIndex).trim();
        String min2 = eventTime2.substring((colonIndex+1),(colonIndex+3));
        String amOrPm2 = eventTime2.substring(colonIndex+3).toLowerCase();
        hour2.setText(eventhour2);
        minute2.setText(min2);

        int amOrpmIndex = 0;
        while(amOrpmIndex < AMorPM_arr.length)
        {
            if(AMorPM_arr[amOrpmIndex].equals(amOrPm2))
            {
                break;
            }
            amOrpmIndex++;
        }

        time_spinner2.setSelection(amOrpmIndex);

        //create event button
        editEventImageButton = (Button)findViewById(R.id.create_events_button);

        editEventImageButton.setOnClickListener(new View.OnClickListener() {
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


                    String date_readable = month_spinner.getSelectedItem().toString() + day_spinner.getSelectedItem().toString();
                    String time = hour1.getText().toString() + ":" + minute1.getText().toString() + (time_spinner1.getSelectedItem().toString()).toUpperCase() + " - "
                            + hour2.getText().toString() + ":" + minute2.getText().toString() + (time_spinner2.getSelectedItem().toString()).toUpperCase();
                    String description_text = description.getText().toString().trim();
                    String location_text = location.getText().toString().trim();
                    String selected_tag = tag_spinner.getSelectedItem().toString().toLowerCase();
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

                    //make the changes
                    FirebaseDatabase.getInstance().getReference().child("Events").child(event.getEventId()).child("date").setValue(datetime_text);
                    FirebaseDatabase.getInstance().getReference().child("Events").child(event.getEventId()).child("date_readable").setValue(date_readable);
                    FirebaseDatabase.getInstance().getReference().child("Events").child(event.getEventId()).child("description").setValue(description_text);
                    FirebaseDatabase.getInstance().getReference().child("Events").child(event.getEventId()).child("location").setValue(location_text);
                    FirebaseDatabase.getInstance().getReference().child("Events").child(event.getEventId()).child("name").setValue(name_text);
                    FirebaseDatabase.getInstance().getReference().child("Events").child(event.getEventId()).child("tag").setValue(selected_tag);
                    FirebaseDatabase.getInstance().getReference().child("Events").child(event.getEventId()).child("time").setValue(time);

                    //end activity
                    finish();

                }
                else{
                    Toast.makeText(EditEventActivity.this, error_message, Toast.LENGTH_LONG).show();
                }


            }
        });

    }
}


