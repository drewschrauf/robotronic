package com.drewschrauf.robotronic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Handler;
import android.os.Message;

public class DataFetchThread extends Thread {

	private String url;
	private Handler msgHandler;
	private DatabaseHandler dbHandler;

	public DataFetchThread(String url, Handler msgHandler,
			DatabaseHandler dbHandler) {
		this.url = url;
		this.msgHandler = msgHandler;
		this.dbHandler = dbHandler;
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
			msg.what = ThreadHandler.ERROR_URL;
			msg.obj = e;
			msgHandler.sendMessage(msg);
			return;
		}

		// load items from database if available
		if (dbHandler != null) {
			String data = dbHandler.getData(url);
			Message msg = Message.obtain();
			msg.what = ThreadHandler.DATA_CACHE;
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
			msg.what = ThreadHandler.ERROR_IO;
			msg.obj = ioe;
			msgHandler.sendMessage(msg);
			return;
		}

		if (dbHandler != null) {
			dbHandler.addData(url, result);
		}

		{
			Message msg = Message.obtain();
			msg.what = ThreadHandler.DATA_FRESH;
			msg.obj = result;
			msgHandler.sendMessage(msg);
		}
	}
}