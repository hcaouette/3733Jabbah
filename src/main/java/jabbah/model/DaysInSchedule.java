package jabbah.model;

import java.sql.Date;

public class DaysInSchedule {
    Date day;
    ArrayList<TimeSlots> slots;
    
    public DaysInSchedule(Date d) {//with no parameters,
        // constructor will be used for organizers
        this.day = d;
        this.slots = new ArrayList<TimeSlots>();
    }

}