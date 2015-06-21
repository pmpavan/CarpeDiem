/**
 * 
 */
package com.crusader.carpediem.appwatcher.fragments;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.crusader.carpediem.appwatcher.R;
import com.crusader.carpediem.appwatcher.constants.AppConstants;
import com.crusader.carpediem.appwatcher.interfaces.HomeListenerInterface;
import com.crusader.carpediem.appwatcher.logger.AppLogger;
import com.crusader.carpediem.appwatcher.utils.AppUtils;
import com.crusader.carpediem.appwatcher.views.MonkTextView;

/**
 * @author pavan
 * 
 */
public class SettingsFragment extends CommonMobileFragment implements
		OnClickListener {

	private View mRootView;
	private ImageView incognitoSlider;
	private boolean incognitoModeEnabled;
	private HomeListenerInterface headerEventListener;
	private ImageView headerBack;
	private SeekBar dailyLimitSeekbar;
	protected long hours;
	private ImageView zenSlider;
	private boolean zenModeEnabled;
	private AudioManager myAudioManager;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mRootView = inflater.inflate(R.layout.fragment_settings, container,
				false);

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

	private void setupViews() {
		incognitoSlider = (ImageView) mRootView
				.findViewById(R.id.slider_incognito);
		zenSlider = (ImageView) mRootView.findViewById(R.id.slider_zen);
		headerBack = (ImageView) mRootView.findViewById(R.id.mnu_back);
		dailyLimitSeekbar = (SeekBar) mRootView
				.findViewById(R.id.daily_limit_seekbar);

		final MonkTextView seekBarValue = (MonkTextView) mRootView
				.findViewById(R.id.daily_limit_txtvw);
		// seekBarValue.setText(tvRadius.getText().toString().trim());

		dailyLimitSeekbar
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						hours = progress;

						AppUtils.storeInPreferences(mContext,
								AppConstants.DAILY_THRSHOLD_VALUE, hours);

						String hoursStr;
						if (progress % 2 == 0) {
							Double d = Double.valueOf(hours);
							int i = d.intValue();
							hoursStr = i + " hr";
						} else {
							hoursStr = String.valueOf(hours) + " hr";
						}
						if (hours > 1) {
							hoursStr += "s";
						}
						seekBarValue.setText(hoursStr);
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

					}
				});

	}

	private void setupControllers() {
		incognitoModeEnabled = AppUtils.getPrefs(mContext).getBoolean(
				AppConstants.ESCAPE_FLAG, false);
		if (!incognitoModeEnabled) {
			incognitoSlider.setImageResource(R.drawable.slide_off);
		} else {
			incognitoSlider.setImageResource(R.drawable.slide_on);
		}

		zenModeEnabled = AppUtils.getPrefs(mContext).getBoolean(
				AppConstants.ZEN_FLAG, false);
		if (!zenModeEnabled) {
			zenSlider.setImageResource(R.drawable.slide_off);
		} else {
			zenSlider.setImageResource(R.drawable.slide_on);
		}

		incognitoSlider.setOnClickListener(this);
		zenSlider.setOnClickListener(this);
		headerBack.setOnClickListener(this);
		long value = AppUtils.getPrefs(mContext).getLong(
				AppConstants.DAILY_THRSHOLD_VALUE, 0);
		AppLogger.msg("value --> " + value);
		int progress = (int) value;
		AppLogger.msg("progress --> " + progress);
		dailyLimitSeekbar.setProgress(progress);
	}

	private void toggleSlider() {
		incognitoModeEnabled = !incognitoModeEnabled;
		if (!incognitoModeEnabled) {
			incognitoSlider.setImageResource(R.drawable.slide_off);
		} else {
			incognitoSlider.setImageResource(R.drawable.slide_on);
		}
		AppUtils.storeInPreferences(mContext, AppConstants.ESCAPE_FLAG,
				incognitoModeEnabled);
		headerEventListener.onIncognitoEnabled(incognitoModeEnabled);
	}

	private void toggleZenSlider() {
		zenModeEnabled = AppUtils.getPrefs(mContext).getBoolean(
				AppConstants.ZEN_FLAG, false);
		zenModeEnabled = !zenModeEnabled;
		if (!zenModeEnabled) {
			zenSlider.setImageResource(R.drawable.slide_off);
		} else {
			zenSlider.setImageResource(R.drawable.slide_on);
		}
		AppUtils.storeInPreferences(mContext, AppConstants.ZEN_FLAG,
				zenModeEnabled);

		// get the instance of AudioManager class
		if (zenModeEnabled) {
			myAudioManager = (AudioManager) mContext
					.getSystemService(Context.AUDIO_SERVICE);
			myAudioManager
					.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
			myAudioManager.setStreamMute(AudioManager.STREAM_RING, false);
		} else {
			myAudioManager = (AudioManager) mContext
					.getSystemService(Context.AUDIO_SERVICE);
			myAudioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION,
					false);
			myAudioManager.setStreamMute(AudioManager.STREAM_RING, false);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == incognitoSlider) {
			toggleSlider();
		} else if (v == headerBack) {
			headerEventListener.onBackClicked();
		} else if (v == zenSlider) {
			toggleZenSlider();
		}
	}
}
