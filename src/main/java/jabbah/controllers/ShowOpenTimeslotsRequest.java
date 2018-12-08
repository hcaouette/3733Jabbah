package jabbah.controllers;

public class ShowOpenTimeslotsRequest {

    String accessCode;
    String monthDay;
    String startingTime;
    String weekday;
    String year;
    String month;
    //ArrayList<String> identifiers = new ArrayList<>();
    //ArrayList<String> values = new ArrayList<>();

    /*public TestingArrays(String[]s ) {
    //myStrings = s;
    for (String x : s) {
    myStrings.add(x);
    myStrings2.add(x);
    }
    }*/

    /*public String toString() {
            return "Test (" + myStrings + ")";
        }*/

    /*public static void main(String[] args) {
    TestingArrays x = new TestingArrays(new String[] { "abc", "asdsd"});
    System.out.println(new Gson().toJson(x));
    } */
    //}
    public ShowOpenTimeslotsRequest (String accessCode) {

        this.accessCode = accessCode;
        this.monthDay = null;
        this.startingTime = null;
        this.weekday = null;
        this.year = null;
        this.month = null;
    }

    public ShowOpenTimeslotsRequest (String accessCode, String monthDay, String startingTime,
            String weekday, String year, String month) {

        this.accessCode = accessCode;
        this.monthDay = monthDay;
        this.startingTime = startingTime;
        this.weekday = weekday;
        this.year = year;
        this.month = month;

    }

    /*public ShowOpenTimeslotsRequest (String accessCode, ArrayList<String> otherParams) {

        this.accessCode = accessCode;
        this.monthDay = monthDay;
        this.startingTime = startingTime;
        this.weekday = weekday;
        this.year = year;
        this.month = month;

    }*/

    @Override
    public String toString() {
        return "Get(" + accessCode + "," + monthDay + "," + startingTime + "," + weekday +
                 "," + year + "," + month + ")";
    }
}