/**
 * 
 */
package com.crusader.carpediem.appwatcher.logger;

import android.util.Log;

/**
 * @author pavan
 *
 */
public class AppLogger {

	public static boolean loggerSwitch = false;
	private static String LOGGER_TAG = "APP_LOGGER";

	public AppLogger() {
	}

	/**
	 * @param status
	 */
	public static void setSwitch(boolean status) {
		loggerSwitch = status;
	}

	/**
	 * @param msg
	 *            Log as a Debug
	 */
	public static void msg(String msg) {
		if (loggerSwitch) {
			Log.d(LOGGER_TAG, msg);
		}
	}

	/**
	 * @param error
	 *            Log as a Error
	 */
	public static void error(String error) {
		if (loggerSwitch) {
			Log.e(LOGGER_TAG, error);
		}
	}
}
