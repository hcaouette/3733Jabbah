package jabbah.model;

import java.util.ArrayList;

import com.google.gson.Gson;

public class TestingArrays {
	//String[] myStrings = { "One", "Two", "Three", "Four", "Five"};
	ArrayList<String> myStrings =new ArrayList<>();
	ArrayList<String> myStrings2 =new ArrayList<>();
	
	public TestingArrays(String[]s ) {
		//myStrings = s;
		for (String x : s) {
			myStrings.add(x);
			myStrings2.add(x);
		}
	}
	
	public String toString() {
        return "Test (" + myStrings + ")";
    }
	
	public static void main(String[] args) {
		TestingArrays x = new TestingArrays(new String[] { "abc", "asdsd"});
		System.out.println(new Gson().toJson(x));
	}
}
