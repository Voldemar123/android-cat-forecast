package com.cat_forecast.forecast.worldweatheronline.model;

import org.droidparts.annotation.json.Key;
import org.droidparts.model.Entity;

public class Place extends Entity {

	private static final long serialVersionUID = 6220619881922302670L;
	
	@Key(name = "areaName")
	public ValueItem[] areaName;

	@Key(name = "country")
	public ValueItem[] country;

	@Key(name = "latitude")
	public String latitude;

	@Key(name = "longitude")
	public String longitude;

	@Key(name = "region")
	public ValueItem[] region;

	@Key(name = "weatherUrl")
	public ValueItem[] weatherUrl;

}
