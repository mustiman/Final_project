package com.example.final_project;


import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;

// singleton implementation of dogs siren player
public class CamMan  {

	private final static CamMan instance = new CamMan();
	private Camera mycam;

	//constructor
	private CamMan(){
		mycam=Camera.open();
	}
	

//-----------------------methods-------------------------//
	
	//get instance
	public static CamMan getInstance(){
		return instance;
	}
	
	
	//creates a new player
	public Parameters getParameters(){
		return mycam.getParameters();
	}
	
	//creates a new player
	public void setParameters(Parameters param){
		mycam.setParameters(param);
	}

	//release the player
	public void kill(){
		if (mycam != null){
			mycam.release();
			mycam = null;
		}			
	}
}
