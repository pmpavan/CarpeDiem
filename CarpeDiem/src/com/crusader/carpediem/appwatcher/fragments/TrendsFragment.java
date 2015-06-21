package com.crusader.carpediem.appwatcher.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.crusader.carpediem.appwatcher.R;
import com.crusader.carpediem.appwatcher.constants.AppConstants;
import com.crusader.carpediem.appwatcher.models.AppUsageIO;
import com.crusader.carpediem.appwatcher.utils.AppUtils;
import com.crusader.carpediem.appwatcher.views.CustomTypefaceSpan;
import com.crusader.carpediem.appwatcher.views.MonkTextView;
import com.crusader.carpediem.appwatcher.views.adapters.CommonAdapter;
import com.crusader.carpediem.appwatcher.views.adapters.PopulationListener;

public class TrendsFragment extends CommonMobileFragment {

	private View mRootView;
	private MonkTextView todayUsageTxtVw;
	private ListView appUsageList;
	private CommonAdapter<AppUsageIO> inboxListAdapter;
	private ArrayList<AppUsageIO> inboxModel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater
				.inflate(R.layout.fragment_trends, container, false);
		setupViews();
		setupControllers();
		return mRootView;
	}

	private void setupViews() {
		todayUsageTxtVw = (MonkTextView) mRootView
				.findViewById(R.id.today_uage_stat_txtvw);
		appUsageList = (ListView) mRootView.findViewById(R.id.trends_listview);

	}

	private void setupControllers() {
		int[] timeArray = AppUtils.calculateTime(AppUtils.getPrefs(mContext)
				.getLong(AppConstants.SCREEN_ACTIVE_TIME, 0));
		setSpannableTime(timeArray, todayUsageTxtVw);

		setupListAdapter();
		ArrayList<AppUsageIO> appUsageIOs = new ArrayList<AppUsageIO>();
		AppUsageIO appUsageIO = new AppUsageIO();
		appUsageIO.setAppName("Facebook");
		appUsageIO.setAppUsage(50);
		appUsageIOs.add(appUsageIO);
		appUsageIO = new AppUsageIO();
		appUsageIO.setAppName("Watsapp");
		appUsageIO.setAppUsage(30);
		appUsageIOs.add(appUsageIO);
		appUsageIO = new AppUsageIO();
		appUsageIO.setAppName("Cricinfo");
		appUsageIO.setAppUsage(10);
		appUsageIOs.add(appUsageIO);
		appUsageIO = new AppUsageIO();
		appUsageIO.setAppName("Gmail");
		appUsageIO.setAppUsage(5);
		appUsageIOs.add(appUsageIO);
		appUsageIO = new AppUsageIO();
		appUsageIO.setAppName("Calls");
		appUsageIO.setAppUsage(5);
		appUsageIOs.add(appUsageIO);
		updateInboxList(appUsageIOs);
	}

	private void setSpannableTime(int[] array, MonkTextView textView) {
		String string = array[0] + "hr";
		if (array[0] > 1) {
			string += "s";
		}
		string += " " + array[1] + "min";
		if (array[1] > 1) {
			string += "s";
		}
		SpannableStringBuilder sb = new SpannableStringBuilder(string);

		int start1 = 0;
		int end1 = String.valueOf(array[0]).length();
		sb.setSpan(
				new CustomTypefaceSpan("", CommonMobileFragment
						.getCustomTypeface(mContext, "fonts/OpenSans-Bold.ttf")),
				start1, end1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		sb.setSpan(new RelativeSizeSpan(2f), start1, end1,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		int start2 = 0;

		if (array[0] <= 1) {
			start2 = end1 + 3;
		} else {
			start2 = end1 + 4;
		}

		int end2 = start2 + String.valueOf(array[1]).length();

		sb.setSpan(
				new CustomTypefaceSpan("", CommonMobileFragment
						.getCustomTypeface(mContext, "fonts/OpenSans-Bold.ttf")),
				start2, end2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		sb.setSpan(new RelativeSizeSpan(2f), start2, end2,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(sb);
	}

	private void setupListAdapter() {
		inboxModel = new ArrayList<AppUsageIO>();
		inboxListAdapter = new CommonAdapter<AppUsageIO>(mContext, inboxModel);

		int[] viewIds = { R.id.app_name, R.id.app_usage };

		inboxListAdapter.setLayoutTextViews(R.layout.list_item_appusage,
				viewIds);

		inboxListAdapter
				.setPopulationListener(new PopulationListener<AppUsageIO>() {

					@Override
					public void populateFrom(View v, int position,
							final AppUsageIO activity, View[] views) {
						((MonkTextView) views[0]).setText(AppUtils
								.toCamelcase(activity.getAppName()));
						((MonkTextView) views[1]).setText(activity
								.getAppUsage() + "%");
					}

					@Override
					public void onRowCreate(View[] views) {

					}
				});

		inboxListAdapter.enableListItemAnimation(true);
		appUsageList.setAdapter(inboxListAdapter);
	}

	private void updateInboxList(ArrayList<AppUsageIO> appUsageIOs) {

		inboxListAdapter.addAll(appUsageIOs);
		inboxListAdapter.notifyDataSetChanged();

	}

}
