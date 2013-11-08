package com.cat_forecast.test;

import java.lang.reflect.Method;

import org.droidparts.util.ResourceUtils;
import org.json.JSONObject;

import com.cat_forecast.forecast.worldweatheronline.LocationSearch;
import com.cat_forecast.forecast.worldweatheronline.model.Place;
import com.cat_forecast.forecast.worldweatheronline.model.SearchResult;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.ServiceTestCase;

public class LocationSearchTestCase extends AndroidTestCase {

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
		String str = ResourceUtils.readRawResource( getTestContext(), R.raw.search_empty_api1 );

		assertNotNull(str);
		
		JSONObject result = new JSONObject(str);
		
		assertNotNull(result);
		
		LocationSearch ls = new LocationSearch( getContext() );
		SearchResult search = ls.parseResult(result);
		
		assertNull(search);
		
//		SearchResult search2 = ls.getLocationSearchData("KievXXX");
//		
//		assertNull(search2);
	}

	public void testLatitudeLongitudeJSONResult() throws Exception {
		String str = ResourceUtils.readRawResource( getTestContext(), R.raw.search_api1 );

		JSONObject result = new JSONObject(str);
		
		assertNotNull(result);
		
		LocationSearch ls = new LocationSearch( getContext() );
		SearchResult search = ls.parseResult(result);
		
		assertNotNull(search);
		assertNotNull(search.result);
		assertEquals(10, search.result.length);
		
		Place item = search.result[0];
		
		assertNotNull(item);
		assertEquals("50.433", item.latitude);
		assertEquals("30.517", item.longitude);
		assertEquals(1, item.areaName.length);
		assertEquals("Kiyev", item.areaName[0].value);
		assertEquals(1, item.country.length);
		assertEquals("Ukraine", item.country[0].value);
		
		
//		SearchResult search2 = ls.getLocationSearchData("Kiev");
//		
//		assertNotNull(search2);
//		assertNotNull(search2.result);
	}

	public void testCityJSONResult() throws Exception {
		String str = ResourceUtils.readRawResource( getTestContext(), R.raw.search2_api1 );

		JSONObject result = new JSONObject(str);
		
		assertNotNull(result);
		
		LocationSearch ls = new LocationSearch( getContext() );
		SearchResult search = ls.parseResult(result);
		
		assertNotNull(search);
		assertNotNull(search.result);
		assertEquals(2, search.result.length);
		
		Place item = search.result[0];
		
		assertNotNull(item);
		assertEquals("50.433", item.latitude);
		assertEquals("30.517", item.longitude);
		assertEquals(1, item.areaName.length);
		assertEquals("Kiev", item.areaName[0].value);
		assertEquals(1, item.country.length);
		assertEquals("Ukraine", item.country[0].value);
		
		
//		SearchResult search2 = ls.getLocationSearchData("50.433,30.517");
//		
//		assertNotNull(search2);
//		assertNotNull(search2.result);
	}
	
}
