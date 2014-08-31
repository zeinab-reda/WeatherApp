package weatherApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import model.WeatherInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import Conts.WeatherConts;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class WeatherApp {

	private static final String API_URL = "http://api.worldweatheronline.com/free/v1/weather.ashx";


	public ArrayList<WeatherInfo> getCurrentClimate(String[] locations)
			throws Exception {
		ArrayList<WeatherInfo> weatherList = new ArrayList<WeatherInfo>();

		URL url;
		String mURL = API_URL + "?key=" + WeatherConts.WEATHER_KEY + "&q="
				+ locations[0] + "&cc=no&format=json";
		Log.v("URL", mURL);
		for (int i = 0; i < locations.length; i++) {
			try {
				url = new URL(API_URL + "?key=" + WeatherConts.WEATHER_KEY
						+ "&q=" + locations[i] + "&cc=no&format=json");
				Log.d("", "Opening URL " + url.toString());

				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();

				urlConnection.setRequestMethod("GET");
				urlConnection.setDoInput(true);

				urlConnection.connect();

				String response = streamToString(urlConnection.getInputStream());
				JSONObject jsonObj = (JSONObject) new JSONTokener(response)
						.nextValue();
				JSONObject data = jsonObj.getJSONObject("data");
				weatherList.add(ParsingJson(data).get(0));
			} catch (Exception ex) {
				throw ex;
			}
		}

		return weatherList;
	}

	private ArrayList<WeatherInfo> ParsingJson(JSONObject data){
		ArrayList<WeatherInfo>mWeatherList=new ArrayList<WeatherInfo>();
		WeatherInfo weatherModel = new WeatherInfo();
		
		JSONArray weather;
		try {
			weather = data.getJSONArray("weather");
		weatherModel.setCityName(data.getJSONArray("request")
				.getJSONObject(0).getString("query"));
		for (int i = 0; i < weather.length(); i++) {
	
		weatherModel.setTempMinC(weather.getJSONObject(i).getString(
				"tempMinC"));
		weatherModel.setTempMaxC(weather.getJSONObject(i).getString(
				"tempMaxC"));
		weatherModel.setTempMinF(weather.getJSONObject(i).getString(
				"tempMinF"));
		weatherModel.setTempMaxF(weather.getJSONObject(i).getString(
				"tempMaxF"));
		weatherModel.setWeatherIconUrl(weather.getJSONObject(i)
				.getJSONArray("weatherIconUrl").getJSONObject(i)
				.getString("value"));
		weatherModel.setWeatherIconImag(LoadWeatherImage(weather.getJSONObject(i).getJSONArray("weatherIconUrl").getJSONObject(0).getString("value")));
		mWeatherList.add(weatherModel);
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mWeatherList;
	}

	
	public WeatherInfo getFWeatherDate(String mDate ,String mCountry)
			throws Exception {
		WeatherInfo weatherForecatObject = new WeatherInfo();

		URL url;
			try {
				
				
				
				url = new URL(API_URL + "?key=" + WeatherConts.WEATHER_KEY + "&q="
						+ mCountry + "&num_of_days=3"+"&&date="+mDate+"&format=json");
				Log.d("", "Opening URL " + url.toString());

				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();

				urlConnection.setRequestMethod("GET");
				urlConnection.setDoInput(true);

				urlConnection.connect();

				String response = streamToString(urlConnection.getInputStream());
				JSONObject jsonObj = (JSONObject) new JSONTokener(response)
						.nextValue();
				JSONObject data = jsonObj.getJSONObject("data");
				weatherForecatObject=ParsingJson(data).get(0);
			} catch (Exception ex) {
				throw ex;
			}
		return weatherForecatObject;
	}
	
	
	public ArrayList<WeatherInfo> getNextForecastClimate(String location)
			throws Exception {
		ArrayList<WeatherInfo> weatherForecatList = new ArrayList<WeatherInfo>();

		URL url;
			try {
				
				url = new URL(API_URL + "?key=" + WeatherConts.WEATHER_KEY + "&q="
						+ location + "&num_of_days=3&format=json");
				Log.d("", "Opening URL " + url.toString());

				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();

				urlConnection.setRequestMethod("GET");
				urlConnection.setDoInput(true);

				urlConnection.connect();

				String response = streamToString(urlConnection.getInputStream());
				JSONObject jsonObj = (JSONObject) new JSONTokener(response)
						.nextValue();
				JSONObject data = jsonObj.getJSONObject("data");
				weatherForecatList=ParsingJson(data);
			} catch (Exception ex) {
				throw ex;
			}
		return weatherForecatList;
	}

	private  Bitmap LoadWeatherImage(String imgURL) throws Exception {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream((InputStream) new URL(imgURL)
					.getContent());

		} catch (Exception ex) {
			throw ex;
		}
		return bitmap;
	}

	private String streamToString(InputStream is) throws IOException {
		String str = "";

		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				reader.close();
			} finally {
				is.close();
			}

			str = sb.toString();
		}

		return str;
	}
}
