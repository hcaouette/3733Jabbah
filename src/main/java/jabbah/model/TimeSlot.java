package jabbah.model;

public class TimeSlot {
    boolean isOpen;
    int duration;
    User participant;
    String startTime;
    
    public TimeSlot(String startTime, int duration) {//with no parameters,
        // constructor will be used for organizers
        this.isOpen = false;
        this.duration = duration;
        this.participant = null;
        this.startTime = startTime;
    }

}