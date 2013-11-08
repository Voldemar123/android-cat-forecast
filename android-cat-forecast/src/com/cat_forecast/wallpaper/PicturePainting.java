package com.cat_forecast.wallpaper;

import com.cat_forecast.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class PicturePainting extends Thread implements Runnable {

	private static final String TAG = "Painting";

	private SurfaceHolder surfaceHolder;
	private Context context;

	/** Состояние потоков */
	private boolean wait;
	private boolean run;

	/** Высота и ширина сцены */
	private int width;
	private int height;

	/** Скоре, достижение :) */
	private int score = 0;

	/** Позиция нажатия на экран */
	private float posX;
	private float posY;

	/** Объект рисовалки наших достижений */
	private Paint mScorePaint;

	/** Фоновый рисунок */
	private Bitmap bg;

	/** Конструктор */
	public PicturePainting(SurfaceHolder surfaceHolder, Context context) {
		Log.d(TAG, "PicturePainting");

		this.surfaceHolder = surfaceHolder;
		this.context = context;

		/** Запускаем поток */
		this.wait = true;

		bg = BitmapFactory.decodeResource(context.getResources(), R.drawable.cat1);

		// стили для вывода счета
		mScorePaint = new Paint();
		mScorePaint.setTextSize(20);
		mScorePaint.setStrokeWidth(1);
		mScorePaint.setStyle(Style.FILL);
		mScorePaint.setColor(Color.WHITE);
	}

	/**
	 * Ставим на паузу анимацию
	 */
	public void pausePainting() {
		Log.d(TAG, "pausePainting");

		this.wait = true;
		synchronized (this) {
			this.notify();
		}
	}

	/**
	 * Запускаем поток когда сняли с паузы
	 */
	public void resumePainting() {
		Log.d(TAG, "resumePainting");

		this.wait = false;
		synchronized (this) {
			this.notify();
		}
	}

	/**
	 * Останавливаем поток
	 */
	public void stopPainting() {
		Log.d(TAG, "stopPainting");

		this.run = false;
		synchronized (this) {
			this.notify();
		}
	}

	/** Рисуем в потоке все наши рисунки */
	public void run() {
		Log.d(TAG, "run");

		this.run = true;
		Canvas c = null;
		while (run) {
			try {
				c = this.surfaceHolder.lockCanvas(null);
				synchronized (this.surfaceHolder) {
					Thread.sleep(50);

					doDraw(c);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				if (c != null) {
					this.surfaceHolder.unlockCanvasAndPost(c);
				}
			}
			// pause if no need to animate
			synchronized (this) {
				if (wait) {
					try {
						wait();
					} catch (Exception e) {
					}
				}
			}
		}
	}

	/**
	 * Растягиваем картинку под размер экрана
	 */
	public void setSurfaceSize(int width, int height) {
		Log.d(TAG, "setSurfaceSize");

		Log.d(TAG, "width " + width);
		Log.d(TAG, "height " + height);
		
		this.width = width;
		this.height = height;
		synchronized (this) {
			this.notify();
			bg = Bitmap.createScaledBitmap(bg, width, height, true);
		}
	}

	/**
	 * Обрабатываем нажатия на экран
	 */
	public boolean doTouchEvent(MotionEvent event) {
		Log.d(TAG, "doTouchEvent");

		posX = event.getX();
		posY = event.getY();
		synchronized (surfaceHolder) {

		}
		return true;
	}

	/**
	 * Рисуем на сцене в потоке
	 */
	private void doDraw(Canvas canvas) {
		Log.d(TAG, "doDraw");

		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(bg, 0, 0, null);

		canvas.drawText("Score: " + score, 50, 70, mScorePaint);
	}

}