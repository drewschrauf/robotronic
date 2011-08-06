package com.drewschrauf.robotronic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;

public abstract class RobotronicActivity<A> extends ListActivity {
	List<A> items;
	DatabaseHandler handler;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		handler = new DatabaseHandler(this);
		new FetchDataTask<A>().execute(getURL());
	}

	private class FetchDataTask<B> extends AsyncTask<String, Integer, String> {
		private Exception thrownError = null;

		/**
		 * Fetches the data from the database if it's available then
		 * from the network
		 */
		protected String doInBackground(String... urls) {
			URL url;
			try {
				url = new URL((String) urls[0]);
			} catch (MalformedURLException e) {
				thrownError = e;
				return null;
			}
			
			// load items from database if available
			items = parseData(handler.getData(urls[0]));
			
			// load items from URL
			String result;
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						url.openStream()));
				StringBuilder resultBuilder = new StringBuilder();
				String inputLine = null;
				while ((inputLine = in.readLine()) != null) {
					resultBuilder.append(inputLine);
				}
				in.close();
				result = resultBuilder.toString();
			} catch (IOException ioe) {
				thrownError = ioe;
				return null;
			}
			
			handler.addData(urls[0], result);
			return result;
		}
		
		/**
		 * Handles the fetched data or any thrown exceptions
		 */
		protected void onPostExecute(String result) {
			// if we had an exception during download, handle it
			if (thrownError != null) {
				handleException(thrownError);
			}
			items = parseData(result);
		}
	}
	
	/**
	 * Parses the data fetched from the database or network
	 * @param data A String containing the returned data
	 * @return A list of items to be displayed on the screen
	 */
	protected abstract List<A> parseData(String data);
	
	/**
	 * 
	 * @param e
	 */
	protected abstract void handleException(Exception e);
	
	/**
	 * 
	 * @return
	 */
	protected abstract String getURL();
}