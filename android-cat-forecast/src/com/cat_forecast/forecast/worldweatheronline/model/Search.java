package com.cat_forecast.forecast.worldweatheronline.model;

import org.droidparts.annotation.json.Key;
import org.droidparts.model.Model;

public class Search extends Model {

	private static final long serialVersionUID = -7070428293366399223L;
	
	@Key(name = "search_api")
	public SearchResult search;

}
