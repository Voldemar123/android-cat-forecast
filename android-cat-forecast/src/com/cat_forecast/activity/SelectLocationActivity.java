package com.cat_forecast.activity;

import java.util.ArrayList;

import org.droidparts.activity.Activity;
import org.droidparts.annotation.inject.InjectDependency;
import org.droidparts.annotation.inject.InjectView;
import org.droidparts.concurrent.task.AsyncTaskResultListener;
import org.droidparts.concurrent.task.SimpleAsyncTask;
import org.droidparts.net.image.ImageFetcher;
import org.droidparts.util.L;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cat_forecast.Constants;
import com.cat_forecast.R;
import com.cat_forecast.adapter.FoundLocationAdapter;
import com.cat_forecast.forecast.worldweatheronline.LocalWeather;
import com.cat_forecast.forecast.worldweatheronline.LocationSearch;
import com.cat_forecast.forecast.worldweatheronline.model.CurrentCondition;
import com.cat_forecast.forecast.worldweatheronline.model.LocationData;
import com.cat_forecast.forecast.worldweatheronline.model.LocationItem;
import com.cat_forecast.forecast.worldweatheronline.model.Place;
import com.cat_forecast.forecast.worldweatheronline.model.SearchResult;
import com.cat_forecast.service.GPSTracker;

public class SelectLocationActivity extends Activity implements OnClickListener {

	@InjectDependency
	private ImageFetcher imageFetcher;
	
	@InjectView(id = R.id.searchLocation)
	private EditText mSearchLocation;
	
	@InjectView(id = R.id.searchLocationButton, click = true)
	private Button mSearchLocationButton;
		
	@InjectView(id = R.id.locationsList)
	private ListView mLocationsList;

	@InjectView(id = R.id.locationsEmpty)
	private View mLocationsEmpty;
	
	@InjectView(id = R.id.currentLocationWeatherIcon)
	private ImageView mCurrentLocationWeatherIcon;

	@InjectView(id = R.id.currentLocationTemp)
	private TextView mCurrentLocationTemp;

//	@InjectView(id = R.id.currentLocationWeatherDesc)
//	private TextView mCurrentLocationWeatherDesc;
	
	private static Handler mProgressBarHandler;

	
	
	@Override
	public void onPreInject() {
//		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	    setContentView(R.layout.activity_select_location);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mProgressBarHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Boolean visibility = (Boolean) msg.obj;
//				setProgressBarIndeterminateVisibility(visibility);
				
//				mSearchLocationButton.setEnabled( !visibility );
			}
		};
		
// get current location use GPS/Network		
		GPSTracker GPSTracker = new GPSTracker(this);
		
// can't get location, GPS or Network is not enabled
// Ask user to enable GPS/network in settings
		if( ! GPSTracker.canGetLocation() )
        	GPSTracker.showSettingsAlert();

// try to found initial location
		WeatherLocationResultListener locationResultListener = new WeatherLocationResultListener(this);
		
		SearchLocationWeatherTask task = new SearchLocationWeatherTask( this, locationResultListener );

// last known location			
		task.setFoundLocation( GPSTracker.getPosition() );
		task.execute();		
		
		mSearchLocation.setText("Kievr");

		mSearchLocation.addTextChangedListener(new TextWatcher() {

	          public void afterTextChanged(Editable s) {
// try to found input location
	        	  if ( s.length() >= Constants.MIN_SEARCH_LOCATION_LENGTH ) 
	        		  foundInputLocation();
	          }

	          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	          public void onTextChanged(CharSequence s, int start, int before, int count) {}
	       });
	}

// try to found user input location
	private void foundInputLocation() {
		WeatherLocationResultListener locationResultListener = new WeatherLocationResultListener(this);

		SearchLocationWeatherTask task = new SearchLocationWeatherTask( this, locationResultListener );
		
// input location			
		task.setFoundLocation( mSearchLocation.getText().toString() );
		task.execute();		
	}
	
	@Override
	public void onClick(View v) {

		switch ( v.getId() ) {
			case R.id.searchLocationButton:
				if ( mSearchLocation.getText().toString().length() >= Constants.MIN_SEARCH_LOCATION_LENGTH )
					foundInputLocation();
				
			break;
		}
	}


