package jabbah.model;

import java.util.Random;

public class User {
    String name;
    String accessCode;
    String permissions;
    
    // to know which schedule is associated with this user
    String orgAccessCode; 
    
    public User(String name, String accessCode, String id) {
    	this.name = name;
    	this.accessCode = accessCode;
    	setPermission(accessCode);
    	this.orgAccessCode = id;
    }
    /*
    public User(String name, String orgPermission) {//organizers
        //name themselves and have another string showing their
        //permission is explicitly stated
        this.name = name; 
        this.accessCode = this.generateAccessCode(); //generate an access code for the organizer
        this.permissions = "Organizer";
    }
    
    public User(String name) {//with name parameter only,
        // constructor will be used for participantss
        this.name = name;
        this.accessCode = this.generateAccessCode(); //generate a new access code for the participant
        this.permissions = "Participant";
    }
    
    public User(String name, boolean isAdmin) {//with name and isAdmin parameters,
        // constructor will be used for System Admininstrators
        this.name = name;
        this.accessCode = this.generateAccessCode();
        this.permissions = "SysAdmin";
    }*/
    
    public String generateAccessCode() { //generates a random 10 letter access code
        //with a last letter indicating permissions
        //can adjust this later
        String code = "";
        for (int i = 0; i < 10; i++) {
            Random letterChooser = new Random();
            int asciiValue = 65 + letterChooser.nextInt(26);
            char randomChar = (char)asciiValue;
            code += randomChar;
        }
        //adding an 11th letter indicating permissions
        if (permissions.equals("SysAdmin")) {
            code += "s";
        }
        else if (permissions.equals("Organizer")) {
            code += "o";
        }
        else if (permissions.equals("Participant")) {
            code += "p";
        }
        //now make sure that there are no collisions with any other 
        //accesscode in the database
        //will move this to DAO
        
        return code;
    }
    
    //check accessCode and set the appropriate permission
    private boolean setPermission(String code) {
    	if(code.endsWith("O")) {
    		this.permissions = "Organizer";
    		return true;
    	}
    	else if(code.endsWith("S")) {
    		this.permissions = "SysAdmin";
    		return true;
    	}
    	else if(code.endsWith("P")) {
    		this.permissions = "Participant";
    		return true;
    	}
    	else
    		return false; //false case would be the accessCode 11th char does not = O, S, nor P
    }
    
    //getter functions
    public String getName() {
    	return this.name;
    }
    public String getCode() {
    	return this.accessCode;
    }
    public String getPermission() {
    	return this.permissions;
    }
    public String getScheduleID() {
    	return this.orgAccessCode;
    }
    
    //setter function
    public void editName(String newName) {
    	this.name = newName;
    }
}
