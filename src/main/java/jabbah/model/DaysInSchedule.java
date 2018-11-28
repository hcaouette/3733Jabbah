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

}