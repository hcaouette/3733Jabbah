function newParticipant(){
    let output = "<label for='viewDayText'>Enter the day of the month here</label>";
    output+="<br><textarea id='viewDayText' rows='2' cols='20'></textarea>";
    output+="<br><br>";
    output+= "<label for='viewYearText'>Enter the year here</label>";
    output+="<br><textarea id='viewYearText' rows='2' cols='20'></textarea>";
    output+="<br><br>";
    output+= "<label for='viewMonthText'>Enter the month here</label>";
    output+="<br><textarea id='viewMonthText' rows='2' cols='20'></textarea>";
    output+="<br><br>";
    output+="<button type='button' onclick='pullSchedule()'>View Week</button>";
    
    output+="<br><br>";
    output+= "<label for='viewWeekdayText'>Enter the weekday here</label>";
    output+="<br><textarea id='viewWeekdayText' rows='2' cols='20'></textarea>";
    output+="<br><br>";
    output+= "<label for='viewStartTimeText'>Enter the starting time here</label>";
    output+="<br><textarea id='viewStartTimeText' rows='2' cols='20'></textarea>";
    output+="<br><br>";
    output+="<button type='button' onclick='pullSchedule()'>View Timeslots</button>"

    document.getElementById("panelPrint").innerHTML = output;
}