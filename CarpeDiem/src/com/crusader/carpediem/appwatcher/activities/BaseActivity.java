/**
 * 
 */
package com.crusader.carpediem.appwatcher.activities;

import java.util.Date;
import java.util.Stack;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.crusader.carpediem.appwatcher.constants.AppConstants;
import com.crusader.carpediem.appwatcher.logger.AppLogger;
import com.crusader.carpediem.appwatcher.utils.AppUtils;

/**
 * @author pavan
 * 
 */
public class BaseActivity extends FragmentActivity {

	public void navigateToScreen(Class className) {
		Intent intent = new Intent(this, className);
		startActivityForResult(intent, AppConstants.REQUEST_CODE);
	}

	public String getStringValue(int stringId) {
		return getResources().getString(stringId);
	}

	public void addFragmentToContainer(int containerId, Fragment fragment,
			boolean addToStack, int enter_anim, int exit_anim, int enter_anim2,
			int exit_anim2) {
		addFragment(containerId, fragment, addToStack, !addToStack, enter_anim,
				exit_anim, enter_anim2, exit_anim2, false);
	}

	public void addFragmentToContainer(int containerId, Fragment fragment,
			boolean addToStack, int enter_anim, int exit_anim, int enter_anim2,
			int exit_anim2, boolean shouldAllowStateLoss) {
		addFragment(containerId, fragment, addToStack, !addToStack, enter_anim,
				exit_anim, enter_anim2, exit_anim2, shouldAllowStateLoss);
	}

	public void addFragmentToContainer(int containerViewId, Fragment fragment,
			boolean addStack) {
		addFragment(containerViewId, fragment, addStack, !addStack, 0, 0, 0, 0,
				false);
	}

	public Stack<Fragment> mFragmentStack = new Stack<Fragment>();
	public int mFragmentStackCount = 0;

	private void addFragment(int containerViewId, Fragment fragment,
			boolean addStack, boolean isReplace, int enter_anim, int exit_anim,
			int enter_anim2, int exit_anim2, boolean shouldAllowStateLoss) {

		if (fragment == null) {
			AppLogger.error("Fragment is null.");
			return;
		}

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.setCustomAnimations(enter_anim, exit_anim,
				enter_anim2, exit_anim2);

		String tag = fragment.toString();

		AppLogger.msg("addFragment ==> fragment.isAdded() ==> "
				+ fragment.isAdded());

		if (fragment.isAdded()) {
			fragmentTransaction.show(fragment);

		} else {
			if (!isReplace) {
				fragmentTransaction.add(containerViewId, fragment, tag);

			} else {
				fragmentTransaction.replace(containerViewId, fragment, tag);
			}
		}

		if (addStack) {
			mFragmentStackCount++;
			mFragmentStack.add(fragment);
			fragmentTransaction.addToBackStack(tag);

		} else {
			fragmentManager.popBackStack(null,
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
		if (shouldAllowStateLoss) {
			try {
				fragmentTransaction.commitAllowingStateLoss();
			} catch (Exception e) {
			}

		} else {
			try {
				fragmentTransaction.commit();
			} catch (Exception e) {
			}
		}
		fragmentManager.executePendingTransactions();
	}

	public void popFragment() {
		mFragmentStackCount--;
		if (mFragmentStack.size() > 0)
			mFragmentStack.pop();
		getSupportFragmentManager().popBackStackImmediate();
	}

	public Fragment getTopFragmentFromStack() {
		Fragment fragment = null;
		if (mFragmentStack.size() > 0) {
			fragment = mFragmentStack.peek();
		}

		AppLogger.msg("top fragment ==> " + fragment);
		return fragment;
	}

	private Boolean doubleBackToExitPressedOnce = false;

	public void onBackKeyPressed() {
		if (doubleBackToExitPressedOnce) {
			exitApp();
			return;
		}

		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Please click BACK again to exit",
				Toast.LENGTH_SHORT).show();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
	}

	private long calculateCurrentScreenAtiveTime(Context context) {
		long startTime = 0;
		long currentTime = 0;
		long timeSpentInThisSession = 0;
		if (AppUtils.getPrefs(context).contains(
				AppConstants.SCREEN_ON_START_TIME)) {
			startTime = AppUtils.getPrefs(context).getLong(
					AppConstants.SCREEN_ON_START_TIME, 0);
			Date dt = new Date();
			currentTime = dt.getTime();
			timeSpentInThisSession = currentTime - startTime;
			AppLogger.msg("time spent in session "
					+ AppUtils.calculateTime(timeSpentInThisSession)[1]);
		}
		if (timeSpentInThisSession > 0) {
			return incrementScreenActiveCount(context, timeSpentInThisSession);
		}
		return 0;
	}

	private long incrementScreenActiveCount(Context context,
			long timeSpentInThisSession) {
		long activeTimeinPref = AppUtils.getPrefs(context).getLong(
				AppConstants.SCREEN_ACTIVE_TIME, 0);
		long totalActiveTime = activeTimeinPref + timeSpentInThisSession;
		AppUtils.storeInPreferences(context, AppConstants.SCREEN_ACTIVE_TIME,
				totalActiveTime);
		return totalActiveTime;
	}

	private void exitApp() {
		calculateCurrentScreenAtiveTime(this);
		Date dt = new Date();
		long currentTime = dt.getTime();
		AppUtils.storeInPreferences(this, AppConstants.SCREEN_ON_START_TIME,
				currentTime);
		AppLogger.msg("exitApp");
		finish();
	}

}
