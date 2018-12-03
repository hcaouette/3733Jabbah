package jabbah.model;

import java.sql.Date;
import java.util.ArrayList;

public class DaysInSchedule {
    Date day;
    String orgAccessCode;
    
    public DaysInSchedule(Date d, String id) {//with no parameters,
        // constructor will be used for organizers
        this.day = d;
        this.orgAccessCode = id;
    }
    
    //getter function
    public Date getDate() {
    	return this.day;
    }
    public String getScheduleID() {
    	return this.orgAccessCode;
    }

}