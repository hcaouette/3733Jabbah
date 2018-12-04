package jabbah.controllers;

public class GetUserRequest {

    String accessCode;

    public GetUserRequest (String name, String accessCode, String id) {

        this.accessCode = accessCode;
        
    }

    @Override
    public String toString() {
        return "Get(" + accessCode + ")";
    }
}
