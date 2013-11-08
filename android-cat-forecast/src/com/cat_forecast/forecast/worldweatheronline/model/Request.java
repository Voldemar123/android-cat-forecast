package com.cat_forecast.forecast.worldweatheronline.model;

import org.droidparts.annotation.json.Key;
import org.droidparts.model.Entity;

public class Request extends Entity {

	private static final long serialVersionUID = 2561559038897498965L;

	@Key(name = "query")
	public String query;

	@Key(name = "type")
	public String type;

}
