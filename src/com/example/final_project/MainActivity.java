package com.example.final_project;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);        

		// Access the default SharedPreferences
		SharedPreferences preferences = 
			PreferenceManager.getDefaultSharedPreferences(this);
		// The SharedPreferences editor - must use commit() to submit changes
		SharedPreferences.Editor editor = preferences.edit();

		editor.putBoolean("siren", false);
		editor.putBoolean("dogs", false);
		editor.putBoolean("flash", false);
		
		
		editor.putBoolean("firstTime", true); //debugging only!!!!
			
		editor.commit();
		
		if(preferences.getBoolean("firstTime", true)){			

		editor.putBoolean("firstTime", false);
		editor.putBoolean("helpIsClicked", false);
		editor.putBoolean("sirenIsChecked", false);
		editor.putBoolean("cameraIsChecked", false);
		editor.putBoolean("flashIsChecked", false);


		editor.putString("helpSms", "Help Me!");
		editor.putString("fineSms", "I'm fine");
		editor.putString("password", "");

		editor.putString("name1", "");
		editor.putString("name2", "");
		editor.putString("name3", "");

		editor.putString("phone1", null);
		editor.putString("phone2", null);
		editor.putString("phone3", null);


		editor.commit();
		Intent intent = new Intent(this,SettingsActivity.class);
		startActivity(intent);
		}
		else{
			
		
		Intent intent = new Intent(this,FirstActivity.class);
		startActivity(intent);
		setContentView(R.layout.activity_main);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
