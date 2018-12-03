package jabbah.model;

import java.sql.Date;
import java.util.ArrayList;

public class DaysInSchedule {
    Date day;
    ArrayList<TimeSlot> slots;
    
    public DaysInSchedule(Date d) {//with no parameters,
        // constructor will be used for organizers
        this.day = d;
        this.slots = new ArrayList<TimeSlot>();
    }
    
    //getter function
    public Date getDate() {
    	return this.day;
    }
    
    public boolean addTimeSlot(TimeSlot slot) {
    	
    	for(int i = 0; i < this.slots.size(); i++) {
    		if (slots.get(i).startTime.equals(slot.startTime))
    			return false; //slot already exist
    	}
    	
    	this.slots.add(slot);
    	return true;
    }

}