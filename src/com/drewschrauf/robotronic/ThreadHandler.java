package com.drewschrauf.robotronic;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class ThreadHandler {

	List<Thread> threads;
	Map<String, Drawable> cachedImages;

	public ThreadHandler() {
		threads = new ArrayList<Thread>();
		cachedImages = new HashMap<String, Drawable>();
	}

	public void killAll() {
		for (Thread t : threads) {
			if (t.isAlive()) {
				t.stop();
			}
		}
		threads.clear();
	}

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
			BinaryFetchThread thread = new BinaryFetchThread(imageUrl, handler);
			threads.add(thread);
			thread.start();
		}
	}

	public void makeDataDownloader(Handler msgHandler,
			DatabaseHandler dbHandler, String url) {
		DataFetchThread thread = new DataFetchThread(url, msgHandler, dbHandler);
		threads.add(thread);
		thread.start();
	}
}
