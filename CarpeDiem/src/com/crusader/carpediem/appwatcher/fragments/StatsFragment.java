/**
 * 
 */
package com.crusader.carpediem.appwatcher.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.crusader.carpediem.appwatcher.R;
import com.crusader.carpediem.appwatcher.interfaces.HomeListenerInterface;
import com.crusader.carpediem.appwatcher.views.adapters.ViewPagerAdapter;

/**
 * @author pavan
 * 
 */
public class StatsFragment extends CommonMobileFragment implements
		OnClickListener {
	private View mRootView;
	private ImageView backMnu;
	private HomeListenerInterface headerEventListener;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mRootView = inflater.inflate(R.layout.fragment_stats, container, false);

		setupViews();
		setupControllers();
		return mRootView;
	}

	private void setupViews() {
		ViewPager viewPager = (ViewPager) mRootView.findViewById(R.id.pager);

		viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));

		backMnu = (ImageView) mRootView.findViewById(R.id.mnu_back);
	}

	private void setupControllers() {
		backMnu.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == backMnu) {
			headerEventListener.onBackClicked();
		}
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
}
