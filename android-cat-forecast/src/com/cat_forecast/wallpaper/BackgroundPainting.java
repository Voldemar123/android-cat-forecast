package com.cat_forecast.wallpaper;

import org.droidparts.util.L;

import com.cat_forecast.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

public class BackgroundPainting {

	private SurfaceHolder surfaceHolder;
	private Context context;

	private boolean wait, redraw;
	private int width, height, xOffset, yOffset;
	private long base_dx, base_dy, dx, dy;
	private long prev_roll, prev_pitch;
	private Bitmap bg;

	public static final int D = 3;
	public static final double boundary_angle = 15;
	
	
	public BackgroundPainting(SurfaceHolder surfaceHolder, Context context) {
		L.d("BackgroundPainting");

		this.surfaceHolder = surfaceHolder;
		this.context = context;

		this.wait = false;
	}

	public void pausePainting() {
		L.d("pausePainting");

		this.wait = true;
	}

	public void resumePainting() {
		L.d("resumePainting");

		this.wait = false;
		doDraw();
	}

	public void startPainting() {
		L.d("startPainting");
		
		reloadBackground();
	}

	public void stopPainting() {
		L.d("stopPainting");
	}

	public void needRedraw() {
		L.d("needRedraw");

		reloadBackground();
		rescalePicture();
		doDraw();
	}

// set up new background image	
	public void reloadBackground() {
		L.d("reloadBackground");

		bg = BitmapFactory.decodeResource(context.getResources(), R.drawable.cat2);
		
		this.redraw = true;
	}
	
// rescale picture for device screen size 	
	private void rescalePicture() {
		L.d("rescalePicture");
		
		float xScale = (float) this.width / bg.getWidth();
        float yScale = (float) this.height / bg.getHeight();
        
        L.d("xScale " + xScale);
        
        float scale = Math.max(xScale, yScale); // selects the larger size to grow the images by
        L.d("scale " + scale);
        
        float scaledWidth = scale * bg.getWidth();
        float scaledHeight = scale * bg.getHeight();

        L.d("scaledWidth " + scaledWidth);
        
        base_dx = dx = Math.round( ( scaledWidth * D ) / 100 );   
        base_dy = dy = Math.round( ( scaledHeight * D ) / 100 );   

        L.d("dx " + dx);
        
        scaledWidth += dx * 2;
        
        L.d("scaledWidth " + scaledWidth);
        
        scaledHeight += dy * 2;
        
        bg = Bitmap.createScaledBitmap(bg, (int)scaledWidth, (int)scaledHeight, true);
        
        this.redraw = true;
	}
	
// draw scaled bitmap with offset	
	public void doDraw() {
//		L.d("doDraw");
		
		if ( this.wait || !this.redraw )
			return;
		
		Canvas canvas = this.surfaceHolder.lockCanvas(null);

		canvas.drawColor(Color.BLACK);
		canvas.drawBitmap(bg, xOffset - dx, yOffset - dy, null);
		
		this.redraw = false;
		
		this.surfaceHolder.unlockCanvasAndPost(canvas);
	}
	
	public void setSurfaceSize(int width, int height) {
		L.d("setSurfaceSize");

		L.d("width " + width + " height " + height);
		
		this.width = width;
		this.height = height;

		if ( this.redraw )
			rescalePicture();
	}

// update picture offset from wallpaper service	
	public void updateWallpaperOffsets(int xPixels, int yPixels) {
		L.d("setSurfaceSize");

		if ( ( xPixels + bg.getWidth() ) >= this.width )
			this.xOffset = xPixels;
		
		if ( ( yPixels + bg.getHeight() ) >= this.height )
			this.yOffset = yPixels;
		
		this.redraw = true;
	}

	public void updateSensorOffsets(double roll, double pitch) {
//		L.d("updateSensorOffsets");

		if ( roll < -boundary_angle || roll > boundary_angle )
			return;

		if ( pitch < -boundary_angle || pitch > boundary_angle )
			return;
		
		long new_dx = Math.round( ( this.base_dx * roll ) / boundary_angle);
		long new_dy = Math.round( ( this.base_dy * pitch ) / boundary_angle);

		if ( this.dx != new_dx && this.dy != new_dy ) {
			this.dx = new_dx;
			this.dy = new_dy;
		
			L.d("this.base_dx " + this.base_dx + " this.dx " + this.dx);
			
			this.redraw = true;
		}
		
	}

}