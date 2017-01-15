package com.copperweather.android.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.copperweather.android.gson.Weather;
import com.copperweather.android.util.HttpUtil;
import com.copperweather.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class AutoUpdateService extends Service {

	private final static String MY_KEY = "6d145ff79ddf4bbb8cb920902f898694";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		updateWeather();
		updateBingPic();
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 1 * 60 * 60 * 3600;
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
		Intent i = new Intent(this, AutoUpdateService.class);
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
		manager.cancel(pi);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}

	private void updateBingPic() {
		// TODO Auto-generated method stub

		String picUrl = "http://guolin.tech/api/bing_pic";

		HttpUtil.sendOkHttpRequest(picUrl, new Callback() {

			@Override
			public void onResponse(Call call, Response response)
					throws IOException {
				// TODO Auto-generated method stub
				String bingPic = response.body().string();
				SharedPreferences.Editor editor = PreferenceManager
						.getDefaultSharedPreferences(AutoUpdateService.this)
						.edit();
				editor.putString("bing_pic", bingPic);
				editor.apply();
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void updateWeather() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String weatherString = prefs.getString("weather", null);
		if (weatherString != null) {
			Weather weather = Utility.handleWeatherResponse(weatherString);
			String weatherId = weather.basic.weatherId;
			String weatherUrl = "http://guolin.tech/api/weather?cityid="
					+ weatherId + "&key=" + MY_KEY;
			HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {

				@Override
				public void onResponse(Call call, Response response)
						throws IOException {
					// TODO Auto-generated method stub
					String responseText = response.body().string();
					Weather weather = Utility
							.handleWeatherResponse(responseText);
					if (weather != null && "ok".equals(weather.status)) {
						SharedPreferences.Editor editor = PreferenceManager
								.getDefaultSharedPreferences(
										AutoUpdateService.this).edit();
						editor.putString("weather", responseText);
						editor.apply();
					}
				}

				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub

				}
			});
		}
	}

}
