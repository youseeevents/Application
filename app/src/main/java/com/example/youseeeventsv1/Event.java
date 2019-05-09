package com.example.youseeeventsv1;
import java.util.Date;


public class Event {

    private int eventId;
    private String name;
    private String eventDescription;
    private int eventCounterGoing;
    private int eventCounterInterested;

    private Date date;
    private String time;
    private String location;

    private String[] tags;

    private static int eventIDCounter = 0;

    public Event(String name, String eventDescription, Date date, String time, String location, String[]  tags){
        // eventId will be set up by the database... figure that out
        this.name = name;
        this.eventDescription = eventDescription;

        this.date = date;
        this.time = time;
        this.location = location;
        this.eventCounterGoing = 0;
        this.eventCounterInterested = 0;
        this.tags = tags;
        this.eventId = eventIDCounter;
        eventIDCounter++;
    }
    /** SETTERS */
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public void setEventCounterGoing(int eventCounterGoing) {
        this.eventCounterGoing = eventCounterGoing;
    }

    public void setEventCounterInterested(int eventCounterInterested) {
        this.eventCounterInterested = eventCounterInterested;
    }

    public void setDate(Date date) {
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

    public String getEventDescription() {
        return eventDescription;
    }

    public int getEventCounterGoing() {
        return eventCounterGoing;
    }

    public int getEventCounterInterested() {
        return eventCounterInterested;
    }

    public Date getDate() {
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
}
