package com.drewschrauf.robotronic.activities;

import java.util.ArrayList;
import java.util.List;

import com.drewschrauf.robotronic.threads.ParsingException;
import com.drewschrauf.robotronic.threads.ThreadHandler;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

public abstract class RobotronicListActivity<A> extends ListActivity {
	protected List<A> items;
	protected DataFetchHandler msgHandler;
	protected ThreadHandler threadHandler;
	BaseAdapter adapter;
	ListView list;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		items = new ArrayList<A>();
		msgHandler = new DataFetchHandler();
		threadHandler = new ThreadHandler(this);

		adapter = getAdapter();
		setListAdapter(adapter);
		list = (ListView) this.findViewById(android.R.id.list);
	}

	@Override
	protected void onStart() {
		super.onStart();
		threadHandler.makeDataDownloader(msgHandler, getURL());
	}

	@Override
	protected void onStop() {
		super.onStop();
		threadHandler.killAll();
	};

	/**
	 * Returns the items used for populating the list
	 * @return The list of items
	 */
	public List<A> getItems() {
		return items;
	}
	
	/**
	 * A handler for populating the ListView used on this activity
	 * @author Drew
	 *
	 */
	private class DataFetchHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (ThreadHandler.isData(msg.what)) {

				try {
					int originalSize = items.size();
					items.addAll(0, parseData((String) msg.obj));
					
					if (originalSize == 0) {
						list.setAdapter(adapter);
					} else {
						adapter.notifyDataSetChanged();
						int selectedIndex = list.getFirstVisiblePosition();
						View v = list.getChildAt(0);
						int top = (v == null) ? 0 : v.getTop();
						list.setSelectionFromTop(items.size() - originalSize
								+ selectedIndex, top);
					}
				} catch (ParsingException pe) {
					handleException(pe);
				}
			} else {
				handleException((Exception) msg.obj);
			}
		}
	}
	
	/**
	 * Retrieve the ThreadHandler for the current Activity
	 * @return The ThreadHandler for the current Activity
	 */
	public ThreadHandler getThreadHandler() {
		return threadHandler;
	}

	/**
	 * Parses the data fetched from the database or network
	 * 
	 * @param data
	 *            A String containing the returned data
	 * @return The list of items to be displayed on the screen
	 */
	protected abstract List<A> parseData(String data) throws ParsingException;

	/**
	 * Handle any exceptions thrown during retrieval of data
	 * @param e The Exception thrown during retrieval
	 */
	protected abstract void handleException(Exception e);

	/**
	 * Get the adapter to be used for the ListView
	 * @return An instance of BaseAdapter
	 */
	protected abstract BaseAdapter getAdapter();

	/**
	 * Get the URL to be used for retrieving obects to use in the ListView
	 * @return A string containing the URL to retrieve the data from
	 */
	protected abstract String getURL();

	
}