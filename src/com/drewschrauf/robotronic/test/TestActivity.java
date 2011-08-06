package com.drewschrauf.robotronic.test;

import java.util.List;

import android.util.Log;

import com.drewschrauf.robotronic.RobotronicActivity;

public class TestActivity extends RobotronicActivity<String> {

	@Override
	protected List<String> parseData(String data) {
		if (data != null) {
			Log.i("test", data.substring(0, 100));
		}
		return null;
	}

	@Override
	protected void handleException(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getURL() {
		return "http://api.flickr.com/services/feeds/photos_public.gne?format=json";
	}

}
