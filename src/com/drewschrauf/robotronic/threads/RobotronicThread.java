package com.drewschrauf.robotronic.threads;

import android.os.Handler;
import android.os.Message;

public abstract class RobotronicThread extends Thread {
	protected String url;
	protected Handler doneHandler;
	protected RobotronicProperties properties;
	protected boolean isStopping;

	protected void done() {
		Message msg = Message.obtain();
		msg.what = 0;
		msg.obj = url;
		doneHandler.sendMessage(msg);
	}
	
	protected void requestStop() {
		isStopping = true;
	}
}
