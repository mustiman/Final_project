package com.example.final_project;


import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;

//singleton implementation of dogs siren player
public class MpSiren extends Activity implements Mpable{

	private final static MpSiren instance = new MpSiren();
	private MediaPlayer mpS;
	
	//constructor
	private MpSiren(){
		mpS=null;
	}
	

	//-----------------------methods-------------------------//
		
	//get instance
	public static MpSiren getInstance(){
		return instance;
	}
	
	//pause the player
	public void pause(){
		mpS.pause();
	}

	//play Mp from beginning
	public void play(){
		mpS.seekTo(0);
		mpS.start();
	}

	//creates a new player
	public void create(Context con){
		if (mpS != null)
			mpS.release();
		mpS = MediaPlayer.create(con, R.raw.siren);
		mpS.setLooping(true);
	}

	//release the player
	public void kill(){
		if (mpS != null){
			mpS.release();
			mpS = null;
		}		
		
	}
}
