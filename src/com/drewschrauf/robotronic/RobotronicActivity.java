package com.drewschrauf.robotronic;

import android.app.Activity;
import android.os.Bundle;

public class RobotronicActivity extends Activity {
	protected ThreadHandler threadHandler;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		threadHandler = new ThreadHandler(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		threadHandler.killAll();
	};
}
