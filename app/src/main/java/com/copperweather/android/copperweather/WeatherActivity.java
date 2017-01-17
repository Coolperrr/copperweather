package com.copperweather.android.copperweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.copperweather.android.R;
import com.copperweather.android.gson.Forecast;
import com.copperweather.android.gson.Weather;
import com.copperweather.android.service.AutoUpdateService;
import com.copperweather.android.snowfall.FlakeView;
import com.copperweather.android.util.HttpUtil;
import com.copperweather.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView mWeatherLayout;
    private TextView mTitleCity, mTitleUpdateTime, mDegreeText,
            mWeatherInfoText, mAqiText, mPm25Text, mComfortText, mFluText,
            mUVText, mDressText, mWindDirection, mWindSpeed, mWindScale,
            mSportText;
    private LinearLayout mForecastLayout;

    private FrameLayout mFrameLayout;
    private FlakeView flakeView;

    private ImageView mBingPicImg, mNowWeatherPic;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected String weatherId;
    private int daysCount = 3;

    private Button mToolButton;
    private final static String MY_KEY = "6d145ff79ddf4bbb8cb920902f898694";
    private Handler handlerRain = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            // View decorView = getWindow().getDecorView();
            // decorView
            // .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            // | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            // | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            // getWindow().setStatusBarColor(Color.TRANSPARENT);

            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }
        setContentView(R.layout.activity_weather);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout
                .setColorSchemeResources(android.R.color.background_dark);

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        // final String weatherId;
        mWeatherLayout = (ScrollView) findViewById(R.id.weather_layout);

        mForecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);


        mFrameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        flakeView = new FlakeView(this);
        mFrameLayout.addView(flakeView);

        mTitleCity = (TextView) findViewById(R.id.title_city);
        mTitleUpdateTime = (TextView) findViewById(R.id.title_updateTime);
        mDegreeText = (TextView) findViewById(R.id.degree_text);
        mWeatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        mAqiText = (TextView) findViewById(R.id.aqi_text);
        mPm25Text = (TextView) findViewById(R.id.pm25_text);
        mComfortText = (TextView) findViewById(R.id.comfort_text);
        mFluText = (TextView) findViewById(R.id.flu_text);
        mUVText = (TextView) findViewById(R.id.uv_text);
        mDressText = (TextView) findViewById(R.id.dress_text);
        mSportText = (TextView) findViewById(R.id.sport_text);
        mWindDirection = (TextView) findViewById(R.id.wind_direction);
        mWindScale = (TextView) findViewById(R.id.wind_scale);
        mWindSpeed = (TextView) findViewById(R.id.wind_speed);

        mToolButton = (Button) findViewById(R.id.nav_button);

        mNowWeatherPic = (ImageView) findViewById(R.id.now_weather_state_img);
        mBingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic == null) {
            Glide.with(this).load(bingPic).into(mBingPicImg);
        } else {
            loadBingPic();
        }

        if (weatherString != null) {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            weatherId = getIntent().getStringExtra("weather_id");
            mWeatherLayout.setVisibility(View.INVISIBLE);
            requestWeatherInfo(weatherId);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                requestWeatherInfo(weatherId);
            }
        });
    }

    private Runnable runnableRain = new Runnable() {

        @Override
        public void run() {
            flakeView.addFlakes(15);
            handlerRain.postDelayed(runnableRain, 4000);
            if (flakeView.getNumFlakes() > 75) {
                handlerRain.removeCallbacks(runnableRain);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        handlerRain.postDelayed(runnableRain, 1000);
    }

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                // TODO Auto-generated method stub
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(WeatherActivity.this)
                        .edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Glide.with(WeatherActivity.this).load(bingPic)
                                .into(mBingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException response) {
                // TODO Auto-generated method stub
                Toast.makeText(WeatherActivity.this, "图片加载失败",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showWeatherInfo(Weather weather) {

        String cityName = weather.basic.cityName;
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.nowWeatherState;
        mTitleCity.setText(cityName);
        mDegreeText.setText(degree);
        mWeatherInfoText.setText(weatherInfo);
        mNowWeatherPic.setImageResource(loadWeatherStatePic(Integer
                .parseInt(weather.now.more.nowWeatherCode)));

        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String windScale = weather.now.wind.nowWindScale + "级";
        String windSpeed = weather.now.wind.nowWindSpeed + "km/h";
        String windDirection = weather.now.wind.nowWindDirection;
        mTitleUpdateTime.setText("最近更新于："+updateTime);
        mWindDirection.setText(windDirection);
        mWindSpeed.setText(windSpeed);
        mWindScale.setText(windScale);

        mForecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(
                    R.layout.weather_forecast_item, mForecastLayout, false);

            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            if (daysCount > 2) {
                dateText.setText("今天");
                daysCount = daysCount - 1;
            } else if (daysCount > 1) {
                dateText.setText("明天");
                daysCount = daysCount - 1;
            } else if (daysCount > 0) {
                dateText.setText("后天");
                daysCount = daysCount - 1;
            } else if (daysCount <= 0) {
                daysCount = 3;
                dateText.setText("今天");
                daysCount = daysCount - 1;
            }

            TextView weatherInfoText = (TextView) view
                    .findViewById(R.id.info_text);
            weatherInfoText.setText(forecast.more.foreWeatherState);
            ImageView weatherStatePic = (ImageView) view
                    .findViewById(R.id.fore_weather_state_img);
            weatherStatePic.setImageResource(loadWeatherStatePic(Integer
                    .parseInt(forecast.more.foreWeatherCode)));

            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            mForecastLayout.addView(view);
        }
        mForecastLayout.setVisibility(View.VISIBLE);

        if (weather.aqi != null) {
            mAqiText.setText(weather.aqi.city.aqi);
            mPm25Text.setText(weather.aqi.city.pm25);
        }


        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String flu = "感冒防范：" + weather.suggestion.flu.info;
        String dress = "穿衣建议：" + weather.suggestion.dress.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;
        String uv = "防晒建议：" + weather.suggestion.uv.info;

        mComfortText.setText(comfort);
        mFluText.setText(flu);
        mDressText.setText(dress);
        mUVText.setText(uv);
        mSportText.setText(sport);

        if (weather != null && "ok".equals(weather.status)) {
            Intent i = new Intent(this, AutoUpdateService.class);
            startService(i);

        } else {
            Toast.makeText(WeatherActivity.this, "更新失败了",
                    Toast.LENGTH_SHORT).show();
        }

    }

    protected void requestWeatherInfo(final String weatherId) {

        String weatherUrl = "http://guolin.tech/api/weather?cityid="
                + weatherId + "&key=" + MY_KEY;
//        String weatherUrl ="https://free-api.heweather.com/v5/weather?city="+"weatherId"+"&key="+MY_KEY;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {

                final String responseText = response.body().string();
                final Weather weather = Utility
                        .handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(
                                            WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);

                        } else {
                            Toast.makeText(WeatherActivity.this,
                                    "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException response) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        Toast.makeText(WeatherActivity.this, "��ȡ������Ϣʧ�ܣ���Ǹ����",
                                Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                });
            }
        });
        loadBingPic();
    }

    private int loadWeatherStatePic(int weatherCode) {
        switch (weatherCode) {
            case 100:
                return R.drawable.wea100;
            case 101:
                return R.drawable.wea101;
            case 102:
                return R.drawable.wea102;
            case 103:
                return R.drawable.wea103;
            case 104:
                return R.drawable.wea104;
            case 200:
                return R.drawable.wea200;
            case 201:
                return R.drawable.wea201;
            case 202:
                return R.drawable.wea202;
            case 203:
                return R.drawable.wea203;
            case 204:
                return R.drawable.wea204;
            case 205:
                return R.drawable.wea205;
            case 206:
                return R.drawable.wea206;
            case 207:
                return R.drawable.wea207;
            case 208:
                return R.drawable.wea208;
            case 209:
                return R.drawable.wea209;
            case 210:
                return R.drawable.wea210;
            case 211:
                return R.drawable.wea211;
            case 212:
                return R.drawable.wea212;
            case 213:
                return R.drawable.wea213;
            case 300:
                return R.drawable.wea300;
            case 301:
                return R.drawable.wea301;
            case 302:
                return R.drawable.wea302;
            case 303:
                return R.drawable.wea303;
            case 304:
                return R.drawable.wea304;
            case 305:
                return R.drawable.wea305;
            case 306:
                return R.drawable.wea306;
            case 307:
                return R.drawable.wea307;
            case 308:
                return R.drawable.wea308;
            case 309:
                return R.drawable.wea309;
            case 310:
                return R.drawable.wea310;
            case 311:
                return R.drawable.wea311;
            case 312:
                return R.drawable.wea312;
            case 313:
                return R.drawable.wea313;
            case 400:
                return R.drawable.wea400;
            case 401:
                return R.drawable.wea401;
            case 402:
                return R.drawable.wea402;
            case 403:
                return R.drawable.wea403;
            case 404:
                return R.drawable.wea404;
            case 405:
                return R.drawable.wea405;
            case 406:
                return R.drawable.wea406;
            case 407:
                return R.drawable.wea407;
            case 500:
                return R.drawable.wea500;
            case 501:
                return R.drawable.wea501;
            case 502:
                return R.drawable.wea502;
            case 503:
                return R.drawable.wea503;
            case 504:
                return R.drawable.wea504;
            case 507:
                return R.drawable.wea507;
            case 508:
                return R.drawable.wea508;
            case 900:
                return R.drawable.wea900;
            case 901:
                return R.drawable.wea901;
            case 999:
                return R.drawable.wea999;
            default:
                return R.drawable.wea999;
        }
    }
}
