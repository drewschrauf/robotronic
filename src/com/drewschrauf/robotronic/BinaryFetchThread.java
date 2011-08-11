package com.drewschrauf.robotronic;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BinaryFetchThread extends Thread {
	public static final int DATA_CACHE = 1;
	public static final int DATA_FRESH = 2;
	public static final int ERROR_IO = 4;

	private String url;
	private Handler msgHandler;

	public BinaryFetchThread(String url, Handler msgHandler) {
		this.url = url;
		this.msgHandler = msgHandler;
	}
	
	/**
	 * Fetches the data from the database if it's available then from the
	 * network
	 */
	public void run() {
		Log.d("test", "Starting: " + url);
		InputStream is = null;
		try {
			is = new DefaultHttpClient().
			execute(new HttpGet(url)).
			getEntity().
			getContent();
		} catch (Exception e) {
			Message msg = Message.obtain();
			msg.what = ERROR_IO;
			msg.obj = e;
			msgHandler.sendMessage(msg);
			return;
		}
		
		{
			Message msg = Message.obtain();
			msg.what = DATA_FRESH;
			msg.obj = is;
			msgHandler.sendMessage(msg);
		}
		Log.d("test", "Finished: " + url);
	}
}
