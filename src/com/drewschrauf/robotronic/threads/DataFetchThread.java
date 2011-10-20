package com.drewschrauf.robotronic.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.Handler;
import android.os.Message;

import com.drewschrauf.robotronic.database.DatabaseHandler;
import com.drewschrauf.robotronic.threads.ThreadHandler.CacheMode;

public class DataFetchThread extends RobotronicThread {

	private Handler msgHandler;
	private DatabaseHandler dbHandler;
	private boolean useCache;
	private boolean useFresh;

	/**
	 * Creates a new DataFetchThread
	 * 
	 * @param url
	 *            The URL to retrieve data from
	 * @param msgHandler
	 *            The handler to call back to with retrieved binary
	 * @param context
	 *            The context of the application using Robotronic
	 * @param mode
	 *            The cache mode to be used
	 * @param doneHandler
	 *            A generic handler to be called when the thread is done
	 */
	public DataFetchThread(String url, Handler msgHandler,
			DatabaseHandler dbHandler, CacheMode mode, Handler doneHandler,
			RobotronicProperties properties) {
		this.url = url;
		this.msgHandler = msgHandler;
		this.dbHandler = dbHandler;
		this.doneHandler = doneHandler;
		this.properties = properties;

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
		if (isStopping)
			return;

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

		if (isStopping)
			return;

		// load items from URL
		String result;
		try {
			if (useFresh) {
				URLConnection connection = fetchUrl.openConnection();
				if (properties.getDataAcceptType() != null) {
					connection.addRequestProperty("Accept",
							properties.getDataAcceptType());
				}
				BufferedReader in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));

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