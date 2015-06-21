/**
 * 
 */
package com.crusader.carpediem.appwatcher.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.crusader.carpediem.appwatcher.R;

/**
 * @author pavan
 * 
 */
public class MonkTextView extends TextView {

	public MonkTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		onInitTypeface(context, attrs, defStyleAttr);
	}

	public MonkTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		onInitTypeface(context, attrs, 0);
	}

	public MonkTextView(Context context) {
		super(context);
		onInitTypeface(context, null, 0);
	}

	private void onInitTypeface(Context context, AttributeSet attrs,
			int defStyle) {
		// Typeface.createFromAsset doesn't work in the layout editor, so
		// skipping.
		if (isInEditMode()) {
			return;
		}

		int typefaceValue = 0;
		if (attrs != null) {
			TypedArray values = context.obtainStyledAttributes(attrs,
					R.styleable.MonkTextView, defStyle, 0);
			typefaceValue = values.getInt(R.styleable.MonkTextView_typeface, 0);
			values.recycle();
		}

		Typeface opensansTypeface = TypefaceManager.obtaintTypeface(context,
				typefaceValue);
		setTypeface(opensansTypeface);
	}
}
