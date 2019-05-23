package com.example.youseeeventsv1;
import android.os.Parcel;
import android.os.Parcelable;


public class Event implements Parcelable {

    private int eventId;
    private String name;
    private String description;
    private int eventCounterGoing;
    private int eventCounterInterested;

    private String date;
    private String time;
    private String location;

    private String[] tags;

    private static int eventIDCounter = 0;

    public Event(){}

    public Event(String name, String description, String date, String time, String location, String[]  tags){
        // eventId will be set up by the database... figure that out
        this.name = name;
        this.description = description;

        this.date = date;
        this.time = time;
        this.location = location;
        this.eventCounterGoing = 0;
        this.eventCounterInterested = 0;
        this.tags = tags;
        this.eventId = eventIDCounter;
        eventIDCounter++;
    }
    public Event(Parcel p){
        // eventId will be set up by the database... figure that out
        this.name = p.readString();
        this.description = p.readString();
        this.date = p.readString();
        this.time = p.readString();
        this.location = p.readString();
        this.eventCounterGoing = p.readInt();
        this.eventCounterInterested = p.readInt();
        //this.tags = p.readArray();
        this.eventId = p.readInt();
    }

    /** SETTERS */
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEventCounterGoing(int eventCounterGoing) {
        this.eventCounterGoing = eventCounterGoing;
    }

    public void setEventCounterInterested(int eventCounterInterested) {
        this.eventCounterInterested = eventCounterInterested;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    /** GETTERS */
    public int getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getEventCounterGoing() {
        return eventCounterGoing;
    }

    public int getEventCounterInterested() {
        return eventCounterInterested;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String[] getTags() {
        return tags;
    }

    /*
      PARCEABLEMETHODS
     */

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>(){
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
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(location);
        dest.writeInt(eventCounterGoing);
        dest.writeInt(eventCounterInterested);
        dest.writeArray(tags);
        dest.writeInt(eventId);
    }
}
