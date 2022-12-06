package com.example.smartshopper.models;

public class ActivityTimestamp implements Comparable<ActivityTimestamp> {

    String activityMessage;
    Long timeStamp;

    public ActivityTimestamp(String activityMessage, Long timeStamp){
        this.activityMessage = activityMessage;
        this.timeStamp = timeStamp;
    }

    public String getActivityMessage() {
        return activityMessage;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public int compareTo(ActivityTimestamp o) {
        return Long.compare(this.timeStamp, o.getTimeStamp());
    }
}
