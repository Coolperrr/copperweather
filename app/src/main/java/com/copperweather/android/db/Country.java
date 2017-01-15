package com.copperweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by copper on 2017/1/15.
 */

public class Country extends DataSupport {

    private int id;
    private int countryCode;
    private String weatherId;
    private String countryName;

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

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
