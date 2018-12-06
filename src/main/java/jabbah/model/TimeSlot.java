package jabbah.model;

import java.sql.Date;

public class TimeSlot {
    String startTime;
	int duration;
    String participant;
    boolean isOpen;

    // to know which day within which schedule it is associated with
    Date idDays;
    String orgAccessCode;

    public TimeSlot(String startTime, int duration, Date date, String id) {//with no parameters,
        // constructor will be used for organizers
        this.isOpen = true;
        this.duration = duration;
        this.participant = null;
        this.startTime = startTime;
        this.idDays = date;
        this.orgAccessCode = id;
    }

    //getter functions
    public boolean open() {
    	return this.isOpen;
    }
    public int getDuration() {
    	return this.duration;
    }
    public String getParticipant() {
    	return this.participant;
    }
    public String getTime() {
    	return this.startTime;
    }
    public Date getDate() {
    	return this.idDays;
    }
    public String getScheduleID() {
    	return this.orgAccessCode;
    }

    //time slot states
    public void openSlot() {
    	this.isOpen = true;
    	this.participant = null;
    }
    public void closeSlot() {
    	this.isOpen = false;
    }
    public void cancel() {
    	this.participant = null;
    }
    public void book(String participant) {
    	this.participant = participant;
    }

}