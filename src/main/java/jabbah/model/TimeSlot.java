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
    
    //getter functions
    public boolean open() {
    	return this.isOpen;
    }
    public int getDuration() {
    	return this.duration;
    }
    public User getParticipant() {
    	return this.participant;
    }
    public String getTime() {
    	return this.startTime;
    }
    
    //timeslot states
    public void openSlot() {
    	this.isOpen = true;
    	this.participant = null;
    }
    public void closeSlot() {
    	this.isOpen = false;
    	this.participant = null;
    }
    public void cancel() {
    	this.isOpen = true;
    	this.participant = null;
    }
    public void book(User participant) {
    	this.isOpen = false;
    	this.participant = participant;
    }

}