// search location weather information
	private class SearchLocationWeatherTask extends SimpleAsyncTask<SearchResult> {

		private String foundLocation;
		
		public SearchLocationWeatherTask(
				Context ctx, 
				AsyncTaskResultListener<SearchResult> resultListener) {
			
			super(ctx, resultListener);
		}

		public void setFoundLocation(String location) {
			foundLocation = location;			
		}

		@Override
		protected SearchResult onExecute() throws Exception {
//			Message progressBarMessage = new Message();
//			progressBarMessage.obj = true;
//			mProgressBarHandler.sendMessage(progressBarMessage);			

// output weather info for found location
			WeatherInfoListener weatherInfoListener = new WeatherInfoListener();
			
			FoundLocationWeatherTask task = new FoundLocationWeatherTask( getContext(), weatherInfoListener );
			
			task.setFoundLocation(foundLocation);
			task.execute();
			
// search from forecast provider by selected coordinates 			
			LocationSearch ls = new LocationSearch( getContext() );
			return ls.getLocationSearchData(foundLocation);
		}
	}	
	
// update found locations list
	private class WeatherLocationResultListener implements AsyncTaskResultListener<SearchResult> {
		private final Context ctx;
		
		public WeatherLocationResultListener(Context ctx) {
			super();

			this.ctx = ctx.getApplicationContext();
		}

		@Override
		public void onAsyncTaskFailure(Exception e) {
			L.e( e.getMessage() );			
		}

		@Override
		public void onAsyncTaskSuccess(SearchResult search) {
			L.d("update weather location");

			ArrayList<LocationItem> foundLocations = new ArrayList<LocationItem>();
			if ( search.result != null)
				for ( Place location : search.result ) {
					LocationItem locationItem = new LocationItem(location);
					foundLocations.add(locationItem);
				}

// update found locations list
			FoundLocationAdapter adapter = new FoundLocationAdapter( ctx, foundLocations );
			
			mLocationsList.setAdapter(adapter);
			mLocationsList.setEmptyView(mLocationsEmpty);
			
//			Message progressBarMessage = new Message();
//			progressBarMessage.obj = false;
//			mProgressBarHandler.sendMessage(progressBarMessage);			
		}
		
	}
	
	private class FoundLocationWeatherTask extends SimpleAsyncTask<LocationData> {
		private String foundLocation;

		public FoundLocationWeatherTask(Context ctx, AsyncTaskResultListener<LocationData> resultListener) {
			super(ctx, resultListener);
		}

		public void setFoundLocation(String location) {
			foundLocation = location;			
		}
		
		@Override
		protected LocationData onExecute() throws Exception {
			L.d("show weather in location");

// get weather info from forecast provider by place location 			
			LocalWeather lw = new LocalWeather( getContext() );

			return lw.getLocalWeatherData(foundLocation);
		}
	}	
	
// output weather info for found location		
	private class WeatherInfoListener implements AsyncTaskResultListener<LocationData> {

		@Override
		public void onAsyncTaskFailure(Exception e) {
			L.e( e.getMessage() );			
		}

		@Override
		public void onAsyncTaskSuccess(LocationData locationData) {
			L.d("update weather info");

			if ( locationData.error != null ) {
				L.w( locationData.error[0].msg );	
				return;
			}

// output weather info for found location		
			CurrentCondition currentCondition = locationData.currentCondition[0];
			L.i(currentCondition);
			
			imageFetcher.attachImage(
					currentCondition.weatherIconUrl[0].value, 
					mCurrentLocationWeatherIcon );

			mCurrentLocationTemp.setText( 
					String.format( 
							getResources().getString(R.string.weather_temp_c), 
							currentCondition.temp_C) );
		}
		
	}
	

}