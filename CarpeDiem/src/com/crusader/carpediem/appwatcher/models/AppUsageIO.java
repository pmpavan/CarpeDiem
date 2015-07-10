package com.crusader.carpediem.appwatcher.models;

public class AppUsageIO {

	private int uniqueId;
	private String appName;
	private String appPackageName;
	private double appUsage;

	public AppUsageIO(int parseInt, String string, String string2,
			double double1) {
		this.uniqueId = parseInt;
		this.appName = string;
		this.appPackageName = string2;
		this.appUsage = double1;
	}

	public AppUsageIO() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName
	 *            the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @return the appUsage
	 */
	public double getAppUsage() {
		return appUsage;
	}

	/**
	 * @param appUsage
	 *            the appUsage to set
	 */
	public void setAppUsage(double appUsage) {
		this.appUsage = appUsage;
	}

	public String getAppPackageName() {
		return appPackageName;
	}

	public void setAppPackageName(String appPackageName) {
		this.appPackageName = appPackageName;
	}

	public int getId() {
		return uniqueId;
	}

	public void setId(int uniqueId) {
		this.uniqueId = uniqueId;
	}
}
