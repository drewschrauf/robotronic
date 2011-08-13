package com.drewschrauf.robotronic.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Handler;
import android.os.Message;

import com.drewschrauf.robotronic.database.DatabaseHandler;
import com.drewschrauf.robotronic.threads.ThreadHandler.CacheMode;

public class DataFetchThread extends RobotronicThread {

	private Handler msgHandler;
	private DatabaseHandler dbHandler;
	private boolean useCache;
	private boolean useFresh;

	public DataFetchThread(String url, Handler msgHandler,
			DatabaseHandler dbHandler, CacheMode mode, Handler doneHandler) {
		this.url = url;
		this.msgHandler = msgHandler;
		this.dbHandler = dbHandler;
		this.doneHandler = doneHandler;

		useCache = mode.equals(CacheMode.CACHE_AND_FRESH)
				|| mode.equals(CacheMode.CACHE_ONLY);
		useFresh = mode.equals(CacheMode.CACHE_AND_FRESH)
				|| mode.equals(CacheMode.FRESH_ONLY);
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
			done();
			return;
		}

		// load items from database if available
		if (useCache) {
			String data = dbHandler.getData(url);
			if (data != null) {
				Message msg = Message.obtain();
				msg.what = ThreadHandler.DATA_CACHE;
				msg.obj = data;
				msgHandler.sendMessage(msg);
			}
		}

		// load items from URL
		String result;
		try {
			if (useFresh) {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						fetchUrl.openStream()));
				StringBuilder resultBuilder = new StringBuilder();
				String inputLine = null;
				while ((inputLine = in.readLine()) != null) {
					resultBuilder.append(inputLine);
				}
				in.close();
				result = resultBuilder.toString();

				if (useCache) {
					dbHandler.addData(url, result);
				}

				Message msg = Message.obtain();
				msg.what = ThreadHandler.DATA_FRESH;
				msg.obj = result;
				msgHandler.sendMessage(msg);
			}
		} catch (IOException ioe) {
			Message msg = Message.obtain();
			msg.what = ThreadHandler.ERROR_IO;
			msg.obj = ioe;
			msgHandler.sendMessage(msg);
			done();
			return;
		}
		done();
	}
}