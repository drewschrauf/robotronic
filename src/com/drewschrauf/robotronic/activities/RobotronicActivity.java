package com.drewschrauf.robotronic.activities;

import com.drewschrauf.robotronic.threads.ThreadHandler;

import android.app.Activity;
import android.os.Bundle;

public class RobotronicActivity extends Activity {
	private ThreadHandler threadHandler;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		threadHandler = new ThreadHandler(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		threadHandler.killAll();
	};

	/**
	 * Retrieve the ThreadHandler for the current Activity
	 * 
	 * @return The ThreadHandler for the current Activity
	 */
	public ThreadHandler getThreadHandler() {
		return threadHandler;
	}
}
