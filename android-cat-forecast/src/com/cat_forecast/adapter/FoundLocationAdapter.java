package com.cat_forecast.adapter;

import java.util.ArrayList;

import org.droidparts.adapter.holder.Text2Holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.cat_forecast.forecast.worldweatheronline.model.LocationItem;


public class FoundLocationAdapter extends ArrayAdapter<LocationItem> {

	private final static int viewResourceId = android.R.layout.simple_list_item_2;
	
	private ArrayList<LocationItem> items;
	
	public FoundLocationAdapter(Context context, ArrayList<LocationItem> items) {
		super(context, viewResourceId, items);
		
		this.items = items;
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public LocationItem getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Text2Holder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from( getContext() ).inflate(viewResourceId, parent, false);

			holder = new Text2Holder(convertView);
			
			convertView.setTag(holder);
		} 
		else
			holder = (Text2Holder) convertView.getTag();

		
		final LocationItem item = getItem(position);
		
		if ( item != null ) {
			holder.text1.setText(item.name);
			holder.text2.setText(item.description);
		}

		return convertView;
	}	
	
}
