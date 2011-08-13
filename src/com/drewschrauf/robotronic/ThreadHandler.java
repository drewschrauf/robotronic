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

	public enum CacheMode {
		CACHE_AND_FRESH, CACHE_ONLY, FRESH_ONLY
	};

	List<Thread> threads;
	Map<String, Drawable> cachedImages;
	protected DatabaseHandler dbHandler;
	Context context;

	/**
	 * Instantiate the ThreadHandler
	 * 
	 * @param context
	 *            The activity instantiating this ThreadHandler
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
			final String imageUrl, CacheMode mode) {
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
					context, mode);
			threads.add(thread);
			thread.start();
		}
	}

	/**
	 * Convenience method to make an ImageDownloader with the default cache mode
	 * 
	 * @param imageView
	 *            The View that should be updated with the retrieved image
	 * @param imageUrl
	 *            The URL to fetch the image from
	 */
	public void makeImageDownloader(final ImageView imageView,
			final String imageUrl) {
		makeImageDownloader(imageView, imageUrl, CacheMode.CACHE_AND_FRESH);
	}

	/**
	 * Downloads binary data from the URL or restores it from the cache
	 * 
	 * @param msgHandler
	 *            The handler to use for handling the retrieved data
	 * @param url
	 *            The URL to retrieve the data from
	 */
	public void makeBinaryDownloader(Handler msgHandler, String url,
			CacheMode mode) {
		BinaryFetchThread thread = new BinaryFetchThread(url, msgHandler,
				context, mode);
		threads.add(thread);
		thread.start();
	}

	/**
	 * Convenience method to make a BinaryDownloader with default cache mode
	 * 
	 * @param msgHandler
	 *            The handler to use for handling the retrieved data
	 * @param url
	 *            The URL to retrieve the data from
	 */
	public void makeBinaryDownloader(Handler msgHandler, String url) {
		makeBinaryDownloader(msgHandler, url);
	}

	/**
	 * Downloads text data from the URL or restores it from the cache
	 * 
	 * @param msgHandler
	 *            The handler to use for parsing the retrieved data
	 * @param url
	 *            The URL to retrieve the data from
	 */
	public void makeDataDownloader(Handler msgHandler, String url,
			CacheMode mode) {
		DataFetchThread thread = new DataFetchThread(url, msgHandler,
				dbHandler, mode);
		threads.add(thread);
		thread.start();
	}

	/**
	 * Convenience method to make a DataDownloader with default cache mode
	 * 
	 * @param msgHandler
	 *            The handler to use for the message response
	 * @param url
	 *            The url to fetch the data from
	 */
	public void makeDataDownloader(Handler msgHandler, String url) {
		makeDataDownloader(msgHandler, url, CacheMode.CACHE_AND_FRESH);
	}

	public static boolean isData(int code) {
		return (code == DATA_CACHE || code == DATA_FRESH);
	}

	public static boolean isError(int code) {
		return (code == ERROR_URL || code == ERROR_IO);
	}
}
