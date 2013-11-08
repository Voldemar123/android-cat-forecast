package com.cat_forecast.forecast.worldweatheronline.model;

import org.droidparts.annotation.json.Key;
import org.droidparts.model.Model;

public class CurrentCondition extends Model {
	
	private static final long serialVersionUID = 8585880187211585161L;

	@Key(name = "observation_time")	
	public String observation_time;
	
	@Key(name = "temp_C")
	public String temp_C;
	
	@Key(name = "weatherCode")	
	public String weatherCode;
	
	@Key(name = "weatherIconUrl")
	public ValueItem[] weatherIconUrl;
	
	@Key(name = "weatherDesc")
	public ValueItem[] weatherDesc;
	
	@Key(name = "windspeedKmph")
	public String windspeedKmph;
    
	@Key(name = "winddirDegree")	
	public String winddirDegree;
	
	@Key(name = "humidity")
	public String humidity;
    
	@Key(name = "visibility")
	public String visibility;

	@Key(name = "pressure")
	public String pressure;
    
	@Key(name = "cloudcover")
	public String cloudcover;

}
