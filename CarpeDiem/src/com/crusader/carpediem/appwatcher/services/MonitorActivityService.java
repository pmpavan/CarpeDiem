/**
 * 
 */
package com.crusader.carpediem.appwatcher.services;

/**
 * 
 */

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.crusader.carpediem.appwatcher.R;
import com.crusader.carpediem.appwatcher.activities.MonitorActivity;
import com.crusader.carpediem.appwatcher.constants.AppConstants;
import com.crusader.carpediem.appwatcher.logger.AppLogger;
import com.crusader.carpediem.appwatcher.utils.AppUtils;

/**
 * @author pavan
 * 
 */
public class MonitorActivityService extends Service {

	private BroadcastReceiver mScreenStateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			boolean escapeFlag = AppUtils.getPrefs(context).getBoolean(
					AppConstants.ESCAPE_FLAG, false);
			if (!escapeFlag) {
				if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

					long time = calculateCurrentScreenAtiveTime(context);
					AppLogger.msg("Screen went OFF "
							+ AppUtils.calculateTime(time)[1]);
					// Toast.makeText(context, "screen OFF + " + time,
					// Toast.LENGTH_SHORT).show();

				} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
					AppLogger.msg("Screen went ON");
					// Store the current time as start time
					Date dt = new Date();
					AppUtils.storeInPreferences(context,
							AppConstants.SCREEN_ON_START_TIME, dt.getTime());
					// Toast.makeText(context, "screen ON",
					// Toast.LENGTH_SHORT).show();
				} else if (intent.getAction()
						.equals(Intent.ACTION_USER_PRESENT)) {
					AppLogger.msg("Screen unlocked");
					// Toast.makeText(context, "screen unlocked",
					// Toast.LENGTH_SHORT)
					// .show();
					incrementScreenONCount(context);
				}
			}

		}

		private long calculateCurrentScreenAtiveTime(Context context) {
			long startTime = 0;
			long currentTime = 0;
			long timeSpentInThisSession = 0;
			if (AppUtils.getPrefs(context).contains(
					AppConstants.SCREEN_ON_START_TIME)) {
				// String startTimeString =
				// AppUtils.getPrefs(context).getString(
				// AppConstants.SCREEN_ON_START_TIME, "");
				// if (!startTimeString.isEmpty()) {
				// startTime = Long.parseLong(startTimeString);
				// startTime = AppUtils.getPrefs(context).getLong(
				// AppConstants.SCREEN_ON_START_TIME, 0);
				// }
				startTime = AppUtils.getPrefs(context).getLong(
						AppConstants.SCREEN_ON_START_TIME, 0);
				Date dt = new Date();
				currentTime = dt.getTime();
				timeSpentInThisSession = currentTime - startTime;
				AppLogger.msg("time spent in session "
						+ AppUtils.calculateTime(timeSpentInThisSession)[1]);
			}
			if (timeSpentInThisSession > 0) {
				return incrementScreenActiveCount(context,
						timeSpentInThisSession);
			}
			return 0;
		}

		private long incrementScreenActiveCount(Context context,
				long timeSpentInThisSession) {
			long activeTimeinPref = AppUtils.getPrefs(context).getLong(
					AppConstants.SCREEN_ACTIVE_TIME, 0);
			long totalActiveTime = activeTimeinPref + timeSpentInThisSession;
			AppUtils.storeInPreferences(context,
					AppConstants.SCREEN_ACTIVE_TIME, totalActiveTime);
			return totalActiveTime;
		}

		private void incrementScreenONCount(Context context) {
			long activeTimeinPref = AppUtils.getPrefs(context).getLong(
					AppConstants.SCREEN_ON_COUNT, 0);
			long totalActiveTime = activeTimeinPref + 1;
			AppUtils.storeInPreferences(context, AppConstants.SCREEN_ON_COUNT,
					totalActiveTime);
		}
	};
	public static boolean mRunning = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startBroadcastListener();
		initiateThread();
		initiateScheduler();
		return super.onStartCommand(intent, flags, startId);
	}

	private void startBroadcastListener() {
		IntentFilter screenStateFilter = new IntentFilter();
		screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
		screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
		screenStateFilter.addAction(Intent.ACTION_USER_PRESENT);
		registerReceiver(mScreenStateReceiver, screenStateFilter);
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = preferences.edit();
		editor.putString(AppConstants.PREVIOUS_DAY_DATA,
				"10:42,20:34,10:23,30:34,40:43");
		editor.commit();
		if (!mRunning) {
			mRunning = true;
		}
	}

	private void stopBroadcastReceiver() {
		mRunning = false;
		unregisterReceiver(mScreenStateReceiver);
	}

	@Override
	public void onDestroy() {

		stopBroadcastReceiver();
		super.onDestroy();
	}

	private void initiateScheduler() {
		Timer uploadCheckerTimer = new Timer(true);
		Date dt = new Date();
		Calendar date = Calendar.getInstance();
		date.setTime(dt);
		date.add(Calendar.DAY_OF_WEEK, 1);
		date.set(Calendar.HOUR, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		// Schedule to run every Sunday in midnight
		uploadCheckerTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				getLast5DaysData();
			}
			// }, 0, 1000 * 3600);
		}, date.getTime(), 1000 * 60 * 60 * 24 * 7);
	}

	private void initiateThread() {
		Timer uploadCheckerTimer = new Timer(true);
		uploadCheckerTimer.scheduleAtFixedRate(new SimpleTask(this), 0,
				15 * 1000);
	}

	public void sendNotification() {
		getRunningAppsInfo();
		// long dailyUserValue =
		// AppUtils.getPrefs(this).getLong("DAILY_THRSHOLD_VALUE", 60000);
		long dailyUserValue = 10;
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		long currentValue = preferences.getLong(
				AppConstants.SCREEN_ACTIVE_TIME, 0);
		Boolean alertFlag = AppUtils.getPrefs(this).getBoolean(
				AppConstants.ALERT_FLAG, false);
		if (currentValue > dailyUserValue && !alertFlag) {
			AppUtils.storeInPreferences(this, AppConstants.ALERT_FLAG, true);
			invokeNotification();
		} else {
			AppLogger.msg("Condition fails");
		}
	}

	private void getLast5DaysData() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		String previousValue = preferences.getString(
				AppConstants.PREVIOUS_DAY_DATA, "0:0,0:0,0:0,0:0,0:0");
		long currentValue = AppUtils.getPrefs(this).getLong(
				AppConstants.SCREEN_ACTIVE_TIME, 0);
		currentValue = currentValue / (60 * 1000);
		long activeTimeinPref = preferences.getLong(
				AppConstants.SCREEN_ON_COUNT, 0);
		String[] previousArray = previousValue.split(",");
		previousArray[4] = previousArray[3];
		previousArray[3] = previousArray[2];
		previousArray[2] = previousArray[1];
		previousArray[1] = previousArray[0];
		previousArray[0] = currentValue + ":" + activeTimeinPref;
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = preferences.edit();
		String preData = "";
		for (int i = 0; i < 5; i++) {
			if (i == 0) {
				preData = previousArray[i];
				continue;
			}
			preData = preData + "," + previousArray[i];
		}
		editor.putString(AppConstants.PREVIOUS_DAY_DATA, preData);
		editor.commit();
	}

	private void getRunningAppsInfo() {
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final List<RunningAppProcessInfo> tasks = activityManager
				.getRunningAppProcesses();
		try {
			if (tasks.size() > 0) {
				PackageManager pm = getPackageManager();
				String foregroundTaskPackageName = tasks.get(0).pkgList[0];
				PackageInfo foregroundAppPackageInfo = pm.getPackageInfo(
						foregroundTaskPackageName, 0);
				AppLogger.msg("Package Name ="
						+ foregroundAppPackageInfo.applicationInfo
								.loadLabel(pm).toString()
						+ " foregroundTaskPackageName "
						+ foregroundTaskPackageName);
			}
		} catch (Exception e) {
		}
	}

	@SuppressLint("NewApi")
	private void invokeNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		PendingIntent i = PendingIntent.getActivity(this, 0, new Intent(this,
				MonitorActivity.class), 0);

		Notification n = new Notification.Builder(this)
				.setContentTitle("Tuk Tuk Notification")
				.setContentText(
						"Hello - Usage limit for the day reached. Guess you won’t mind throwing me away now.")
				.setSmallIcon(R.drawable.monk_dissatisfied).setContentIntent(i)
				.addAction(0, "See why", i).build();

		notificationManager.notify(1, n);

	}

}

class SimpleTask extends TimerTask {

	MonitorActivityService mservice;

	SimpleTask(MonitorActivityService mservice) {
		this.mservice = mservice;
	}

	@Override
	public void run() {
		AppLogger.msg("InsideRun method");
		mservice.sendNotification();

	}

}