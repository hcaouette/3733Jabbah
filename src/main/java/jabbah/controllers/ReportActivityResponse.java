package jabbah.controllers;

import java.util.ArrayList;
import java.util.List;

import jabbah.model.Schedule;

public class ReportActivityResponse {
    public final List<Schedule> list;
    public final int httpCode;

    public ReportActivityResponse (List<Schedule> list, int code) {
        this.list = list;
        this.httpCode = code;
    }

    public ReportActivityResponse (int code) {
        this.list = new ArrayList<Schedule>();
        this.httpCode = code;
    }

    @Override
    public String toString() {
        if (list == null) { return "Timeslots"; }
        return "ShowOpenTimeslots(" + list.size() + ")";
    }
}