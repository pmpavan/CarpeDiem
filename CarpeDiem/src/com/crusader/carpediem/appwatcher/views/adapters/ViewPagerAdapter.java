package com.crusader.carpediem.appwatcher.views.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.crusader.carpediem.appwatcher.fragments.TrendsFragment;
import com.crusader.carpediem.appwatcher.fragments.ChartFragment;
 
public class ViewPagerAdapter extends FragmentPagerAdapter {
 
	final int PAGE_COUNT = 2;
	// Tab Titles
	private String tabtitles[] = new String[] { "DAILY", "WEEKLY" };
	Context context;
 
	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}
 
	@Override
	public int getCount() {
		return PAGE_COUNT;
	}
 
	@Override
	public Fragment getItem(int position) {
		switch (position) {
 
			// Open FragmentTab1.java
		case 0:
			TrendsFragment fragmenttab1 = new TrendsFragment();
			return fragmenttab1;
 
			// Open FragmentTab2.java
		case 1:
			ChartFragment fragmenttab2 = new ChartFragment();
			return fragmenttab2;
 
		}
		return null;
	}
 
	@Override
	public CharSequence getPageTitle(int position) {
		return tabtitles[position];
	}
}
