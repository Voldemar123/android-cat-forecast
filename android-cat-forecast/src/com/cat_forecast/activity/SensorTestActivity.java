package com.cat_forecast.activity;

import com.cat_forecast.R;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SensorTestActivity extends Activity implements SensorEventListener {
	  private long lastUpdate;

	  private TextView mazimuth_angle, mpitch_angle;
	  private SensorManager mSensorManager;
      private Sensor mAccelerometer;


	  @Override
	  public void onCreate(Bundle savedInstanceState) {

	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    mazimuth_angle = (TextView)findViewById(R.id.azimuth_angle);
	    mpitch_angle = (TextView)findViewById(R.id.pitch_angle);

	    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    
	    lastUpdate = System.currentTimeMillis();
	  }

	  @Override
	  public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
        	return;
		  
 	    long actualTime = System.currentTimeMillis();
 	    if (actualTime - lastUpdate < 200)
 	    	return;

 	    lastUpdate = actualTime;
          
 	    Float azimuth = event.values[0];
		Float pitch = event.values[1];

		    
		    mazimuth_angle.setText(azimuth.toString());   
		    mpitch_angle.setText(pitch.toString());   
		  
		    
	  }

	  @Override
	  public void onAccuracyChanged(Sensor sensor, int accuracy) {
	  }

	  @Override
	  protected void onResume() {
	    super.onResume();

	    mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

	  }

	  @Override
	  protected void onPause() {
	    super.onPause();

	    mSensorManager.unregisterListener(this);

	  }
	} 