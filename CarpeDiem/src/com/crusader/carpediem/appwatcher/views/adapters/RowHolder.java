package com.crusader.carpediem.appwatcher.views.adapters;

import android.view.View;

/**
 * @author Rinoy
 * 
 */
public class RowHolder {
	private View[] views;

	public RowHolder(final View row, final int[] viewIds) {
		if (row != null && viewIds != null) {
			views = new View[viewIds.length];
			for (int i = 0; i < viewIds.length; i++)
				views[i] = row.findViewById(viewIds[i]);
		}
	}

	public View[] getViews() {
		return views;
	}
}
