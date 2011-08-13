package com.drewschrauf.robotronic;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class ThreadHandler {
	public static final int DATA_CACHE = 1;
	public static final int DATA_FRESH = 2;
	public static final int ERROR_URL = 3;
	public static final int ERROR_IO = 4;

	List<Thread> threads;
	Map<String, Drawable> cachedImages;
	protected DatabaseHandler dbHandler;
	Context context;

	/**
	 * Instantiate the ThreadHandler
	 * @param context The activity instantiating this ThreadHandler
	 */
	public ThreadHandler(final Context context) {
		threads = new ArrayList<Thread>();
		cachedImages = new HashMap<String, Drawable>();
		dbHandler = new DatabaseHandler(context);
		this.context = context;
		
		// clean up the cache
		Thread cacheCleaner = new Thread() {
			@Override
			public void run() {
				CacheCleaner.cleanFilesystem(context);
			}
		};
		cacheCleaner.start();
	}

	public void killAll() {
		for (Thread t : threads) {
			if (t.isAlive()) {
				t.stop();
			}
		}
		threads.clear();
	}

	/**
	 * Downloads the image from the URL or restores it from the cache.
	 * 
	 * @param imageView
	 *            The View that should be updated with the retrieved image
	 * @param imageUrl
	 *            The URL to fetch the image from
	 */
	public void makeImageDownloader(final ImageView imageView,
			final String imageUrl) {
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (isData(msg.what)) {
					Drawable d = Drawable.createFromStream(
							(InputStream) msg.obj, "src");
					imageView.setImageDrawable(d);
					cachedImages.put(imageUrl, d);
				} else {
					// set default image
				}
			}
		};

		if (cachedImages.containsKey(imageUrl)) {
			imageView.setImageDrawable(cachedImages.get(imageUrl));
		} else {
			BinaryFetchThread thread = new BinaryFetchThread(imageUrl, handler,
					context);
			threads.add(thread);
			thread.start();
		}
	}

	/**
	 * Downloads text data from the URL or restores it from the cache
	 * 
	 * @param msgHandler
	 *            The handler to use for parsing the retrieved data
	 * @param url
	 *            The URL to retrieve the data from
	 */
	public void makeDataDownloader(Handler msgHandler, String url) {
		DataFetchThread thread = new DataFetchThread(url, msgHandler, dbHandler);
		threads.add(thread);
		thread.start();
	}

	public static boolean isData(int code) {
		return (code == DATA_CACHE || code == DATA_FRESH);
	}

	public static boolean isError(int code) {
		return (code == ERROR_URL || code == ERROR_IO);
	}
}
