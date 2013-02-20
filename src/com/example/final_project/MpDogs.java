package com.example.final_project;


import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;

// singleton implementation of dogs siren player
public class MpDogs extends Activity implements Mpable {

	private final static MpDogs instance = new MpDogs();
	private MediaPlayer mpD;

	//constructor
	private MpDogs(){
		mpD=null;
	}
	

//-----------------------methods-------------------------//
	
	//get instance
	public static MpDogs getInstance(){
		return instance;
	}
	
	//pause the player
	public void pause(){
		mpD.pause();
	}
	
	//play Mp from beginning
	public void play(){
		mpD.seekTo(0);
		mpD.start();
	}
	
	//creates a new player
	public void create(Context con){
		if (mpD != null)
			mpD.release();
		mpD = MediaPlayer.create(con, R.raw.dogi);
		mpD.setLooping(true);
	}

	//release the player
	public void kill(){
		if (mpD != null){
			mpD.release();
			mpD = null;
		}			
	}
}
