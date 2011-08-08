package com.drewschrauf.robotronic;

import java.io.InputStream;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class ThreadFactory {

	public static void makeImageDownloader(final ImageView imageView, String imageUrl) {
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what) {
					case DataFetchThread.DATA_CACHE:
						imageView.setImageDrawable(Drawable.createFromStream((InputStream)msg.obj, "src"));
						break;
					case DataFetchThread.DATA_FRESH:
						imageView.setImageDrawable(Drawable.createFromStream((InputStream)msg.obj, "src"));
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
		
		new DataFetchThread(imageUrl, handler, null, true).start();
	}
}
