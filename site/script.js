function firstLoad(){
    let output = "<label for='loginText'>Enter your access code here, if you've logged in before, or have an access code for your organizer's schedule</label>";
    output+="<br><textarea id='loginText' rows='2' cols='20'></textarea>";
    output+="<button type='button' onclick='pullSchedule()'>Login</button>";
    output+="<br><br>";
    output+="<label for='newOrganizer'>Enter a Username to get started as a new Organizer</label>";
    output+="<br><textarea id='newOrganizer' rows='2' cols='20'></textarea>";
    output+=" <button type='button' onclick='loadCal()'>Create Schedule</button>";
    output+="<br><br>";
    output+="<label for='userText'>Enter a username and the access code from your organizer to get started as a participant</label>";
    output+="<br><textarea id='userText' rows='2' cols='20'></textarea>";
    output+="  <textarea id='accessCode' rows='2' cols='20'></textarea>";
    output+="   <button type='button' onclick='newParticipant()'>Participant</button>";

    //console.log(output);
    document.getElementById("panelPrint").innerHTML = output;
}

function loadCal() {

    let interval = prompt("Please enter your desired interval in minutes", "30min");
    let numIntervals = 540 / interval;
    console.log(interval);
    console.log(numIntervals);

    let myTable = "<table><tr><td>TIME</td>";
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

function pullSchedule(){
    console.log("yeehaw");
}

function openCreateMenu(){}

function newParticipant(){}