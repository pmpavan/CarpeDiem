package com.crusader.carpediem.appwatcher.views.adapters;

import android.view.View;

/**
 * @author Rinoy
 * 
 * @param <T>
 */
public interface PopulationListener<T> {

	public void populateFrom(View v, int position, final T row, View[] views);

	// Call when the row is created first time.
	public void onRowCreate(View[] views);

}
