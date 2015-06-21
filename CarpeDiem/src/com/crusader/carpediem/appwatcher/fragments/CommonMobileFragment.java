/**
 * 
 */
package com.crusader.carpediem.appwatcher.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;

import com.crusader.carpediem.appwatcher.activities.MonitorActivity;

/**
 * @author pavan
 *
 */
public class CommonMobileFragment extends Fragment {

	public Context mContext;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();

		try {
			java.lang.reflect.Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	public int getScreenHeight() {
		if (mContext != null) {
			DisplayMetrics displaymetrics = new DisplayMetrics();
			((MonitorActivity) mContext).getWindowManager().getDefaultDisplay()
					.getMetrics(displaymetrics);
			return displaymetrics.heightPixels;
		} else {
			return -1;
		}
	}

	public int getScreenWidth() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((MonitorActivity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		return displaymetrics.widthPixels;
	}

	public static int dpToPx(Context context, int dp) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		int px = Math.round(dp
				* (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return px;
	}

	public static int pxToDp(Context context, int px) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		int dp = Math.round(px
				* (DisplayMetrics.DENSITY_DEFAULT / displayMetrics.xdpi));
		return dp;
	}

	public String getStringValue(int stringId) {

		if (isAdded()) {
			return getResources().getString(stringId);
		} else {
			return "";
		}
	}
	
	public Activity getFragmentContext() {
		return (MonitorActivity) mContext;
	}

	public static Typeface getCustomTypeface(Context context, String assetPath) {
		return Typeface.createFromAsset(context.getAssets(), assetPath);
	}
}
