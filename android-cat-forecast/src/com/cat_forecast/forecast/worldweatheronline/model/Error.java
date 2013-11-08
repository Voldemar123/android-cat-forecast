package com.cat_forecast.forecast.worldweatheronline.model;

import org.droidparts.annotation.json.Key;
import org.droidparts.model.Entity;

public class Error extends Entity {

	private static final long serialVersionUID = -6876251063252608543L;

	@Key(name = "msg")
	public String msg;

}
