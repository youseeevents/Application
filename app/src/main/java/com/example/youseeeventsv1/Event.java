package com.example.youseeeventsv1;
import android.os.Parcel;
import android.os.Parcelable;


public class Event implements Parcelable {

    private String eventId;
    protected String name;
    protected String organizer;
    private String description;
    private int counterGoing;

    private String date;
    private String date_readable;
    private String time;
    private String location;

    protected String tag;

    private static int eventIDCounter = 0;

    public Event(){}

    public Event(String eventId, String name, String organizer, String description, String date, String date_readable, String time, String location, String tag){
        // eventId will be set up by the database... figure that out
        this.name = name;
        this.organizer = organizer;
        this.description = description;

        this.date = date;
        this.date_readable = date_readable;
        this.time = time;
        this.location = location;
        this.counterGoing = 0;
        this.tag = tag;
        this.eventId = eventId;
    }

    public Event(String eventId, String name, String organizer, String description, String date, String date_readable, String time, String location, String tag, int counterGoing){
        // eventId will be set up by the database... figure that out
        this.name = name;
        this.organizer = organizer;
        this.description = description;

        this.date = date;
        this.date_readable = date_readable;
        this.time = time;
        this.location = location;
        this.counterGoing = 0;
        this.tag = tag;
        this.eventId = eventId;
        this.counterGoing = counterGoing;
    }

    public Event(Parcel p){
        // eventId will be set up by the database... figure that out
        this.name = p.readString();
        this.organizer = p.readString();
        this.description = p.readString();
        this.date = p.readString();
        this.date_readable = p.readString();
        this.time = p.readString();
        this.location = p.readString();
        this.counterGoing = p.readInt();
        this.tag = p.readString();
        this.eventId = p.readString();
    }

    /** SETTERS */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCounterGoing(int counterGoing) {
        this.counterGoing = counterGoing;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDate_readable(String date_readable) { this.date_readable = date_readable; }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTags(String tag) {
        this.tag = tag;
    }

    /** GETTERS */
    public String getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public String getOrganizer() { return organizer; }

    public String getDescription() {
        return description;
    }

    public int getCounterGoing() {
        return counterGoing;
    }

    public String getDate() {
        return date;
    }

    public String getDate_readable() { return date_readable;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getTag() {
        return tag;
    }

    /*
      PARCEABLEMETHODS
     */

    public static final Creator<Event> CREATOR = new Creator<Event>(){
        public Event createFromParcel(Parcel in){
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[0];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(organizer);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(date_readable);
        dest.writeString(time);
        dest.writeString(location);
        dest.writeInt(counterGoing);
        dest.writeString(tag);
        dest.writeString(eventId);
    }
}
