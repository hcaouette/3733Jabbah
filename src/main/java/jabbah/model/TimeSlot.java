package jabbah.model;

import java.sql.Date;

public class TimeSlot {
    String startTime;
	int duration;
    String participant;
    private boolean isOpen;

    // to know which day within which schedule it is associated with
    Date idDays;
    private String orgAccessCode;
    String name;

    public TimeSlot(String startTime, int duration, Date date, String id) {//with no parameters,
        // constructor will be used for organizers
        this.isOpen = true;
        this.duration = duration;
        this.participant = null;
        this.startTime = startTime;
        this.idDays = date;
        this.setOrgAccessCode(id);
        this.name = null;
    }

    //getter functions
    public boolean open() {
    	return this.isOpen();
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
    	return this.getOrgAccessCode();
    }
    public String getName() {
    	return this.name;
    }

    //time slot states
    public void openSlot() {
    	this.setOpen(true);
    	this.participant = null;
    }
    public void closeSlot() {
    	this.setOpen(false);
    	this.name = null;
    }
    public void cancel() {
    	this.participant = null;
    	this.name = null;
    }
    public void book(String participant, String name) {
    	this.participant = participant;
    	this.name = name;
    }

    public String getOrgAccessCode() {
        return orgAccessCode;
    }

    public void setOrgAccessCode(String orgAccessCode) {
        this.orgAccessCode = orgAccessCode;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
    public String toString() {
        return "Fields(" + startTime + "," + duration + "," + participant + "," + isOpen +
                 "," + idDays + "," + orgAccessCode	+ "," + name + ")";
    }
}