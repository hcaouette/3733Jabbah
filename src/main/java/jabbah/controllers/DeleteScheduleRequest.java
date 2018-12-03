package jabbah.controllers;

public class DeleteScheduleRequest {
    public final String orgAccessCode;

    public DeleteScheduleRequest (String n) {
        this.orgAccessCode = n;
    }

    @Override
    public String toString() {
        return "Delete(" + orgAccessCode + ")";
    }
}
