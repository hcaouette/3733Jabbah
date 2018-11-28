package jabbah.model;

import java.util.Random;

public class User {
    String name;
    String accessCode;
    String permissions;
    
    public User() {//with no parameters,
        // constructor will be used for organizers
        this.name = null; //organizer's names are unused
        this.accessCode = this.generateAccessCode(); //generate an access code for the organizer
        this.permissions = "Organizer";
    }
    
    public User(String name) {//with name parameter only,
        // constructor will be used for participantss
        this.name = name; //organizer's names are unused
        this.accessCode = this.generateAccessCode(); //generate a new access code for the participant
        this.permissions = "Participant";
    }
    
    public User(String name, boolean isAdmin) {//with name and isAdmin parameters,
        // constructor will be used for System Admininstrators
        this.name = name; //organizer's names are unused
        this.accessCode = this.generateAccessCode();
        this.permissions = "SysAdmin";
    }
    
    public String generateAccessCode() { //generates a random 10 letter access code
        //can adjust this later
        String code = "";
        for (int i = 0; i < 10; i++) {
            Random letterChooser = new Random();
            int asciiValue = 65 + letterChooser.nextInt(26);
            char randomChar = (char)asciiValue;
            code += randomChar;
        }
        return code;
    }
}
