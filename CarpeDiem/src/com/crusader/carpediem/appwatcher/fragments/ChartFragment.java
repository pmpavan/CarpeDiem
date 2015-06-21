package com.crusader.carpediem.appwatcher.fragments;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crusader.carpediem.appwatcher.R;
import com.crusader.carpediem.appwatcher.utils.AppUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.LargeValueFormatter;

public class ChartFragment extends CommonMobileFragment implements
		OnChartValueSelectedListener {

	private View mRootView;
	private BarChart mChart;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab1.xml
		mRootView = inflater.inflate(R.layout.fragment_chart, container, false);
		setupViews();
		setupControllers();
		return mRootView;
	}

	private void setupViews() {
		mChart = (BarChart) mRootView.findViewById(R.id.stats_chart);

	}

	private void setupControllers() {
		mChart.setOnChartValueSelectedListener(this);
		mChart.setDescription("");

		// mChart.setDrawBorders(true);

		// scaling can now only be done on x- and y-axis separately
		mChart.setPinchZoom(false);

		mChart.setDrawBarShadow(false);

		mChart.setDrawGridBackground(false);

		Typeface tf = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/OpenSans-Regular.ttf");

		Legend l = mChart.getLegend();
		l.setPosition(LegendPosition.RIGHT_OF_CHART_INSIDE);
		l.setTypeface(tf);

		XAxis xl = mChart.getXAxis();
		xl.setTypeface(tf);

		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.setTypeface(tf);
		leftAxis.setValueFormatter(new LargeValueFormatter());
		leftAxis.setDrawGridLines(false);
		leftAxis.setSpaceTop(25f);

		mChart.getAxisRight().setEnabled(false);
		fillData();
	}

	private void fillData() {

		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			xVals.add("Day " + (i + 1));
		}

		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
		ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();

//		AppUtils.storeInPreferences(mContext, "PREVIOUS_DAY_DATA",
//				"1:10,2:20,30:30,40:40,50:50");
		//String chartValueStr = AppUtils.getPrefs(getActivity()).getString("PREVIOUS_DAY_DATA", "0:0,0:0,0:0,0:0,0:0");
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String chartValueStr = preferences.getString(
				"PREVIOUS_DAY_DATA", "0:0,0:0,0:0,0:0,0:0");
		
		String[] mobileUsageArray = chartValueStr.split(",");
		int i = 0;
		for (String str : mobileUsageArray) {
			String[] dayUsageArr = str.split(":");
			yVals1.add(new BarEntry(Integer.parseInt(dayUsageArr[0]), i));
			yVals2.add(new BarEntry(Integer.parseInt(dayUsageArr[1]), i));
			i += 1;
		}
		BarDataSet set1 = new BarDataSet(yVals1, "Mobile Usage");
		set1.setColor(Color.rgb(104, 241, 175));
		BarDataSet set2 = new BarDataSet(yVals2, "Mobile Checkins");
		set2.setColor(Color.rgb(164, 228, 251));

		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set1);
		dataSets.add(set2);

		BarData data = new BarData(xVals, dataSets);
		// data.setValueFormatter(new LargeValueFormatter());

		// add space between the dataset groups in percent of bar-width
		data.setGroupSpace(80f);

		mChart.setData(data);

	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub

	}

}
