package com.example.final_project;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera.Parameters;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ExtrasActivity extends Activity {

	ImageButton imBPolice,imBAmbulance,imBFire,imBSiren,imBDogs,imBFlash;
	Button bBack,im_fineButton;


	AnimationDrawable sirenFrameAnimation;
	int started;

	Parameters param;

	SharedPreferences preferences;
	SharedPreferences.Editor editor;

	LocationManager locationManager;
	LocationProvider provider;

	SmsManager sms;

	int flagState;
	int animstate;

	private void animate(String type) {
		if (type.compareTo("siren")==0){
			if (!preferences.getBoolean("siren", false)){
				
				sirenFrameAnimation.setOneShot(true);
				
			}
			else{
				sirenFrameAnimation.stop();
				sirenFrameAnimation.setOneShot(false);
				sirenFrameAnimation.start();
			}
		}
	}


	//calling listener (100,101,102)
	public class CallButtonsListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			call(""+v.getContentDescription());

		}
	}

	//end call listener - to return back to the application
	private class EndCallListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			//speaking state
			if(TelephonyManager.CALL_STATE_OFFHOOK == state) {
				flagState=1;
			}
			if(TelephonyManager.CALL_STATE_IDLE == state) {
				//call was ended
				if (flagState == 1){
					flagState = 0;
					//stop the state listener
					((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE)).listen(this, LISTEN_NONE);
					Intent intent = new Intent(ExtrasActivity.this, ExtrasActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
				//regular idle state
				else{
					flagState=0;
				}
			}
		}
	}

	//sounds listener 
	public class MpButtonsListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Mpable mp;
			String type;

			//initial to the right instance
			if("dogs".compareTo(""+v.getContentDescription()) == 0){
				mp = MpDogs.getInstance();
				type = "dogs";
			}
			else{
				mp = MpSiren.getInstance();
				type = "siren";
			}
			//start playing mp and animation
			if (!preferences.getBoolean(type, false)){
				mp.create(getApplicationContext());
				mp.play();
				editor.putBoolean(type, true);
				editor.commit();
				if(started==1){
					animate(type);
					Toast.makeText(getApplicationContext(), "start2", Toast.LENGTH_LONG).show();
				}
			}
			//stop playing mp and animation
			else{
				mp.pause();
				mp.kill();
				editor.putBoolean(type, false);
				editor.commit();
				animate(type);
				Toast.makeText(getApplicationContext(), "stop2", Toast.LENGTH_LONG).show();
			}


		}
	}

	//phone-calling function
	private void call(String num) {
		try {
			EndCallListener callListener = new EndCallListener();
			TelephonyManager mTM = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
			mTM.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:"+num));
			startActivity(callIntent);
		} catch (ActivityNotFoundException e) {
			Log.e("helloandroid dialing example", "Call failed", e);
		}
	}

	//location listener
	final LocationListener locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			String myLocation = "https://maps.google.com/maps?q="+latitude+","+longitude+"&z=18";
			String userHelpMessage = (preferences.getString("helpSms", null));
			String messegeToSend = userHelpMessage+"\n  my location is :  "+myLocation;

			//      Toast.makeText(getApplicationContext(), "GPS not enable", Toast.LENGTH_LONG).show();

			if ((preferences.getString("phone1", null)) != null){
				String phoneNum1 = (preferences.getString("phone1", null));
				sms.sendTextMessage(phoneNum1, null, messegeToSend, null, null);       
			}

			if ((preferences.getString("phone2", null)) != null){
				String phoneNum2 = (preferences.getString("phone2", null));
				sms.sendTextMessage(phoneNum2, null, messegeToSend, null, null);       
			}

			if ((preferences.getString("phone3", null)) != null){
				String phoneNum3 = (preferences.getString("phone3", null));
				sms.sendTextMessage(phoneNum3, null, messegeToSend, null, null);       
			}

		}


		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_extras);

		/////////////////////////////-frfrfr
		flagState=0;
		animstate=0;
		started=0;

		// Access the default SharedPreferences
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		// The SharedPreferences editor - must use commit() to submit changes
		editor = preferences.edit();

		//open the camera

		param = CamMan.getInstance().getParameters();// get camera Parameters
		param.getFlashMode();

		//initiate sms manager
		sms = SmsManager.getDefault();

		//initiate GPS manager
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!gpsEnabled) {
			Toast.makeText(getApplicationContext(), "GPS not enable", Toast.LENGTH_LONG).show();
			Log.d("myLocation","GPS not enables");
		}
		provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);


		// Set an EditText view to get user password   
		final EditText passText = new EditText(this);//(EditText)wind.findViewById((R.id.rate));
		passText.setTransformationMethod(PasswordTransformationMethod.getInstance());

		final AlertDialog.Builder builder = new AlertDialog.Builder(this); 
		builder.setMessage("Enter confirmation password:");
		builder.setView(passText);

		builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				String value = passText.getText().toString();
				passText.setText("");
				if (value.compareTo(preferences.getString("password", null))==0){
					//check if in "panic" mode
					if (!preferences.getBoolean("helpIsClicked", false)){			
					}
					//if help me is clicked
					else{
						//unclick on help me
						editor.putBoolean("helpIsClicked", false);
						editor.commit();
						//turn off siren if siren is on
						if(!preferences.getBoolean("siren", false)){
						}
						else{
							imBSiren.performClick();
						}
						//turn off flash-light if flash is on
						if(!preferences.getBoolean("flas", false)){
						}
						else{
							imBFlash.performClick();
						}
						//return to first intent
						Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
						startActivity(intent);
					}
				}
				else{
					Toast.makeText(getApplicationContext(), "incorrect password", Toast.LENGTH_LONG).show();
				}

				return;
			} 
		}); 

		builder.setNegativeButton("Cancel", new	DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel(); 
			} 
		}); 

		final AlertDialog alert = builder.create(); 

		//set i'm fine button 
		im_fineButton = (Button)this.findViewById(R.id.fine);
		im_fineButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alert.show();
			}
		});

		//set call buttons
		imBPolice = (ImageButton) findViewById(R.id.ImgPolice);
		imBAmbulance = (ImageButton) findViewById(R.id.ImgAmbulance);
		imBFire = (ImageButton) findViewById(R.id.ImgFire);
		imBPolice.setOnClickListener(new CallButtonsListener());
		imBAmbulance.setOnClickListener(new CallButtonsListener());
		imBFire.setOnClickListener(new CallButtonsListener());


		//set sound and flash buttons
		imBSiren = (ImageButton) findViewById(R.id.ImgSiren);

		imBSiren.setBackgroundResource(R.layout.anima);
		//set siren animation
		sirenFrameAnimation = (AnimationDrawable) imBSiren.getBackground();
		imBSiren.setOnClickListener(new MpButtonsListener());

		imBDogs = (ImageButton) findViewById(R.id.ImageDogs);
		imBFlash = (ImageButton) findViewById(R.id.ImgFlash);

		imBDogs.setOnClickListener(new MpButtonsListener());

		imBFlash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//turn on flash
				if (!preferences.getBoolean("flash", false)){
					param.setFlashMode(Parameters.FLASH_MODE_TORCH); 
					CamMan.getInstance().setParameters(param);
					editor.putBoolean("flash", true);
					editor.commit();
				}
				//turn of flash
				else{
					param.setFlashMode(Parameters.FLASH_MODE_OFF);
					editor.putBoolean("flash", false);
					editor.commit();
				}

			}
		});

		//set back button
		bBack = (Button) findViewById(R.id.Extras_back);
		bBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if	(!preferences.getBoolean("helpIsClicked", false)){
					finish();
				}
				else{
					Toast.makeText(getApplicationContext(), "            back is disabled\nplease confirm you are ok first", Toast.LENGTH_LONG).show();
					editor.commit();
				}

			}
		});

		//-----------------------------check if in "panic" mode----------------////

		if (!preferences.getBoolean("helpIsClicked", false)){			
		}
		//if help me is clicked
		else{
			im_fineButton.setVisibility(View.VISIBLE);
			bBack.setVisibility(View.GONE);
			
			//turn on siren if siren is checked
			if(!preferences.getBoolean("sirenIsChecked", false)){
			}
			else{
				imBSiren.performClick();
			}
			//turn on flash-light if flash is checked
			if(!preferences.getBoolean("flashIsChecked", false)){
			}
			else{
				imBFlash.performClick();
			}
			if ((preferences.getString("phone1", null)) != null){
				String phoneNum1 = (preferences.getString("phone1", null));
				Toast.makeText(getBaseContext(), phoneNum1, Toast.LENGTH_SHORT).show();
				sms.sendTextMessage(phoneNum1, null, "hi", null, null);       
			}

			if ((preferences.getString("phone2", null)) != null){
				String phoneNum2 = (preferences.getString("phone2", null));
				sms.sendTextMessage(phoneNum2, null, "byush", null, null);       
			}
			//get gps and send messages
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					10000,          // 10-second interval.
					10,             // 10 meters.
					locationListener);
		}
	}

	@Override
	public void  onWindowFocusChanged(boolean hasFocus){
		if (hasFocus){
			started=1;
			animate("siren");
		}
	}

	@Override
	public void onPause(){
		super.onPause();  

		// Release the police siren
		MpSiren.getInstance().kill();
		editor.putBoolean("siren", false);
		editor.commit();

		// Release the dogs-siren
		MpDogs.getInstance().kill();
		editor.putBoolean("dogs", false);
		editor.commit();

		//Release the camera
		//		mycam.release();


	}

	@Override
	public void onBackPressed() {
		bBack.performClick();
	}

	@Override
	public void onResume(){
		super.onResume();
		//		mycam = Camera.open();
		//		param = mycam.getParameters();// get camera Parameters
		//		param.getFlashMode();
	}

}
