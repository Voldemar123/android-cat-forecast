package com.cat_forecast.forecast.worldweatheronline.model;

import org.droidparts.annotation.json.Key;
import org.droidparts.model.Entity;

public class ValueItem extends Entity {

	private static final long serialVersionUID = 6020653261625398121L;

	@Key(name = "value")
	public String value;

}
