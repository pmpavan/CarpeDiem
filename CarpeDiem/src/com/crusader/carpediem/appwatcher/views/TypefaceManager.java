package com.crusader.carpediem.appwatcher.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;

/**
 * The manager of proxima nova typefaces.
 * 
 */
public class TypefaceManager {

	/*
	 * Available values ​​for the "typeface" attribute.
	 */

	private final static int OPENSANS_REGULAR = 0;
	private final static int OPENSANS_BOLD = 1;
	private final static int OPENSANS_SEMIBOLD = 2;
	private final static int OPENSANS_ITALIC = 3;
	private final static int OPENSANS_LIGHT = 4;

	/**
	 * Array of created typefaces for later reused.
	 */
	private final static SparseArray<Typeface> mTypefaces = new SparseArray<Typeface>(
			20);

	/**
	 * Obtain typeface.
	 * 
	 * @param context
	 *            The Context the widget is running in, through which it can
	 *            access the current theme, resources, etc.
	 * @param typefaceValue
	 *            The value of "typeface" attribute
	 * @return specify {@link Typeface}
	 * @throws IllegalArgumentException
	 *             if unknown `typeface` attribute value.
	 */
	public static Typeface obtaintTypeface(Context context, int typefaceValue)
			throws IllegalArgumentException {
		Typeface typeface = mTypefaces.get(typefaceValue);
		if (typeface == null) {
			typeface = createTypeface(context, typefaceValue);
			mTypefaces.put(typefaceValue, typeface);
		}
		return typeface;
	}

	/**
	 * Create typeface from assets.
	 * 
	 * @param context
	 *            The Context the widget is running in, through which it can
	 *            access the current theme, resources, etc.
	 * @param typefaceValue
	 *            The value of "typeface" attribute
	 * @return Proxima {@link Typeface}
	 * @throws IllegalArgumentException
	 *             if unknown `typeface` attribute value.
	 */
	private static Typeface createTypeface(Context context, int typefaceValue)
			throws IllegalArgumentException {
		Typeface typeface;
		switch (typefaceValue) {
		case OPENSANS_REGULAR:
			typeface = Typeface.createFromAsset(context.getAssets(),
					"fonts/OpenSans-Regular.ttf");
			break;
		case OPENSANS_BOLD:
			typeface = Typeface.createFromAsset(context.getAssets(),
					"fonts/OpenSans-Bold.ttf");
			break;
		case OPENSANS_SEMIBOLD:
			typeface = Typeface.createFromAsset(context.getAssets(),
					"fonts/OpenSans-Semibold.ttf");
			break;
		case OPENSANS_ITALIC:
			typeface = Typeface.createFromAsset(context.getAssets(),
					"fonts/OpenSans-Italic.ttf");
			break;
		case OPENSANS_LIGHT:
			typeface = Typeface.createFromAsset(context.getAssets(),
					"fonts/OpenSans-Light.ttf");
			break;
		default:
			throw new IllegalArgumentException(
					"Unknown `typeface` attribute value " + typefaceValue);
		}
		return typeface;
	}

}
