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

	List<Thread> threads;
	Map<String, Drawable> cachedImages;
	protected DatabaseHandler dbHandler;
	Context context;

	public ThreadHandler(Context context) {
		threads = new ArrayList<Thread>();
		cachedImages = new HashMap<String, Drawable>();
		dbHandler = new DatabaseHandler(context);
		this.context = context;
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
	 * @param imageView The View that should be updated with the retrieved image
	 * @param imageUrl The URL to fetch the image from
	 */
	public void makeImageDownloader(final ImageView imageView,
			final String imageUrl) {
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case DataFetchThread.DATA_CACHE:
				case DataFetchThread.DATA_FRESH:
					Drawable d = Drawable.createFromStream(
							(InputStream) msg.obj, "src");
					imageView.setImageDrawable(d);
					cachedImages.put(imageUrl, d);
					break;
				case DataFetchThread.ERROR_URL:
					// maybe set default image?
					break;
				case DataFetchThread.ERROR_IO:
					// maybe set default image?
					break;
				}
			}
		};

		if (cachedImages.containsKey(imageUrl)) {
			imageView.setImageDrawable(cachedImages.get(imageUrl));
		} else {
			BinaryFetchThread thread = new BinaryFetchThread(imageUrl, handler, context);
			threads.add(thread);
			thread.start();
		}
	}

	/**
	 * Downloads text data from the URL or restores it from the cache
	 * @param msgHandler The handler to use for parsing the retrieved data
	 * @param url The URL to retrieve the data from
	 */
	public void makeDataDownloader(Handler msgHandler,
			String url) {
		DataFetchThread thread = new DataFetchThread(url, msgHandler, dbHandler);
		threads.add(thread);
		thread.start();
	}
}
