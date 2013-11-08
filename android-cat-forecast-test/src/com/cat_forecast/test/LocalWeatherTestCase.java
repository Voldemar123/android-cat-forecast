package com.cat_forecast.test;

import java.lang.reflect.Method;

import org.droidparts.util.ResourceUtils;
import org.json.JSONObject;

import com.cat_forecast.forecast.worldweatheronline.LocalWeather;
import com.cat_forecast.forecast.worldweatheronline.model.LocationData;
import com.cat_forecast.forecast.worldweatheronline.model.Weather;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.ServiceTestCase;

public class LocalWeatherTestCase extends AndroidTestCase {

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}
	
	private Context getTestContext()
	{
	    try
	    {
	        Method getTestContext = ServiceTestCase.class.getMethod("getTestContext");
	        return (Context) getTestContext.invoke(this);
	    }
	    catch (final Exception exception)
	    {
	        exception.printStackTrace();
	        return null;
	    }
	}
	
	public void testEmptyResult() throws Exception {
		String str = ResourceUtils.readStringResource( getTestContext(), R.raw.local_empty_api1 );

		assertNotNull(str);
		
		JSONObject result = new JSONObject(str);
		
		assertNotNull(result);
		
		LocalWeather lw = new LocalWeather( getContext() );
		LocationData data = lw.parseResult(result);
		
		assertNotNull(data);

		assertNotNull(data.error);
		assertEquals(1, data.error.length);
		
		assertNull(data.currentCondition);
		assertNull(data.request);
		assertNull(data.weather);
	}

	public void testLatitudeLongitudeJSONResult() throws Exception {
		String str = ResourceUtils.readStringResource( getTestContext(), R.raw.local2_api1 );

		assertNotNull(str);
		
		JSONObject result = new JSONObject(str);
		
		assertNotNull(result);
		
		LocalWeather lw = new LocalWeather( getContext() );
		LocationData data = lw.parseResult(result);
		
		assertNotNull(data);
		assertNull(data.error);
		
		assertNotNull(data.currentCondition);
		assertEquals(1, data.currentCondition.length);
		
		assertNotNull(data.request);
		assertEquals(1, data.request.length);
		assertEquals("LatLon", data.request[0].type);
		
		assertNotNull(data.weather);
		assertEquals(5, data.weather.length);
		
		Weather item = data.weather[0];
		
		assertNotNull(item);
		assertEquals(1, item.weatherDesc.length);
		assertEquals(1, item.weatherIconUrl.length);
	}

	public void testCityJSONResult() throws Exception {
		String str = ResourceUtils.readStringResource( getTestContext(), R.raw.local_api1 );

		assertNotNull(str);
		
		JSONObject result = new JSONObject(str);
		
		assertNotNull(result);
		
		LocalWeather lw = new LocalWeather( getContext() );
		LocationData data = lw.parseResult(result);
		
		assertNotNull(data);
		assertNull(data.error);
		
		assertNotNull(data.currentCondition);
		assertEquals(1, data.currentCondition.length);
		
		assertNotNull(data.request);
		assertEquals(1, data.request.length);
		assertEquals("City", data.request[0].type);
		
		assertNotNull(data.weather);
		assertEquals(5, data.weather.length);
		
		Weather item = data.weather[0];
		
		assertNotNull(item);
		assertEquals(1, item.weatherDesc.length);
		assertEquals(1, item.weatherIconUrl.length);
	}
	
}
