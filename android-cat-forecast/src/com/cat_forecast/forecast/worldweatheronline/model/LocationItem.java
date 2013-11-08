package com.cat_forecast.forecast.worldweatheronline.model;

public class LocationItem {

	public String name;
	public String description;
	
	public LocationItem(Place item) {
		this.name = item.areaName[0].value;
		
		StringBuilder sb = new StringBuilder();
		sb.append(item.country[0].value);
		sb.append(", ");
		sb.append(item.region[0].value);
		
		this.description = sb.toString();
	}
}
