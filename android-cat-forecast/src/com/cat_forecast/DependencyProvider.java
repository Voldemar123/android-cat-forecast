package com.cat_forecast;

import org.droidparts.AbstractDependencyProvider;
import org.droidparts.net.image.ImageFetcher;
import org.droidparts.persist.sql.AbstractDBOpenHelper;

import android.content.Context;

public class DependencyProvider extends AbstractDependencyProvider {

	private ImageFetcher imageFetcher;

	public DependencyProvider(Context ctx) {
		super(ctx);
	}

	@Override
	public AbstractDBOpenHelper getDBOpenHelper() {
		return null;
	}

	public ImageFetcher getImageFetcher(Context ctx) {
		if (imageFetcher == null) {
			imageFetcher = new ImageFetcher(ctx);
			imageFetcher.clearCacheOlderThan(48);
		}
		return imageFetcher;
	}

}
