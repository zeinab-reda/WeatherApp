package model;

import android.graphics.Bitmap;

public class WeatherInfo {

	private String tempMinC;
	private String tempMaxC;
	private String tempMinF;
	private String tempMaxF;
	private String cityName;
	private String weatherIconUrl;
	private Bitmap weatherIconImag;

	public WeatherInfo() {
		super();
	}

	public WeatherInfo(Bitmap icon, String title, String minTemp, String maxTemp) {
		super();
		this.weatherIconImag = icon;
		this.cityName = title;
		this.tempMinC = minTemp;
		this.tempMaxC = maxTemp;
	}

	public Bitmap getWeatherIconImag() {
		return weatherIconImag;
	}

	public void setWeatherIconImag(Bitmap weatherIconImag) {
		this.weatherIconImag = weatherIconImag;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getTempMinF() {
		return tempMinF;
	}

	public void setTempMinF(String tempMinF) {
		this.tempMinF = tempMinF;
	}

	public String getTempMaxF() {
		return tempMaxF;
	}

	public void setTempMaxF(String tempMaxF) {
		this.tempMaxF = tempMaxF;
	}

	public String getWeatherIconUrl() {
		return weatherIconUrl;
	}

	public void setWeatherIconUrl(String weatherIconUrl) {
		this.weatherIconUrl = weatherIconUrl;
	}

	public String getTempMinC() {
		return tempMinC;
	}

	public void setTempMinC(String tempMinC) {
		this.tempMinC = tempMinC;
	}

	public String getTempMaxC() {
		return tempMaxC;
	}

	public void setTempMaxC(String tempMaxC) {
		this.tempMaxC = tempMaxC;
	}

}
