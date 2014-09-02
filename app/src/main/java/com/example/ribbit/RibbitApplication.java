package com.example.ribbit;

import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

public class RibbitApplication extends Application{
	@Override
	public void onCreate() {
		
		  super.onCreate();
		  Parse.initialize(this, "Z7aHn7fpQ4nve6KeKF56Y5wTeaqNdtkz1t4uOP08", "Z87YVDsVBIqsiSoThLlNrRULQ9P0dhGgsFdDTDs3");
		  
		  ParseObject testObject = new ParseObject("TestObject");
		  testObject.put("foo", "bar");
		  testObject.saveInBackground();
		}

}
