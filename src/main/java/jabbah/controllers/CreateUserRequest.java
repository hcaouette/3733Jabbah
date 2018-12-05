package jabbah.controllers;

public class CreateUserRequest {
    String name;
    String accessCode;
    String id;

    public CreateUserRequest (String name, String accessCode, String id) {
        this.name = name;
        this.accessCode = accessCode;
        this.id = id;
        
    }

    @Override
    public String toString() {
        return "Create(" + name + "," + accessCode + "," +
   id + ")";
    }
}
