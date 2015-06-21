/**
 * 
 */
package com.crusader.carpediem.appwatcher.fragments;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crusader.carpediem.appwatcher.R;
import com.crusader.carpediem.appwatcher.constants.AppConstants;
import com.crusader.carpediem.appwatcher.utils.AppUtils;
import com.crusader.carpediem.appwatcher.views.CustomTypefaceSpan;
import com.crusader.carpediem.appwatcher.views.MonkTextView;

/**
 * @author pavan
 * 
 */
public class TimeSpentFragment extends CommonMobileFragment {

	private View mRootView;
	private MonkTextView timeSpentCntTxtVw;
	private MonkTextView timeSpentQuoteTxtVw;
	private int[] timeArray;
	private Timer timer;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mRootView = inflater.inflate(R.layout.fragment_time_spent, container,
				false);
		setupViews();
		setupControllers();
		return mRootView;
	}

	@Override
	public void onStop() {
		boolean incognitoModeEnabled = AppUtils.getPrefs(mContext).getBoolean(
				AppConstants.ESCAPE_FLAG, false);
		if (!incognitoModeEnabled)
			stoptimertask();
		super.onStop();
	}

	@Override
	public void onResume() {
		boolean incognitoModeEnabled = AppUtils.getPrefs(mContext).getBoolean(
				AppConstants.ESCAPE_FLAG, false);
		if (!incognitoModeEnabled)
			initializeTimerTask();
		super.onResume();
	}

	private void setupViews() {
		timeSpentCntTxtVw = (MonkTextView) mRootView
				.findViewById(R.id.time_spent_count_txtvw);
		timeSpentQuoteTxtVw = (MonkTextView) mRootView
				.findViewById(R.id.time_spent_quote_txtvw);

	}

	private void setupControllers() {
		timeArray = AppUtils.calculateTime(AppUtils.getPrefs(mContext).getLong(
				AppConstants.SCREEN_ACTIVE_TIME, 0));
		setSpannableTime(timeArray, timeSpentCntTxtVw);
	}

	public void initializeTimerTask() {
		timer = new Timer();
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				if (timeArray[1] < 59) {
					timeArray[1] += 1;
				} else {
					timeArray[0] += 1;
					timeArray[1] = 0;
				}
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {

						setSpannableTime(timeArray, timeSpentCntTxtVw);

					}
				});

			}
		};
		timer.schedule(timerTask, 60000, 60000);
	}

	public void stoptimertask() {
		// stop the timer, if it's not already null
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
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
		timeSpentCntTxtVw.setText(sb);
	}
}
