package com.drewschrauf.robotronic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "robotronic"; // the name of our database
	private static final int DB_VERSION = 1; // the version of the database
	
	public static final String TABLE_NAME = "FETCHED_DATA";
	public static final String COLUMN_URL = "URL";
	public static final String COLUMN_DATA = "URL";
	public static final String COLUMN_FETCHED_DATE = "FETCHED_DATE";

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String newTableQueryString = 	
			"create table " + TABLE_NAME + " (" +
			COLUMN_URL + " text primary key not null," +
			COLUMN_DATA + " text," +
			COLUMN_FETCHED_DATE + " datetime);";
	 
		// execute the query string to the database.
		db.execSQL(newTableQueryString);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {		
	}
	
}
