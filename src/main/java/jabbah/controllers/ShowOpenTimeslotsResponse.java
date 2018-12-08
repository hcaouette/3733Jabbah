package jabbah.controllers;

import java.util.ArrayList;
import java.util.List;

import jabbah.model.TimeSlot;

public class ShowOpenTimeslotsResponse {
    public final List<TimeSlot> list;
    public final int httpCode;

    public ShowOpenTimeslotsResponse (List<TimeSlot> list, int code) {
        this.list = list;
        this.httpCode = code;
    }

    public ShowOpenTimeslotsResponse (int code) {
        this.list = new ArrayList<TimeSlot>();
        this.httpCode = code;
    }

    @Override
    public String toString() {
        if (list == null) { return "Timeslots"; }
        return "ShowOpenTimeslots(" + list.size() + ")";
    }
}