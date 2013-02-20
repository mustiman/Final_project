package com.example.final_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class FirstActivity extends Activity {
	//----- declarations ---------//

	Button extrasButton;
	Button settingsButton;
	Button im_fineButton;
	ImageButton helpButton;
	Intent extrasIntent;
	Intent settingsIntent;
	
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	//----------------------------//
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		
        // Access the default SharedPreferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // The SharedPreferences editor - must use commit() to submit changes
        editor = preferences.edit();

		extrasIntent = new Intent(this, ExtrasActivity.class);
		settingsIntent = new Intent(this, SettingsActivity.class);
		
	
	
		extrasButton = (Button)this.findViewById(R.id.Bextras);
		extrasButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(extrasIntent);
			}
		});
	

	        settingsButton = (Button)this.findViewById(R.id.Bsettings);	        
	        settingsButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(settingsIntent);
					
				}
			});
	        
	        helpButton = (ImageButton)this.findViewById(R.id.imbtHelp);	        
	        helpButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					editor.putBoolean("helpIsClicked", true);
			        editor.commit();
					startActivity(extrasIntent);
					
				}
			});
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}