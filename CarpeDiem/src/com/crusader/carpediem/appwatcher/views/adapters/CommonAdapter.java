package com.crusader.carpediem.appwatcher.views.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import com.crusader.carpediem.appwatcher.R;

/**
 * @author Rinoy
 * 
 * @param <T>
 */
public class CommonAdapter<T> extends BaseAdapter {

	private List<T> elements;
	private Context mContext;

	private int layoutID = 0;
	private int[] viewIds;
	private int lastSelectedPosition;
	private int selectedRowColor = -1;
	private int unSelectedRowColor = Color.parseColor("#00000000");
	private int selectedResourceRowColor = -1;
	private int oddColor = -1;
	private int evenColor = -1;
	private PopulationListener<T> listener;
	private Animation rowAnimation;
	private int lastPosition = -1;
	private int ignorePos = -1;
	private boolean isAnimEnabled = false;

	public CommonAdapter(Context context, final List<T> elem) {
		this.mContext = context;
		this.elements = elem;
	}

	public void setLayoutTextViews(int layoutID, int[] viewIDs) {
		this.layoutID = layoutID;
		this.viewIds = viewIDs;
	}

	public void clear() {
		elements.clear();
		notifyDataSetChanged();
	}

	public void add(final T row) {
		elements.add(row);
	}

	public void add(int position, final T row) {
		elements.add(position, row);
	}

	public void addAll(final List<T> rows) {
		elements.addAll(rows);
	}

	public void set(final List<T> rows) {
		elements = rows;
	}

	public void remove(int position) {
		elements.remove(position);
	}

	public int getCount() {
		return elements.size();
	}

	public T getItem(int position) {
		if (getCount() > 0 && position < getCount())
			return elements.get(position);
		return null;
	}

	public List<T> getItems() {
		return elements;
	}

	public long getItemId(int position) {
		return position;
	}

	public void setPopulationListener(PopulationListener<T> listener) {
		this.listener = listener;
	}

	public void setListAnimation(Animation anim) {
		this.rowAnimation = anim;
	}

	public void setIgnorePosition(int ignorePos) {
		this.ignorePos = ignorePos;
	}

	public void enableListItemAnimation(boolean isEnabled) {
		this.isAnimEnabled = isEnabled;
	}

	public View getView(int position, View row, ViewGroup parent) {
		RowHolder holder = null;

		if (row == null) {

			row = (View) LayoutInflater.from(mContext).inflate(layoutID,
					parent, false);

			holder = new RowHolder(row, viewIds);
			listener.onRowCreate(holder.getViews());
			row.setTag(holder);

		} else {
			holder = (RowHolder) row.getTag();
		}

		if (row != null) {

			if (oddColor != -1) {
				if (position % 2 == 1) {
					row.setBackgroundColor(oddColor);
				} else {
					row.setBackgroundColor(evenColor);
				}
			}

			if (selectedRowColor != -1) {
				if (lastSelectedPosition == position) {
					// Selected Row Color
					row.setBackgroundColor(selectedRowColor);
				} else {
					// UnSelected Row Color
					row.setBackgroundColor(unSelectedRowColor);
				}
			}

			if (selectedResourceRowColor != -1) {
				if (lastSelectedPosition == position) {
					// Selected Row Color
					row.setBackgroundResource(selectedResourceRowColor);
				} else {
					// UnSelected Row Color
					row.setBackgroundColor(unSelectedRowColor);
				}
			}

		}

		/**
		 * Here we store the position value with key as layoutID to handle
		 * alternate colors for list rows.
		 * 
		 * please refer OnItemClickListener (onListClick in watch list) for
		 * listView.
		 * 
		 */
		row.setTag(layoutID, position);

		listener.populateFrom(row, position, elements.get(position),
				holder.getViews());

		DisplayMetrics metrics = new DisplayMetrics();
		((FragmentActivity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);

		if (isAnimEnabled) {

			if (position > ignorePos) {
				// Animation animation = new TranslateAnimation(0, 0,
				// metrics.heightPixels, 0);
				// animation.setDuration(500);
				Animation animation = AnimationUtils.loadAnimation(mContext,
						(position > lastPosition) ? R.anim.up_from_bottom
								: R.anim.down_from_top);

				row.startAnimation(animation);
			}
			lastPosition = position;
		}
		return (row);
	}

	public void setLastSelectedPosition(int position) {
		lastSelectedPosition = position;
	}

	public void setSelectedRowColor(int selectedRowColor) {
		this.selectedRowColor = selectedRowColor;
	}

	public void setUnSelectedRowColor(int unSelectedRowColor) {
		this.unSelectedRowColor = unSelectedRowColor;
	}

	public void setResourceRowColor(int selectedResourceRowColor) {
		this.selectedResourceRowColor = selectedResourceRowColor;
	}

	public void setAlternativeRowColor(int oddColor, int evenColor) {
		this.oddColor = oddColor;
		this.evenColor = evenColor;
	}

}
