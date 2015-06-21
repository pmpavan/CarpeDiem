package com.crusader.carpediem.appwatcher.utils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppUtils {

	public AppUtils() {
	}

	/**
	 * Returns the SharedPreferences using the package name as shared preference
	 * name
	 * 
	 * @param ctx
	 * @return
	 */
	public static SharedPreferences getPrefs(Context ctx) {
		return ctx.getSharedPreferences(ctx.getPackageName(),
				Context.MODE_PRIVATE);
	}

	public static void clearPreferenceData(Context context)
	{
		Editor editor = AppUtils.getPrefs(context).edit();
		editor.clear();
		editor.commit();
	}
	
	public static void storeInPreferences(Context context, String key,
			String value) {
		Editor editor = AppUtils.getPrefs(context).edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static void storeInPreferences(Context context, String key,
			float value) {
		Editor editor = AppUtils.getPrefs(context).edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public static void removeFromPreferences(Context context, String key) {
		Editor editor = AppUtils.getPrefs(context).edit();
		editor.remove(key);
		editor.commit();
	}

	public static void storeInPreferences(Context context, String key,
			boolean value) {
		Editor editor = AppUtils.getPrefs(context).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void storeInPreferences(Context context, String key, int value) {
		Editor editor = AppUtils.getPrefs(context).edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void storeInPreferences(Context context, String key,
			long value) {
		Editor editor = AppUtils.getPrefs(context).edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static String toCamelcase(String text) {
		StringBuffer captializeText = new StringBuffer();
		String[] strArray = text.split("\\s");

		for (int itr = 0; itr < strArray.length; itr++) {
			text = strArray[itr].trim();
			if (text.length() > 0) {
				text = String.valueOf(text.charAt(0)).toUpperCase(
						Locale.getDefault())
						+ text.subSequence(1, text.length());
				captializeText.append(text);
				captializeText.append(" ");
			}
		}
		return captializeText.toString().trim();
	}

	public static String toTitlecase(String text) {
		StringBuffer captializeText = new StringBuffer();
		text = text.toLowerCase(Locale.ENGLISH);
		String[] strArray = text.split(" ");

		for (int itr = 0; itr < strArray.length; itr++) {
			text = strArray[itr].trim();
			if (text.length() > 0) {
				text = String.valueOf(text.charAt(0)).toUpperCase(
						Locale.getDefault())
						+ text.subSequence(1, text.length());
				captializeText.append(text);
				captializeText.append(" ");
			}
		}
		return captializeText.toString().trim();
	}

	public static int[] calculateTime(long seconds) {
		seconds = seconds/1000;
		int day = (int) TimeUnit.SECONDS.toDays(seconds);
		long hours = TimeUnit.SECONDS.toHours(seconds)
				- TimeUnit.DAYS.toHours(day);
		long minute = TimeUnit.SECONDS.toMinutes(seconds)
				- TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(seconds));
		long second = TimeUnit.SECONDS.toSeconds(seconds)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS
						.toMinutes(seconds));
		int[] arr = new int[2];
		arr[0] = (int) hours;
		arr[1] = (int) minute;
		return arr;
	}
}
