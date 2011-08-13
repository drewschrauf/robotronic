package com.drewschrauf.robotronic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class BinaryFetchThread extends Thread {

	File cachePath;

	private String url;
	private Handler msgHandler;

	public BinaryFetchThread(String url, Handler msgHandler, Context context) {
		this.url = url;
		this.msgHandler = msgHandler;

		// make the folder for the cache
		String cacheDirString = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		cacheDirString += "/android/data/" + context.getPackageName()
				+ "/cache/";
		new File(cacheDirString).mkdirs();

		// make the cache file
		cacheDirString += url.hashCode();
		cachePath = new File(cacheDirString);
	}

	/**
	 * Fetches the data from the database if it's available then from the
	 * network
	 */
	public void run() {
		InputStream is = null;
		
		if (cachePath.exists()) {
			try {
				is = new FileInputStream(cachePath);
				Message msg = Message.obtain();
				msg.what = ThreadHandler.DATA_CACHE;
				msg.obj = is;
				msgHandler.sendMessage(msg);
				return;
			} catch (FileNotFoundException e) {
				// shouldn't happen
			}
		}
		
		try {
			is = new DefaultHttpClient().
			execute(new HttpGet(url)).
			getEntity().
			getContent();
			
			// write item to filesystem
			FileOutputStream fos = new FileOutputStream(cachePath);
			byte[] buf = new byte[1024];
			int numRead;
	    	while ( (numRead = is.read(buf) ) >= 0) {
	    		fos.write(buf, 0, numRead);
	    	}
	    	is.close();
	    	fos.close();
	    	is = new FileInputStream(cachePath);
		} catch (Exception e) {
			Message msg = Message.obtain();
			msg.what = ThreadHandler.ERROR_IO;
			msg.obj = e;
			msgHandler.sendMessage(msg);
			return;
		}
		
		{
			Message msg = Message.obtain();
			msg.what = ThreadHandler.DATA_FRESH;
			msg.obj = is;
			msgHandler.sendMessage(msg);
		}
	}
}
