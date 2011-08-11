package com.drewschrauf.robotronic;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public abstract class RobotronicListActivity<A> extends ListActivity {
	protected List<A> items;
	protected DatabaseHandler dbHandler;
	protected DataFetchHandler msgHandler;
	protected ThreadHandler threadHandler;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		dbHandler = new DatabaseHandler(this);
		msgHandler = new DataFetchHandler();
		threadHandler = new ThreadHandler();		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		threadHandler.makeDataDownloader(msgHandler, dbHandler, getURL());
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		threadHandler.killAll();
	};
	
	public List<A> getItems() {
		return items;
	}
	
	/**
	 * Parses the data fetched from the database or network
	 * @param data A String containing the returned data
	 * @return The list of items to be displayed on the screen
	 */
	protected abstract List<A> parseData(String data) throws ParsingException;
	
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
					try {
						items = parseData((String)msg.obj);
						setListAdapter(getListAdapter());
					} catch (ParsingException pe) {
						handleException(pe);
					}
					break;
				case DataFetchThread.DATA_FRESH:
					try {
						items = parseData((String)msg.obj);
						setListAdapter(getListAdapter());
					} catch (ParsingException pe) {
						handleException(pe);
					}
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