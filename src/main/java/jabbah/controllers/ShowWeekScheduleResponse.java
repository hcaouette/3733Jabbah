
public class ShowWeekScheduleResponse {
	List<DayInSchedule> week;
	int httpCode;
	
	public ShowWeekSchedule(List<DayInSchedule> week, int code) {
		this.week = week;
		this.httpCode = code;
	}
	
	public ShowWeekSchedule(int code) {
		this.week = new ArrayList<DayInSchedule>();
		this.httpCode = code;
	}
	
	public String toString() {
		if(week == null)
			return "Given date is not within this Schedule's boundaries";
		return "Week";
	}
}
