package com.example.final_project;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	// ----- general declarations ------ //

//	Intent mainIntent;
	
	SharedPreferences preferences;
	SharedPreferences.Editor editor;

	ImageButton contactButton1;
	ImageButton contactButton2;
	ImageButton contactButton3;

	CheckBox sirenCheckBox;
	CheckBox cameraCheckBox;
	CheckBox flashCheckBox;

	TextView contactName1;
	TextView contactName2;
	TextView contactName3;
	
	EditText helpSmsEdit;
	EditText iAmFineSmsEdit;
	EditText iAmFinePassEdit;
	
	ImageButton back;

	// --------------------------------- //

	ContactClicker clicker = new ContactClicker(); //initialize the general listner class for the contacts buttons

	
	public void setContacts(){
		
		if(preferences.getString("name1", null) != null){
			String name = preferences.getString("name1", null);
			contactName1.setText(name);
		}

		if(preferences.getString("name2", null) != null){
			String name = preferences.getString("name2", null);
			contactName2.setText(name);
		}

		if(preferences.getString("name3", null) != null){
			String name = preferences.getString("name3", null);
			contactName3.setText(name);
		}
	}
	
	public void setSiren(){ 
		if(preferences.getBoolean("sirenIsChecked", true)){
			sirenCheckBox.setChecked(true);
		}

		else{
			sirenCheckBox.setChecked(false);
		}
	}
	
	public void setCamera(){
		if(preferences.getBoolean("cameraIsChecked", true)){
			cameraCheckBox.setChecked(true);
		}

		else{
			cameraCheckBox.setChecked(false);
		}
	}
	
	public void setFlash(){
		if(preferences.getBoolean("flashIsChecked", true)){
			flashCheckBox.setChecked(true);
		}

		else{
			flashCheckBox.setChecked(false);
		}
	}
	
	public void setHelpSms(){
		if(preferences.getString("helpSms", null) != null){
			String name = preferences.getString("helpSms", null);
			helpSmsEdit.setText(name);
		}
		else{
			String name = "Help me, I am in trouble!";
			helpSmsEdit.setText(name);
		}
	}
	
	public void setIAmFineSms(){
		if(preferences.getString("fineSms", null) != null){
			String name = preferences.getString("fineSms", null);
			iAmFineSmsEdit.setText(name);
		}
		else{
			String name = "Dont worry, I am fine";
			helpSmsEdit.setText(name);
		}
	}
	
	public void setPassword(){
		if((preferences.getString("password", null) != null) && (!preferences.getString("password", null).equals(""))){
			String name = preferences.getString("password", null);
			iAmFinePassEdit.setText(name);
		}
		
	}
	
	
	// this function tells which contact button was clicked (1, 2 or 3)
	public int setIdCode(View v){
		int viewId = v.getId();
		if(viewId == R.id.add1) return 1;
		else if(viewId == R.id.add2) return 2;
		else return 3;

	}

	// general listner class for the contacts buttons
	public class ContactClicker implements OnClickListener{

		@Override
		public void onClick(View v) {
			int idCode = setIdCode(v);
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, idCode);

		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		
		final Intent firstIntent = new Intent(this, FirstActivity.class);
		// initializing shared preferences object's
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();

		// integrates the java objects with their appropriate xml code
		contactName1 = (TextView)this.findViewById(R.id.name1);
		contactName2 = (TextView)this.findViewById(R.id.name2);
		contactName3 = (TextView)this.findViewById(R.id.name3);

		contactButton1 = (ImageButton)this.findViewById(R.id.add1);
		contactButton2 = (ImageButton)this.findViewById(R.id.add2);
		contactButton3 = (ImageButton)this.findViewById(R.id.add3);

		sirenCheckBox = (CheckBox)this.findViewById(R.id.sirenCheck);
		cameraCheckBox = (CheckBox)this.findViewById(R.id.cameraCheck);
		flashCheckBox = (CheckBox)this.findViewById(R.id.flashCheck);
		
		helpSmsEdit = (EditText)this.findViewById(R.id.helpsms);
		iAmFineSmsEdit = (EditText)this.findViewById(R.id.imfinesms);
		iAmFinePassEdit = (EditText)this.findViewById(R.id.imfinepass);
		
		back = (ImageButton)this.findViewById(R.id.back);

		// -------------------------------------------------------------


		
		this.setContacts();  // check if the contacts had already been initialized before, and if so - displays the name
		this.setSiren();  // check if the siren checkbox is clicked and updates accordingly
		this.setCamera(); // check if the camera checkbox is clicked and updates accordingly
		this.setFlash(); // check if the flash checkbox is clicked and updates accordingly
		this.setHelpSms();
		this.setIAmFineSms();
		this.setPassword();
		
			
		

		//listening to users clicks on the contacts buttons
		contactButton1.setOnClickListener(clicker);
		contactButton2.setOnClickListener(clicker);
		contactButton3.setOnClickListener(clicker);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String smsHelp = helpSmsEdit.getText().toString();
				String smsIAmFine = iAmFineSmsEdit.getText().toString();
				String pass =iAmFinePassEdit.getText().toString();
				
				if (sirenCheckBox.isChecked()){		
					editor.putBoolean("sirenIsChecked", true);
					editor.commit();
				}
				else{
					editor.putBoolean("sirenIsChecked", false);
					editor.commit();
				}
				
				if (cameraCheckBox.isChecked()){		
					editor.putBoolean("cameraIsChecked", true);
					editor.commit();
				}
				else{
					editor.putBoolean("cameraIsChecked", false);
					editor.commit();
				}
				
				if (flashCheckBox.isChecked()){		
					editor.putBoolean("flashIsChecked", true);
					editor.commit();
				}
				else{
					editor.putBoolean("flashIsChecked", false);
					editor.commit();
				}
				
				editor.putString("helpSms", smsHelp);
				editor.putString("fineSms", smsIAmFine);
				editor.putString("password", pass);
				editor.commit();
				
				startActivity(firstIntent);
				
			}
		});



	}

	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			Uri contactData = data.getData();
			Cursor c =  managedQuery(contactData, null, null, null, null);
			Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
			String name = null;
			String phoneNumber = null;

			if (c.moveToFirst()) {
				name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); 

				while (phones.moveToNext()) 
				{
					phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				}

				if(reqCode == 1) {
					contactName1.setText(name);
					editor.putString("name1", name);
					editor.putString("phone1", phoneNumber);
					editor.commit();
				}

				else if(reqCode == 2) {
					contactName2.setText(name);
					editor.putString("name2", name);
					editor.putString("phone2", phoneNumber);
					editor.commit();
				}

				else {
					contactName3.setText(name);
					editor.putString("name3", name);
					editor.putString("phone3", phoneNumber);
					editor.commit();
				}
			}
		}



	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}
}