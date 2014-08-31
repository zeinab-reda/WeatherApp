package com.example.weatherapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import weatherApp.WeatherApp;

import model.WeatherInfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;


public class ForecastActivity extends Activity {
	private TextView forecastDateView;
	private TextView minTempView;
	private TextView maxTempView;
	private ImageView weatherStateView;
	private TextView counteryNameView;
	private ProgressDialog mProgressDialog;
	private ArrayList<String>forecastDateList;
	private Button forecastBtn;
	private String selectedForecastDate;
	private WeatherInfo forecastWeatherInfo;
	private WeatherApp mWeatherWorld;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forecast);
		counteryNameView = (TextView) findViewById(R.id.counteryName);

		forecastDateView = (TextView) findViewById(R.id.date);
		minTempView = (TextView) findViewById(R.id.minForecast);
		maxTempView = (TextView) findViewById(R.id.maxForecast);
		weatherStateView = (ImageView) findViewById(R.id.forecastWeatherIcon);
        Spinner comingDateView = (Spinner)findViewById(R.id.forecastDate);

		forecastBtn=(Button)findViewById(R.id.forecast_btn);

        forecastDateList=new ArrayList<String>();
        forecastWeatherInfo=new WeatherInfo();
        mWeatherWorld=new WeatherApp();
        
        String  counteryName = getIntent().getStringExtra("counteryName");
        Toast.makeText(getApplicationContext(), counteryName, Toast.LENGTH_LONG).show();
		String minTemp = getIntent().getStringExtra("minTemp");
		String maxTemp = getIntent().getStringExtra("maxTemp");
		Bitmap weatherIcon = StringToBitMap(getIntent().getStringExtra(
				"TempIcon"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDateandTime = sdf.format(new Date());
		counteryNameView.setText(counteryName);

		setView(currentDateandTime, minTemp, maxTemp, weatherIcon);
	
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

        for(int i=1;i<6;i++){      
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, i);
        String output = sdf1.format(cal.getTime());
        
        forecastDateList.add(output);
        }
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(ForecastActivity.this,
                android.R.layout.simple_spinner_item,forecastDateList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comingDateView.setAdapter(adapter);
        comingDateView.setOnItemSelectedListener(new OnItemSelectedListener() {
        	
        	
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                int position, long arg3) {
                            // On selecting a spinner item
                    String label = parent.getItemAtPosition(position).toString();
                        // Showing selected spinner item
                    Toast.makeText(parent.getContext(), "You selected: " + label,Toast.LENGTH_LONG).show();
                    selectedForecastDate=parent.getItemAtPosition(position).toString();
                        }
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
        
        forecastBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadForecastWeather(selectedForecastDate);
			}
		});
	}

	private void setView(String Date, String minTemp, String maxTemp,
			Bitmap weatherIcon) {
		forecastDateView.setText("Date :"+Date);
		minTempView.setText("Minimum :" + minTemp + "\u2103");
		maxTempView.setText("Maxmum :" + maxTemp + "\u2103");
		Drawable drawable = new BitmapDrawable(getApplicationContext()
				.getResources(), weatherIcon);
		weatherStateView.setBackground(drawable);

	}

	
	private void loadForecastWeather(final String Date) {
		showProgressDialog();
		new Thread() {
			@Override
			public void run() {
				int what = 0;

				try {
						
					 forecastWeatherInfo= mWeatherWorld.getFWeatherDate(Date, counteryNameView.getText().toString());

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

				setView(selectedForecastDate, forecastWeatherInfo.getTempMinC(), forecastWeatherInfo.getTempMaxC(), forecastWeatherInfo.getWeatherIconImag());
				hideProgressDialog();
			} else {
				Toast.makeText(ForecastActivity.this, "Failed to load Weather",
						Toast.LENGTH_SHORT).show();
				hideProgressDialog();
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_forecast, menu);
		return true;
	}

	private Bitmap StringToBitMap(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
					encodeByte.length);
			return bitmap;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
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
		mProgressDialog = new ProgressDialog(ForecastActivity.this);
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
