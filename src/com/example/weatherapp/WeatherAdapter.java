package com.example.weatherapp;

import java.util.ArrayList;

import model.WeatherInfo;

import org.w3c.dom.Text;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherAdapter extends ArrayAdapter<WeatherInfo> {

	Context context;
	int layoutResourceId;
	ArrayList<WeatherInfo> data;

	public WeatherAdapter(Context context, int layoutResourceId,
			ArrayList<WeatherInfo> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		WeatherHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new WeatherHolder();
			holder.weatherIcon = (ImageView) row.findViewById(R.id.weatherIcon);
			holder.cityName = (TextView) row.findViewById(R.id.cityName);
			holder.minTemp = (TextView) row.findViewById(R.id.minTemp);
			holder.maxTemp = (TextView) row.findViewById(R.id.maxTemp);
			row.setTag(holder);
		} else {
			holder = (WeatherHolder) row.getTag();
		}

		WeatherInfo weather = data.get(position);
		holder.cityName.setText(weather.getCityName());
		Drawable drawable = new BitmapDrawable(context.getResources(),
				weather.getWeatherIconImag());
		holder.weatherIcon.setBackground(drawable);
		holder.minTemp.setText("Minimum :" + weather.getTempMinC() + "\u2103");
		holder.maxTemp.setText("Maximum :" + weather.getTempMaxC() + "\u2103");
		return row;
	}

	static class WeatherHolder {
		ImageView weatherIcon;
		TextView cityName;
		TextView minTemp;
		TextView maxTemp;
	}
}