package com.cat_forecast.forecast.worldweatheronline;

import java.util.ArrayList;
import java.util.TreeMap;

import org.droidparts.net.http.HTTPException;
import org.droidparts.persist.json.JSONSerializer;
import org.json.JSONException;
import org.json.JSONObject;

import com.cat_forecast.forecast.worldweatheronline.model.Place;
import com.cat_forecast.forecast.worldweatheronline.model.LocationItem;
import com.cat_forecast.forecast.worldweatheronline.model.Search;
import com.cat_forecast.forecast.worldweatheronline.model.SearchResult;

import android.content.Context;

public class LocationSearch extends WorldWeatherOnline {

	public static final String FREE_API_URL = "http://api.worldweatheronline.com/free/v1/search.ashx";
	public static final String PREMIUM_API_URL = "http://api.worldweatheronline.com/free/v1/search.ashx";
	
	private SearchResult search;
	
	public LocationSearch(Context ctx) {
		super(ctx);
		
		apiUrl = FREE_API_URL;
		
		params = new TreeMap<String, String>();
		
		params.put("popular", "yes");
		params.put("format", "json");
	}
	
	public SearchResult getLocationSearchData(String text) throws JSONException, HTTPException {
		params.put("q", text);
		
		search = parseResult( callAPI() ); 
		
		return search;
	}

	public SearchResult parseResult(JSONObject responce) throws JSONException {
		JSONSerializer<Search> serializer = new JSONSerializer<Search>( Search.class, ctx );

		Search api = serializer.deserialize(responce);
		
		return ( api == null ) ? 
				null : 
				( api.search == null ) ?
						new SearchResult() :
						api.search; 
	}

	public ArrayList<LocationItem> getLocations() {
		ArrayList<LocationItem> items = new ArrayList<LocationItem>();

		if ( search != null )
			for (Place location : search.result)
				items.add( new LocationItem(location) );
		
		return items;
	}
	
}
