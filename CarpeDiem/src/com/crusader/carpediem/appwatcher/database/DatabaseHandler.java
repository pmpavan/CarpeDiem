package com.crusader.carpediem.appwatcher.database;

import java.util.ArrayList;

import com.crusader.carpediem.appwatcher.models.AppUsageIO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "appUsageManager";

	// App Usage table name
	private static final String TABLE_APPUSAGE = "appUsage";

	// App Usage Table Columns names
	private static final String KEY_NAME = "name";
	private static final String KEY_PACKAGE_NAME = "package_name";
	private static final String KEY_DURATION = "duration";
	private final ArrayList<AppUsageIO> package_list = new ArrayList<AppUsageIO>();

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_APPUSAGE_TABLE = "CREATE TABLE " + TABLE_APPUSAGE + "("
				+ KEY_PACKAGE_NAME + " TEXT PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_DURATION + " DOUBLE" + ")";
		db.execSQL(CREATE_APPUSAGE_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPUSAGE);
		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	public void addAppUsage(AppUsageIO contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_PACKAGE_NAME, contact.getAppPackageName());
		values.put(KEY_NAME, contact.getAppName()); // Name
		values.put(KEY_DURATION, contact.getAppUsage());
		// Inserting Row
		db.insert(TABLE_APPUSAGE, null, values);
		db.close(); // Closing database connection
	}

	public AppUsageIO getAppUsage(String packageName) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_APPUSAGE, new String[] {
				KEY_PACKAGE_NAME, KEY_NAME, KEY_DURATION }, KEY_PACKAGE_NAME
				+ "ilike ?", new String[] { packageName }, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();

		AppUsageIO appUsage = new AppUsageIO(cursor.getString(0),
				cursor.getString(1), cursor.getDouble(2));
		cursor.close();
		db.close();

		return appUsage;
	}

	public ArrayList<AppUsageIO> getAppUsages() {
		try {
			package_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_APPUSAGE;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					AppUsageIO appUsageIO = new AppUsageIO();
					appUsageIO.setAppPackageName(cursor.getString(0));
					appUsageIO.setAppName(cursor.getString(1));
					appUsageIO.setAppUsage(cursor.getDouble(2));
					package_list.add(appUsageIO);
				} while (cursor.moveToNext());
			}

			cursor.close();
			db.close();
			return package_list;
		} catch (Exception e) {
			Log.e("all_appusages", "" + e);
		}

		return package_list;
	}

	public int updateAppUsages(AppUsageIO appUsageIO) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PACKAGE_NAME, appUsageIO.getAppPackageName());
		values.put(KEY_NAME, appUsageIO.getAppName()); // Name
		values.put(KEY_DURATION, appUsageIO.getAppUsage());

		// updating row
		return db.update(TABLE_APPUSAGE, values, KEY_PACKAGE_NAME + " ilike ?",
				new String[] { appUsageIO.getAppPackageName() });
	}

	public int updateAppUsageDuration(String packageName, double duration) {

		AppUsageIO appUsageIO = getAppUsage(packageName);

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DURATION, appUsageIO.getAppUsage() + duration);

		// updating row
		return db.update(TABLE_APPUSAGE, values, KEY_PACKAGE_NAME + " ilike ?",
				new String[] { packageName });
	}

	public int resetAppUsageDuration() {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DURATION, 0.0);

		// updating row
		return db.update(TABLE_APPUSAGE, values, "", null);
	}

	public void deleteAppUsages(String packageName) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_APPUSAGE, KEY_PACKAGE_NAME + " ilike ?",
				new String[] { packageName });
		db.close();
	}

	public int getAllAppUsages() {
		String countQuery = "SELECT  * FROM " + TABLE_APPUSAGE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}
