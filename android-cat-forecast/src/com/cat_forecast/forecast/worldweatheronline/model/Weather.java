package com.cat_forecast.forecast.worldweatheronline.model;

import org.droidparts.annotation.json.Key;
import org.droidparts.model.Model;

public class Weather extends Model {
	
	private static final long serialVersionUID = -6812847544668375186L;

	@Key(name = "date")	
	public String date;
	
	@Key(name = "tempMaxC")
	public String tempMaxC;

	@Key(name = "tempMinC")
	public String tempMinC;
	
	@Key(name = "weatherCode")	
	public String weatherCode;
	
	@Key(name = "weatherIconUrl")
	public ValueItem[] weatherIconUrl;
	
	@Key(name = "weatherDesc")
	public ValueItem[] weatherDesc;
	
	@Key(name = "windspeedKmph")
	public String windspeedKmph;
    
	@Key(name = "winddirection")	
	public String winddirection;

	@Key(name = "winddirDegree")	
	public String winddirDegree;
	
}
