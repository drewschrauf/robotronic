package com.drewschrauf.robotronic.threads;

import android.content.Context;

import com.drewschrauf.robotronic.database.DatabaseHandler;

public class RobotronicUtilities {

	/**
	 * Delete all data cached in DB
	 */
	public static void deleteAllDBEntries(final Context context) {
		DatabaseHandler dbHandler = new DatabaseHandler(context);
		dbHandler.deleteAllData();
	}
}
