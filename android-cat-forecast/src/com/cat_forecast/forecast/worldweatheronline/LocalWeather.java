package com.cat_forecast.forecast.worldweatheronline;

import java.util.TreeMap;

import org.droidparts.net.http.HTTPException;
import org.droidparts.persist.json.JSONSerializer;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.cat_forecast.forecast.worldweatheronline.model.LocationData;
import com.cat_forecast.forecast.worldweatheronline.model.LocationResult;

public class LocalWeather extends WorldWeatherOnline {
	
	public static final String FREE_API_URL = "http://api.worldweatheronline.com/free/v1/weather.ashx";
	public static final String PREMIUM_API_URL = "http://api.worldweatheronline.com/premium/v1/premium-weather-V2.ashx";

	
	public LocalWeather(Context ctx) {
		super(ctx);
		
		apiUrl = FREE_API_URL;
		
		params = new TreeMap<String, String>();
		
		params.put("extra", "isDayTime");
		params.put("num_of_days", "1");
		params.put("fx", "no");
		params.put("format", "json");
	}
	
	public LocationData getLocalWeatherData(String text) throws JSONException, HTTPException {
		params.put("q", text);
		
		return parseResult( callAPI() );
	}

	public LocationData parseResult(JSONObject responce) throws JSONException {
		JSONSerializer<LocationResult> serializer = new JSONSerializer<LocationResult>( LocationResult.class, ctx );

		LocationResult result = serializer.deserialize(responce);
		
		return result.data; 
	}
	
}
