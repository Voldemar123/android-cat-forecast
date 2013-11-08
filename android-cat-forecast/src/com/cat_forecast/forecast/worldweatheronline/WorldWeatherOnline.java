package com.cat_forecast.forecast.worldweatheronline;

import java.util.TreeMap;

import org.droidparts.net.http.HTTPException;
import org.droidparts.net.http.RESTClient2;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;

public class WorldWeatherOnline {

	public static final String FREE_API_KEY = "a7x7qrc47y6m3w5mu696xr8h";

	Context ctx;
	String apiUrl;
	TreeMap<String, String> params;
	RESTClient2 restClient;
	
	
	public WorldWeatherOnline(Context ctx) {
		this.ctx = ctx;	
		
		restClient = new RESTClient2(ctx);		
	}
	
	public JSONObject callAPI() throws HTTPException {
		params.put("key", FREE_API_KEY);
		
		Uri.Builder callUri = Uri.parse(apiUrl).buildUpon();
		
		for (String key : params.keySet())
			callUri.appendQueryParameter( key, params.get(key) );
		
		return restClient.getJSONObject( 
				callUri.build().toString() );		
	}
	

}
