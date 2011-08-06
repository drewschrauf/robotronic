package com.drewschrauf.robotronic;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public abstract class RobotronicListActivity<A> extends ListActivity {
	List<A> items;
	DatabaseHandler dbHandler;
	DataFetchHandler msgHandler;
	DataFetchThread fetchThread;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		dbHandler = new DatabaseHandler(this);
		msgHandler = new DataFetchHandler();
		
		fetchThread = new DataFetchThread(getURL(), msgHandler, dbHandler);
		fetchThread.start();
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
	
	private class DataFetchHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case DataFetchThread.DATA_CACHE:
					items = parseData((String)msg.obj);
					break;
				case DataFetchThread.DATA_FRESH:
					items = parseData((String)msg.obj);
					break;
				case DataFetchThread.ERROR_URL:
					handleException((Exception)msg.obj);
					break;
				case DataFetchThread.ERROR_IO:
					handleException((Exception)msg.obj);
					break;
			}
		}
	}
}