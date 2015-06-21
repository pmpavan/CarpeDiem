package com.crusader.carpediem.appwatcher;


import android.app.Application;

import com.crusader.carpediem.appwatcher.logger.AppLogger;

public class CrusaderApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		AppLogger.setSwitch(true);
	}

}
