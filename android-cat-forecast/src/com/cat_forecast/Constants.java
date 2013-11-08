package com.cat_forecast;

import android.os.Environment;

public class Constants {

	public static final String APP_PREFS_NAME = "com.cat_forecast";
	public static final String APP_CACHE_PATH = 
		Environment.getExternalStorageDirectory().getAbsolutePath() + 
		"/Android/data/" + APP_PREFS_NAME + "/cache/";
	
	public static final int MIN_SEARCH_LOCATION_LENGTH = 3;
}
