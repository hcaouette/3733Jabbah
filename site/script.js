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
    let userType = str.slice(10);
    console.log(userType);
    let output = "";
    if(userType === "O"){
        output = "<p>Organizer</p>";
    }else if(userType === "P"){
        output = "<p>Participant</p>";
    }else if(userType === "S"){
        output = "<p>Sysadmin</p>";
    }
    document.getElementById("panelPrint").innerHTML = output;

}

function openCreateMenu(){
    let output = "";
    output+="<p>Enter a starting and ending date:</p>";
    output+="<textarea id='createDate' rows='2' cols='20' placeholder='mm/dd/yy'></textarea>";
    output+="<textarea id='endDate' rows='2' cols='20' placeholder='mm/dd/yy'></textarea><br>";
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

function newParticipant(){}