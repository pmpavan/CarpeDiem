package com.crusader.carpediem.appwatcher.activities;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.crusader.carpediem.appwatcher.R;
import com.crusader.carpediem.appwatcher.constants.AppConstants;
import com.crusader.carpediem.appwatcher.fragments.HomeFragment;
import com.crusader.carpediem.appwatcher.fragments.SettingsFragment;
import com.crusader.carpediem.appwatcher.fragments.StatsFragment;
import com.crusader.carpediem.appwatcher.interfaces.HomeListenerInterface;
import com.crusader.carpediem.appwatcher.logger.AppLogger;
import com.crusader.carpediem.appwatcher.services.MonitorActivityService;
import com.crusader.carpediem.appwatcher.utils.AppUtils;

public class MonitorActivity extends BaseActivity implements
		HomeListenerInterface {

	private HomeFragment homeFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitor);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		manageDuplicateActivityInstance();
		initBackStackCount(savedInstanceState);
		setupViews();
		setupControllers(savedInstanceState);
		if (!AppUtils.getPrefs(this).contains(AppConstants.SCREEN_ON_COUNT)) {
			AppUtils.storeInPreferences(this, AppConstants.SCREEN_ON_COUNT, 1L);
		}
		if (!MonitorActivityService.mRunning) {
			AppLogger.msg("service started");
			startMonitorService();
		} else {
			AppLogger.msg("service already running");
		}

	}

	private void startMonitorService() {
		Intent service = new Intent(this, MonitorActivityService.class);

		/*
		 * final Handler handler = new Handler() {
		 * 
		 * @Override public void handleMessage(Message msg) {
		 * invokeNotification(); } };
		 * 
		 * final Messenger messenger = new Messenger(handler);
		 * service.putExtra("messenger", messenger);
		 */
		startService(service);

	}

	private void setupViews() {

	}

	private void setupControllers(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			showHomeFragment();

		} else {
			popAllFragmentsOnActivityStart();
			showHomeFragment();
		}
	}

	private void showHomeFragment() {
		homeFragment = new HomeFragment();
		addFragmentToContainer(R.id.home_screen_container, homeFragment, true,
				R.anim.fade_in, 0, 0, R.anim.fade_out);
	}

	private void manageDuplicateActivityInstance() {
		// To avoid duplicate instance of the activity
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			finish();
			return;
		}
	}

	private void initBackStackCount(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mFragmentStackCount = getSupportFragmentManager()
					.getBackStackEntryCount();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.monitor, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mFragmentStackCount > 1) {
				Fragment fragment = getTopFragmentFromStack();
				if (fragment instanceof HomeFragment) {
					onBackKeyPressed();
				} else {
					// write code for fragment pop here
					popFragment();
				}
			} else {
				onBackKeyPressed();
			}
		} else
			return super.onKeyDown(keyCode, event);

		return true;

	}

	private void popAllFragmentsOnActivityStart() {
		if (mFragmentStackCount > 0) {
			while (mFragmentStackCount > 0) {
				popFragment();
			}
		}
	}

	@SuppressLint("NewApi")
	private void invokeNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		PendingIntent i = PendingIntent.getActivity(this, 0, new Intent(this,
				MonitorActivity.class), 0);

		Notification n = new Notification.Builder(this)
				.setContentTitle("Tuk Tuk Notification")
				.setContentText("Your limit has reached")
				.setSmallIcon(R.drawable.ic_launcher).setContentIntent(i)
				.build();

		notificationManager.notify(1, n);
	}

	@Override
	public void onSettingMenuCLicked() {
		SettingsFragment settingsFragment = new SettingsFragment();
		addFragmentToContainer(R.id.home_screen_container, settingsFragment,
				true, R.anim.fade_in, 0, 0, R.anim.fade_out);
	}

	@Override
	public void onBackClicked() {
		popFragment();
	}

	@Override
	public void onIncognitoEnabled(boolean status) {
		if (!status) {
			homeFragment.startRunnableProcesses();
		} else {
			homeFragment.stopRunnableProcesses();
		}
	}

	@Override
	public void onStatsMenuClicked() {
		StatsFragment statsFragment = new StatsFragment();
		addFragmentToContainer(R.id.home_screen_container, statsFragment, true,
				R.anim.fade_in, 0, 0, R.anim.fade_out);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Clear all notification
		NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nMgr.cancelAll();
	}
}
