package com.drewschrauf.robotronic.test;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.drewschrauf.robotronic.ParsingException;
import com.drewschrauf.robotronic.R;
import com.drewschrauf.robotronic.RobotronicListActivity;

public class TestActivity extends RobotronicListActivity<TestItem> {

	@Override
	protected List<TestItem> parseData(String data) throws ParsingException {
		List<TestItem> result = new ArrayList<TestItem>();
		data = data.substring(15);
		try {
			JSONObject feed = new JSONObject(data);
			JSONArray items = feed.getJSONArray("items");
			for (int x = 0; x < Math.min(items.length(), 20); x++) {
				JSONObject item = items.getJSONObject(x);
				result.add(new TestItem(item.getString("title"), item
						.getJSONObject("media").getString("m")));
			}
		} catch (JSONException e) {
			throw new ParsingException(e);
		}
		return result;
	}

	@Override
	protected void handleException(Exception e) {
		Log.e("test", e.getMessage());
	}

	@Override
	protected String getURL() {
		return "http://api.flickr.com/services/feeds/photos_public.gne?format=json";
	}

	@Override
	public ListAdapter getListAdapter() {
		final Context context = this;
		ListAdapter adapter = new ListAdapter() {

			public int getCount() {
				return getItems().size();
			}

			public Object getItem(int p) {
				return getItems().get(p);
			}

			public long getItemId(int p) {
				return p;
			}

			public int getItemViewType(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				View row = View.inflate(context, R.layout.testrow, null);
				((TextView)row.findViewById(R.id.title)).setText(getItems().get(position).getTitle());
				threadHandler.makeImageDownloader((ImageView)row.findViewById(R.id.image), 
						getItems().get(position).getImageUrl());
				return row;
			}

			public int getViewTypeCount() {
				return 1;
			}

			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean isEmpty() {
				return getItems().size() == 0;
			}

			public void registerDataSetObserver(DataSetObserver arg0) {
				// TODO Auto-generated method stub

			}

			public void unregisterDataSetObserver(DataSetObserver arg0) {
				// TODO Auto-generated method stub

			}

			public boolean areAllItemsEnabled() {
				return getItems().size() > 0;
			}

			public boolean isEnabled(int arg0) {
				return getItems().size() > 0;
			}

		};

		return adapter;
	}
}
