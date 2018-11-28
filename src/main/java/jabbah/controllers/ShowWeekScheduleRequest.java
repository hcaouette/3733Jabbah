
public class ShowWeekScheduleRequest {
	String date;
	String scheduleID;
	
	public ShowWeekScheduleRequest(String date, String id) {
		this.date = date;
		this.scheduleID = id;
	}
	
	public String toString() {
		return "Show week starting with date: " + date + 
				" within schedule: " + scheduleID ;
	}
}
