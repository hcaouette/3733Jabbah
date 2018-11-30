function firstLoad(){
    let output = "<label for='loginText'>Enter your access code here, if you've logged in before, or have an access code for your organizer's schedule</label>";
    output+="<br><textarea id='loginText' rows='2' cols='20'></textarea>";
    output+="<button type='button' onclick='openViewPanel()'>Login</button>";
    output+="<br><br>";
    output+="<label for='newOrganizer'>Enter a Username to get started as a new Organizer</label>";
    output+="<br><textarea id='newOrganizer' rows='2' cols='20'></textarea>";
    output+=" <button type='button' onclick='openCreateMenu()'>Create Schedule</button>";
    output+="<br><br>";
    output+="<label for='userText'>Enter a username and the access code from your organizer to get started as a participant</label>";
    output+="<br><textarea id='userText' rows='2' cols='20'></textarea>";
    output+="  <textarea id='accessCode' rows='2' cols='20'></textarea>";
    output+="   <button type='button' onclick='newParticipant()'>Participant</button>";

    document.getElementById("panelPrint").innerHTML = output;
}

function loadCal() {
    let interval = document.getElementById("meetingInterval").value;
    let numIntervals = 540 / interval;
    let myTable = "<table><tr><td style='font-size:16px;'>TIME</td>";
    myTable += "<td>MON</td>";
    myTable += "<td>TUE</td>";
    myTable += "<td>WED</td>";
    myTable += "<td>THU</td>";
    myTable += "<td>FRI</td></tr>";

    for (let i = 0; i < numIntervals; i++) {
        let currTime = (i*interval);
        let nexTime = ((i+1)*interval);
        myTable += "<tr><td>"+currTime+"-"+nexTime+"</td>";
        myTable += "<td><button type='button' onclick='bookFunc()'>Click Me!</button></td>";
        myTable += "<td><button type='button' onclick='bookFunc()'>Click Me!</button></td>";
        myTable += "<td><button type='button' onclick='bookFunc()'>Click Me!</button></td>";
        myTable += "<td><button type='button' onclick='bookFunc()'>Click Me!</button></td>";
        myTable += "<td><button type='button' onclick='bookFunc()'>Click Me!</button></td></tr>";
    }

    myTable += "</table>";
    /**console.log(myTable);*/
    document.getElementById("tablePrint").innerHTML = myTable;
}

function bookFunc(){
    window.confirm("Please confirm you want to book");
}

function openViewPanel(){
    let str = document.getElementById('loginText').value;
    let userType = str//.slice(10);
    console.log(userType);
    let output = "";
    if(userType === "O"){
		Organizer();
    }else if(userType === "P"){
		newParticipant();
    }else if(userType === "S"){
        SystemAdmin();
    }
}

function openCreateMenu(){
    let output = "";
    output+="<p>Enter a starting and ending date:</p>";
    output+="<textarea id='createDate' rows='2' cols='20' placeholder='mm/dd/yyyy'></textarea>";
    output+="<textarea id='endDate' rows='2' cols='20' placeholder='mm/dd/yyyy'></textarea><br>";
    output+="<br><p>Enter a starting and ending time of day:</p>";
    output+="<textarea id='startTime' rows='2' cols='20' placeholder='hh:mm'></textarea>";
    output+="<textarea id='endTime' rows='2' cols='20' placeholder='hh:mm'></textarea>";
    output+="<br><p>Enter a time interval in minutes for meetings:</p>";
    output+="<textarea id='meetingInterval' rows='2' cols='20' placeholder='30'></textarea><br>";
    output+="";
    output+="<br><button type='button' onclick='createSchedule()'>Create Schedule</button>";
    document.getElementById("panelPrint").innerHTML = output;
    console.log("we gottem, boys");
}
function createSchedule(){
    //name creation is handled on the backend so it can be checked against the server
    let startDay = document.getElementById("createDate").value;
    let endDay = document.getElementById("endDate").value;
    let startTime = document.getElementById("startTime").value;
    let endTime = document.getElementById("endTime").value;
    let meetingInterval = document.getElementById("meetingInterval").value;
    //console.log(startDay, endDay, startTime, endTime, meetingInterval);
    /*JSON stuff*/
    let subObj = {firstDay:startDay, lastDay:endDay, startTime:startTime, endTime:endTime, Interval:meetingInterval};
    let subJSON = JSON.stringify(subObj);
    /*Lambda stuff*/
    /**
     * */

    console.log("creating");
    loadCal();
}

function newParticipant(){
    let output = "<p> User: Participant </p>" 
    output+="<label for='viewDayText'>Enter the day of the month here</label>";
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

function SystemAdmin(){
	let output = "<p> User: System Administrator </p>";
	
	output+="<p>View Schedules created within the past X hours. </p>";
	output+="<label for = 'numHours'>Enter the number of hours here:</label>";
	output+="<br><textarea id='numHours' rows='2' cols='20'></textarea>";
    output+="<br><br>";
    output+="<button type = 'button' onclick = 'listSchedules()'>View</button>";
    
    output+="<p>View/Delete Schedules that are more than X days old. </p>";
	output+="<label for = 'numDays'>Enter the number of days here:</label>";
	output+="<br><textarea id='numDays' rows='2' cols='20'></textarea>";
    output+="<br><br>";
    output+="<button type = 'button' onclick = 'listSchedules()'>View</button>";
    output+="<button type = 'button' onclick = 'deleteAllSchedules()'>Delete</button>";
    
	document.getElementById("panelPrint").innerHTML = output;
}

function Organizer(){
	let output = "<p> User: Organizer </p>";
	
	output+="<label for = 'month'>Enter month here:</label>";
	output+="<br><textarea id='month' rows='2' cols='20'></textarea>";
	output+="<br><label for = 'dayOfMonth'>Enter day of month here:</label>";
	output+="<br><textarea id='dayOfMonth' rows='2' cols='20'></textarea>";
	output+="<br><label for = 'year'>Enter year here:</label>";
	output+="<br><textarea id='year' rows='2' cols='20'></textarea>";
    output+="<br><br>";
    output+="<button type = 'button' onclick = 'pullSchedule()'>View Schedule</button>";
    output+="<button type = 'button' onclick = 'extendSchedule()'>Extend Schedule</button>";
    
    output+="<p>Close or Open time slots on a given day or all time slots at a given time </p>";
	output+="<label for = 'date'>Enter date here:</label>";
	output+="<br><textarea id='numDays' rows='2' cols='20'></textarea>";
    output+="<br><label for 'time'>Or enter time here:</label>";
    output+="<br><textarea id = 'time' rows='2' cols='20' placeholder='hh:mm'></textarea>";
    output+="<br><br>";
    output+="<button type = 'button' onclick = 'closeTimeSlot()'>Close Time Slots</button>";
    output+="<button type = 'button' onclick = 'openTimeSlot()'>Open Time Slots</button>";
    
    output+="<br><br>";
    output+="<button type = 'button' onclick = 'deleteSchedule()'>Delete Schedule</button>";
	
	document.getElementById("panelPrint").innerHTML = output;
}