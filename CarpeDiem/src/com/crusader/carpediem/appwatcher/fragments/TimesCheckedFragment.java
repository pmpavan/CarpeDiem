/**
 * 
 */
package com.crusader.carpediem.appwatcher.fragments;

import com.crusader.carpediem.appwatcher.R;
import com.crusader.carpediem.appwatcher.constants.AppConstants;
import com.crusader.carpediem.appwatcher.logger.AppLogger;
import com.crusader.carpediem.appwatcher.utils.AppUtils;
import com.crusader.carpediem.appwatcher.views.CustomTypefaceSpan;
import com.crusader.carpediem.appwatcher.views.MonkTextView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author pavan
 * 
 */
public class TimesCheckedFragment extends CommonMobileFragment {

	private View mRootView;
	private MonkTextView timeChkdCntTxtVw;
	private MonkTextView timechkdQuoteTxtVw;
	private static long timeChecked = 0;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mRootView = inflater.inflate(R.layout.fragment_times_checked,
				container, false);
		setupViews();
		setupControllers();
		return mRootView;
	}

	private void setupViews() {
		timeChkdCntTxtVw = (MonkTextView) mRootView
				.findViewById(R.id.time_checked_count_txtvw);
		timechkdQuoteTxtVw = (MonkTextView) mRootView
				.findViewById(R.id.time_checked_quote_txtvw);
	}

	private void setupControllers() {
		timeChecked = AppUtils.getPrefs(mContext).getLong(
				AppConstants.SCREEN_ON_COUNT, 0);
		setSpannableTime(timeChecked);

	}

	public void setSpannableTime(long value) {
		String string = value + " ";
		if (value <= 1) {
			string += "time";
		} else {
			string += "times";
		}

		SpannableStringBuilder sb = new SpannableStringBuilder(string);

		int start1 = 0;
		int end1 = String.valueOf(value).length();
		sb.setSpan(
				new CustomTypefaceSpan("", CommonMobileFragment
						.getCustomTypeface(mContext, "fonts/OpenSans-Bold.ttf")),
				start1, end1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		sb.setSpan(new RelativeSizeSpan(2f), start1, end1,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		timeChkdCntTxtVw.setText(sb);
	}

	public void incrementTimeChecked() {
		timeChecked += 1;
		setSpannableTime(timeChecked);
		AppLogger.msg("timeChecked --> " + timeChecked);
	}

	public long getTimeChecked() {
		return timeChecked;
	}

	public void setTimeChecked(long timeChecked) {
		this.timeChecked = timeChecked;
	}
}
