package com.example.youseeeventsv1;
import java.util.Date;


public class Event {
    private int eventId;
    private String name;
    private String eventDescription;
    private int eventCounter;

    private Date date;
    private int time;
    private String location;

    private String[] tags;

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

    public void setEventCounter(int eventCounter) {
        this.eventCounter = eventCounter;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(int time) {
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

    public int getEventCounter() {
        return eventCounter;
    }

    public Date getDate() {
        return date;
    }

    public int getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String[] getTags() {
        return tags;
    }
}
