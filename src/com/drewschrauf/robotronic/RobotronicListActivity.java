package com.drewschrauf.robotronic;

import java.util.ArrayList;
import java.util.List;

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

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

	public List<A> getItems() {
		return items;
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
	 * 
	 * @param e
	 */
	protected abstract void handleException(Exception e);

	protected abstract BaseAdapter getAdapter();

	/**
	 * 
	 * @return
	 */
	protected abstract String getURL();

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
}