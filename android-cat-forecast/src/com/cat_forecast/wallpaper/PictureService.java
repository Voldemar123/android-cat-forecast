package com.cat_forecast.wallpaper;

import org.droidparts.util.L;

import com.cat_forecast.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class PictureService extends WallpaperService implements OnSharedPreferenceChangeListener {
	
	private SharedPreferences prefs;
	private PictureEngine mPictureEngine;
	
	
	@Override
	public Engine onCreateEngine() {
		L.d("onCreateEngine");
		
		prefs = PictureService.this.getSharedPreferences(Constants.APP_PREFS_NAME, 0);
        prefs.registerOnSharedPreferenceChangeListener(this);
        
        mPictureEngine = new PictureEngine();
        
		return mPictureEngine;
	}

	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		L.d("onSharedPreferenceChanged");
		mPictureEngine.painting.needRedraw();
	}

	@Override
	public void onCreate() {
		L.d("onCreate");
		
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		L.d("onDestroy");
		
		super.onDestroy();
	}

	
	
	
	
	public class PictureEngine extends Engine implements SensorEventListener {

		private BackgroundPainting painting;
		private long lastUpdate;

		private SensorManager mSensorManager;
	    private Sensor mAccelerometer, mMagnetometer;
	    
	    private float[] mLastAccelerometer = new float[3];
	    private float[] mLastMagnetometer = new float[3];
	    private boolean mLastAccelerometerSet = false;
	    private boolean mLastMagnetometerSet = false;

	    private float[] mR = new float[9];
	    private float[] mOrientation = new float[3];
	    
	    private float[] mGravity;
	    private float[] mGeomagnetic;
	    
	    
		public PictureEngine() {
			L.d("PictureEngine");

		    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		    
		    lastUpdate = System.currentTimeMillis();
			
			painting = new BackgroundPainting(
					getSurfaceHolder(),
					getApplicationContext() );
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			L.d("onCreate");
			
			super.onCreate(surfaceHolder);
			setTouchEventsEnabled(true);
		}

		@Override
		public void onDestroy() {
			L.d("onDestroy");
			
			super.onDestroy();
			painting.stopPainting();
		}

		/*
		 * time smoothing constant for low-pass filter
		 * 0 ≤ alpha ≤ 1 ; a smaller value basically means more smoothing
		 * See: http://en.wikipedia.org/wiki/Low-pass_filter#Discrete-time_realization
		 */
		static final float ALPHA = 0.25f;
		 
		/**
		 * @see http://en.wikipedia.org/wiki/Low-pass_filter#Algorithmic_implementation
		 * @see http://developer.android.com/reference/android/hardware/SensorEvent.html#values
		 */
		protected float[] lowPass( float[] input, float[] output ) {
		    if ( output == null ) return input;
		     
		    for ( int i=0; i<input.length; i++ ) {
		        output[i] = output[i] + ALPHA * (input[i] - output[i]);
		    }
		    return output;
		}
		
	  @Override
	  public void onSensorChanged(SensorEvent event) {
			  
		  	if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
		  		mGravity = lowPass(event.values.clone(), mGravity);

		    else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
		    	mGeomagnetic = lowPass(event.values.clone(), mGeomagnetic);

		    if (mGravity != null && mGeomagnetic != null) {
		    	
	            float R[] = new float[9];
	            float I[] = new float[9];
	            
	            SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);

	              float orientation[] = new float[3];
	              SensorManager.getOrientation(R, orientation);
	              
//			        double azimuth = Math.toDegrees( orientation[0] );
	              double pitch = Math.toDegrees( orientation[1] );
	              double roll = Math.toDegrees( orientation[2] );
			        
//			        L.d( String.format("------Orientation3: %f, %f, %f", azimuth, pitch, roll));
	              
	  			painting.updateSensorOffsets(roll, pitch);
	  			painting.doDraw();
		    }		  
	        
		  }

	  @Override
	  public void onAccuracyChanged(Sensor sensor, int accuracy) {
	  }
		
		@Override
		public void onVisibilityChanged(boolean visible) {
			L.d("onVisibilityChanged");
			
			if (visible) {
				mLastAccelerometerSet = false;
		        mLastMagnetometerSet = false;

		        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
				
				painting.resumePainting();
			}
			else {
			    mSensorManager.unregisterListener(this);
				
				painting.pausePainting();
			}
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			L.d("onSurfaceChanged");
			
			super.onSurfaceChanged(holder, format, width, height);
			painting.setSurfaceSize(width, height);
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			L.d("onSurfaceCreated");
			
			super.onSurfaceCreated(holder);
			painting.startPainting();
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			L.d("onSurfaceDestroyed");
			
			super.onSurfaceDestroyed(holder);
			painting.stopPainting();
		}

		@Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, 
        		int xPixels, int yPixels) {
			L.d("onOffsetsChanged");
			
			painting.updateWallpaperOffsets(xPixels, yPixels);
			painting.doDraw();
        }
		
		@Override
		public void onTouchEvent(MotionEvent event) {
			super.onTouchEvent(event);
		}
	}
}