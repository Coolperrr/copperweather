package com.copperweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by copper on 2017/1/15.
 */

public class County extends DataSupport {

    private int id;
    private int cityId;
    private String weatherId;
    private String countyName;

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }
}
