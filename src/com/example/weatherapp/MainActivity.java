package com.example.weatherapp;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import weatherApp.WeatherApp;
import model.BitmapIcon;
import model.WeatherInfo;

import android.location.GpsStatus.NmeaListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ListView listView1;
	private ProgressDialog mProgressDialog;
	private ArrayList<WeatherInfo> mWeatherList;
	private String[] locations = { "France", "Egypt", "London","Canada","China","Germany","Ghana","India","Italy","Korea" };
	private WeatherApp mWeatherWorld;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mWeatherList = new ArrayList<WeatherInfo>();
		mWeatherWorld = new WeatherApp();
		listView1 = (ListView) findViewById(R.id.listView1);
		showProgressDialog();
		loadWeather(locations);
	}

	private void loadWeather(final String[] locations) {
		mWeatherList.clear();
		new Thread() {
			@Override
			public void run() {
				int what = 0;

				try {

					mWeatherList = mWeatherWorld.getCurrentClimate(locations);

				} catch (Exception e) {
					what = 1;
					e.printStackTrace();
				}

				weatherHandler.sendMessage(weatherHandler.obtainMessage(what));
			}
		}.start();
	}

	private Handler weatherHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 0) {
				if (mWeatherList.size() == 0) {
					Toast.makeText(MainActivity.this, "Weather World Error !!",
							Toast.LENGTH_SHORT).show();
					return;
				}

				hideProgressDialog();

				WeatherAdapter adapter = new WeatherAdapter(MainActivity.this,
						R.layout.listview_item_row, mWeatherList);
				listView1.setAdapter(adapter);
				listView1.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int postion, long arg3) {
						// TODO Auto-generated method stub
						
						Intent forecastActivity=new Intent(MainActivity.this,ForecastActivity.class);
						forecastActivity.putExtra("counteryName", mWeatherList.get(postion).getCityName());
						forecastActivity.putExtra("minTemp", mWeatherList.get(postion).getTempMinC());
						forecastActivity.putExtra("maxTemp", mWeatherList.get(postion).getTempMaxC());
						forecastActivity.putExtra("TempIcon", BitMapToString(mWeatherList.get(postion).getWeatherIconImag()));
						startActivity(forecastActivity);
						
					}
					
				});
			} else {
				Toast.makeText(MainActivity.this, "Failed to load Weather",
						Toast.LENGTH_SHORT).show();
				hideProgressDialog();

			}
		}
	};
	private String BitMapToString(Bitmap bitmap){
	     ByteArrayOutputStream baos=new  ByteArrayOutputStream();
	     bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
	     byte [] b=baos.toByteArray();
	     String temp=Base64.encodeToString(b, Base64.DEFAULT);
	     return temp;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		hideProgressDialog();
		mProgressDialog = null;
		super.onDestroy();
	}

	private void showProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.cancel();
		}
		mProgressDialog = new ProgressDialog(MainActivity.this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("Loading.....");

		mProgressDialog.show();
	}

	private void hideProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.cancel();
		}
	}

}
