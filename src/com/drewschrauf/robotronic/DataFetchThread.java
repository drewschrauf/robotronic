package com.drewschrauf.robotronic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Handler;
import android.os.Message;

public class DataFetchThread extends Thread {
	public static final int DATA_CACHE = 1;
	public static final int DATA_FRESH = 2;
	public static final int ERROR_URL = 3;
	public static final int ERROR_IO = 4;

	private String url;
	private Handler msgHandler;
	private DatabaseHandler dbHandler;
	private boolean binary;

	public DataFetchThread(String url, Handler msgHandler,
			DatabaseHandler dbHandler, boolean binary) {
		this.url = url;
		this.msgHandler = msgHandler;
		this.dbHandler = dbHandler;
		this.binary = binary;
	}

	/**
	 * Fetches the data from the database if it's available then from the
	 * network
	 */
	public void run() {
		URL fetchUrl;
		try {
			fetchUrl = new URL(url);
		} catch (final MalformedURLException e) {
			Message msg = Message.obtain();
			msg.what = ERROR_URL;
			msg.obj = e;
			msgHandler.sendMessage(msg);
			return;
		}
		
		if (!binary) {

			// load items from database if available
			if (dbHandler == null) {
				String data = dbHandler.getData(url);
				Message msg = Message.obtain();
				msg.what = DATA_CACHE;
				msg.obj = data;
				msgHandler.sendMessage(msg);
			}
	
			// load items from URL
			String result;
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						fetchUrl.openStream()));
				StringBuilder resultBuilder = new StringBuilder();
				String inputLine = null;
				while ((inputLine = in.readLine()) != null) {
					resultBuilder.append(inputLine);
				}
				in.close();
				result = resultBuilder.toString();
			} catch (IOException ioe) {
				Message msg = Message.obtain();
				msg.what = ERROR_IO;
				msg.obj = ioe;
				msgHandler.sendMessage(msg);
				return;
			}
	
			if (dbHandler == null) {
				dbHandler.addData(url, result);
			}

			{
				Message msg = Message.obtain();
				msg.what = DATA_FRESH;
				msg.obj = result;
				msgHandler.sendMessage(msg);
			}
		} else {
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
		}
	}
}