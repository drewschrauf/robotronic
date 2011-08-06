package com.drewschrauf.robotronic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHandler {
	
	DatabaseHelper helper;
	
	public DatabaseHandler(Context context) {
		helper = new DatabaseHelper(context);
	}
	
	public void addData(String url, String data) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMN_URL, url);
		values.put(DatabaseHelper.COLUMN_DATA, data);
		values.put(DatabaseHelper.COLUMN_FETCHED_DATE, System.currentTimeMillis());
		db.insert(DatabaseHelper.TABLE_NAME, null, values);
	}
	
	public String getData(String url) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] columns = {DatabaseHelper.COLUMN_DATA};
		Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, columns, DatabaseHelper.COLUMN_URL + " = " + url, null, null,
		        null, "1");
		
		String data = null;
		while (cursor.moveToNext()) {
			data = cursor.getString(0);
		}
		cursor.close();
		return data;
	}

}
