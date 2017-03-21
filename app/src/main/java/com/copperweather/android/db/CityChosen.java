package com.copperweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by copper on 2017/3/20.
 */

public class CityChosen extends DataSupport{

    private String weatherId;
    private String weatherInfo;

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public String getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(String weatherInfo) {
        this.weatherInfo = weatherInfo;
    }
}
