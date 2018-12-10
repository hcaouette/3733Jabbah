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

    public TimeSlot(String startTime, int duration, Date date, String id) {//with no parameters,
        // constructor will be used for organizers
        this.isOpen = true;
        this.duration = duration;
        this.participant = null;
        this.startTime = startTime;
        this.idDays = date;
        this.setOrgAccessCode(id);
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

    //time slot states
    public void openSlot() {
    	this.setOpen(true);
    	this.participant = null;
    }
    public void closeSlot() {
    	this.setOpen(false);
    }
    public void cancel() {
    	this.participant = null;
    }
    public void book(String participant) {
    	this.participant = participant;
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
                 "," + idDays + "," + orgAccessCode	 + ")";
    }
}