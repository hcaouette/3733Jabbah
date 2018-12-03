package jabbah.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import jabbah.model.*;

public class UserDAO {
	
	java.sql.Connection conn;
	
	public UserDAO() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
	}
	
	public User getUser(String code) throws Exception{
		try {
			User u = null;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM User WHERE accessCode=?;");
			ps.setString(1, code);
			ResultSet resultSet = ps.executeQuery();
			
			while (resultSet.next()) {
				u = generateUser(resultSet);
			}
			resultSet.close();
			ps.close();
			
			return u;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed in getting user: " + e.getMessage());
		}
	}
	
    public boolean deleteUser(User u) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM User WHERE accessCode = ?;");
            ps.setString(1, u.getCode());
            int numAffected = ps.executeUpdate();
            ps.close();
            
            return (numAffected == 1);

        } catch (Exception e) {
            throw new Exception("Failed to delete user: " + e.getMessage());
        }
    }

    public boolean updateUser(User u) throws Exception {
        try {
        	String query = "UPDATE User SET name=? WHERE accessCode=?;";
        	PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, u.getName());
            ps.setString(2, u.getCode());
            int numAffected = ps.executeUpdate();
            ps.close();
            
            return (numAffected == 1);
        } catch (Exception e) {
            throw new Exception("Failed to update user name: " + e.getMessage());
        }
    }
	
	public boolean addUser(User u, String scheduleID) throws Exception{
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM User WHERE accessCode=?;");
			ps.setString(1, u.getCode());
			ResultSet resultSet = ps.executeQuery();
			
			// already present?
			while (resultSet.next()) {
				User user = generateUser(resultSet);
				resultSet.close();
				return false;
			}
			
			ps = conn.prepareStatement("INSERT INTO User (name, accessCode, permissions, ScheduleID) values(?,?,?,?);");
			ps.setString(1, u.getName());
			ps.setString(2, u.getCode());
			ps.setString(3,  u.getPermission());
			ps.setString(4, scheduleID);
			ps.execute();
			return true;
			
		} catch (Exception e) {
			throw new Exception("Failed to insert new user: " + e.getMessage());
		}
	}
	
    public List<User> getAllUsers() throws Exception {
        
        List<User> allUsers = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM User";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                User u = generateUser(resultSet);
                allUsers.add(u);
            }
            resultSet.close();
            statement.close();
            return allUsers;

        } catch (Exception e) {
            throw new Exception("Failed in getting all users: " + e.getMessage());
        }
    }
	
    //fix to include accessCode
	private User generateUser(ResultSet resultSet) throws Exception{
		String name = resultSet.getString("name");
		String permission = resultSet.getString("permissions");
		if(permission.equals("Organizer"))
			return new User(name, permission);
		else if(permission.equals("SysAdmin"))
			return new User(name, true);
		else
			return new User(name);
	}
}
