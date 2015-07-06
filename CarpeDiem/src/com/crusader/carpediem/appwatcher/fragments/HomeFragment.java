/**
 * 
 */
package com.crusader.carpediem.appwatcher.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.crusader.carpediem.appwatcher.R;
import com.crusader.carpediem.appwatcher.constants.AppConstants;
import com.crusader.carpediem.appwatcher.interfaces.HomeListenerInterface;
import com.crusader.carpediem.appwatcher.logger.AppLogger;
import com.crusader.carpediem.appwatcher.utils.AppUtils;

/**
 * @author pavan
 * 
 */
public class HomeFragment extends CommonMobileFragment implements
		OnPageChangeListener, OnClickListener {

	public static final int NUM_PAGES = 2;

	private View mRootView;

	private int[] circleArray = { R.id.circle_1, R.id.circle_2 };
	private ImageView[] welcomeCircles = new ImageView[4];

	private ViewPager mViewPager;

	private ScreenSlidePagerAdapter mPagerAdapter;

	int currentPos = 0;
	int lastSelectedPos = -1;
	private int pageId = 0;

	private ImageView settingMnu, statsMnu;

	private HomeListenerInterface headerEventListener;

	private ImageView shareMnu;

	private ImageView monkImg;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		mRootView = inflater.inflate(R.layout.fragment_home, container, false);

		setupViews();
		setupControllers();
		return mRootView;
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		try {
			headerEventListener = (HomeListenerInterface) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement HomeListenerInterface");
		}
	}

	@Override
	public void onStart() {
		boolean incognitoModeEnabled = AppUtils.getPrefs(mContext).getBoolean(
				AppConstants.ESCAPE_FLAG, false);
		if (!incognitoModeEnabled)
			startBroadcastListener();
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		boolean incognitoModeEnabled = AppUtils.getPrefs(mContext).getBoolean(
				AppConstants.ESCAPE_FLAG, false);
		if (!incognitoModeEnabled)
			stopBroadcastListener();
		super.onDestroy();
	}

	private void setupViews() {

		monkImg = (ImageView) mRootView.findViewById(R.id.monk_img);
		settingMnu = (ImageView) mRootView.findViewById(R.id.mnu_settings);
		statsMnu = (ImageView) mRootView.findViewById(R.id.mnu_stats);
		shareMnu = (ImageView) mRootView.findViewById(R.id.mnu_share);
		for (int i = 0; i < NUM_PAGES; i++) {
			welcomeCircles[i] = (ImageView) mRootView
					.findViewById(circleArray[i]);
		}
		mViewPager = (ViewPager) mRootView.findViewById(R.id.home_pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOffscreenPageLimit(NUM_PAGES);
		mViewPager.setOnPageChangeListener(this);

		updateSelectedCircle(0);
	}

	private void setupControllers() {
		settingMnu.setOnClickListener(this);
		statsMnu.setOnClickListener(this);
		shareMnu.setOnClickListener(this);
		monkImg.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					monkImg.setImageResource(R.drawable.monk_wink);
					return true;
				} else if(event.getAction() == MotionEvent.ACTION_UP){
					setMonkImage();
					return true;
				}
				return false;
			}
		});

		setMonkImage();
	}

	private void setMonkImage() {
		int[] timeArray = AppUtils.calculateTime(AppUtils.getPrefs(mContext)
				.getLong(AppConstants.SCREEN_ACTIVE_TIME, 0));
		if (timeArray[0] < 1) {
			monkImg.setImageResource(R.drawable.monk_smile);
		} else if (timeArray[0] < 2) {
			monkImg.setImageResource(R.drawable.monk_sad);
		} else {
			monkImg.setImageResource(R.drawable.monk_dissatisfied);
		}
	}

	private void startBroadcastListener() {
		IntentFilter screenStateFilter = new IntentFilter();
		screenStateFilter.addAction(Intent.ACTION_USER_PRESENT);
		mContext.registerReceiver(broadcastReceiver, screenStateFilter);
	}

	private void stopBroadcastListener() {
		mContext.unregisterReceiver(broadcastReceiver);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
				AppLogger
						.msg("Home --> intent.getAction().equals(Intent.ACTION_USER_PRESENT)");
				if (timesCheckedFragment != null) {
					timesCheckedFragment.incrementTimeChecked();
				}
			}
		}
	};

	public TimesCheckedFragment timesCheckedFragment;
	private TimeSpentFragment timeSpentFragment;

	private void updateSelectedCircle(int position) {
		if (lastSelectedPos != -1) {
			welcomeCircles[lastSelectedPos]
					.setImageResource(R.drawable.circle_2);
		}

		welcomeCircles[position].setImageResource(R.drawable.circle_1);
		lastSelectedPos = position;
	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new TimeSpentFragment();
				timeSpentFragment = (TimeSpentFragment) fragment;
				break;
			case 1:
				fragment = new TimesCheckedFragment();
				timesCheckedFragment = (TimesCheckedFragment) fragment;
			default:
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		pageId = position;
		updateSelectedCircle(position);
	}

	private void onShareMenuClicked() {
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

		shareIntent.setType("text/plain");

		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hello");

		String shareMessage = "I tried Carpe Diem, its so cool and helpfull in monitoring by mobile usage, Do try it out. .";

		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);

		startActivity(Intent.createChooser(shareIntent,
				"Insert share chooser title here"));
	}

	@Override
	public void onClick(View v) {
		if (v == settingMnu) {
			headerEventListener.onSettingMenuCLicked();
		} else if (v == statsMnu) {
			headerEventListener.onStatsMenuClicked();
		} else if (v == shareMnu) {
			onShareMenuClicked();
		}
	}

	public void startRunnableProcesses() {
		startBroadcastListener();
		timeSpentFragment.initializeTimerTask();
	}

	public void stopRunnableProcesses() {
		stopBroadcastListener();
		timeSpentFragment.stoptimertask();
	}

}
