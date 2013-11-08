package com.cat_forecast.forecast.worldweatheronline.model;

import org.droidparts.annotation.json.Key;
import org.droidparts.model.Model;

public class LocationData extends Model {

	private static final long serialVersionUID = -4653137148419625101L;

	@Key(name = "error", optional = true)
	public Error[] error;
	
	@Key(name = "current_condition", optional = true)
	public CurrentCondition[] currentCondition;

	@Key(name = "request", optional = true)
	public Request[] request;

	@Key(name = "weather", optional = true)
	public Weather[] weather;
	
}
