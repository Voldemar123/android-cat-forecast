package com.cat_forecast.forecast.worldweatheronline.model;

import org.droidparts.annotation.json.Key;
import org.droidparts.model.Entity;

public class LocationResult extends Entity {

	private static final long serialVersionUID = -4745388021153396971L;

	@Key(name = "data")
	public LocationData data;
}
