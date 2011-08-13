package com.drewschrauf.robotronic;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.os.Environment;

public class CacheCleaner {

	/**
	 * The number of files to keep in the cache directory
	 */
	public static final int KEEP_FILES_COUNT = 50;

	/**
	 * Not yet implemented
	 * Cleans the DB to remove old entries
	 */
	public static void cleanDB() {
	}

	/**
	 * Delete the oldest files from the cache directory
	 * 
	 * @param context
	 *            The activity that created the ThreadHandler that spawned this
	 *            CacheCleaner
	 */
	public static void cleanFilesystem(Context context) {
		// make the folder for the cache
		String cacheDirString = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		cacheDirString += "/android/data/" + context.getPackageName()
				+ "/cache/";
		File cacheDir = new File(cacheDirString);

		if (cacheDir.exists()) {
			File[] files = cacheDir.listFiles();

			// sort the files from oldest to newest
			Arrays.sort(files, new Comparator<File>() {
				public int compare(File o1, File o2) {

					if (((File) o1).lastModified() > ((File) o2).lastModified()) {
						return +1;
					} else if (((File) o1).lastModified() < ((File) o2)
							.lastModified()) {
						return -1;
					} else {
						return 0;
					}
				}
			});

			int filesToDelete = files.length > KEEP_FILES_COUNT ? files.length
					- KEEP_FILES_COUNT : 0;
			for (int i = 0; i < filesToDelete; i++) {
				File file = files[i];
				file.delete();
			}
		}

	}
}
