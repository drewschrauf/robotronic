package com.drewschrauf.robotronic.test;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.drewschrauf.robotronic.ParsingException;
import com.drewschrauf.robotronic.RobotronicListActivity;

public class TestActivity extends RobotronicListActivity<String> {

	@Override
	protected List<String> parseData(String data) throws ParsingException {
		List<String> result = new ArrayList<String>();
		data = data.substring(15);
		try {
			JSONObject feed = new JSONObject(data);
			JSONArray items = feed.getJSONArray("items");
			for (int x = 0; x < items.length(); x++) {
				JSONObject item = items.getJSONObject(x);
				result.add(item.getString("title"));
			}
		} catch (JSONException e) {
			throw new ParsingException(e);
		}
		return result;
	}

	@Override
	protected void handleException(Exception e) {
		
	}

	@Override
	protected String getURL() {
		return "http://api.flickr.com/services/feeds/photos_public.gne?format=json";
	}

	@Override
	public ListAdapter getListAdapter() {
		ListAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);
		return adapter;
	}

}
