package com.cat_forecast.forecast.worldweatheronline.model;

import org.droidparts.annotation.json.Key;
import org.droidparts.model.Entity;

public class SearchResult extends Entity {

	private static final long serialVersionUID = 5605256375980120944L;

	@Key(name = "result")
	public Place[] result;
}
