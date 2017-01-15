package com.copperweather.android.gson;

import com.google.gson.annotations.SerializedName;

/*
 * ʹ��@SerializedNameע��������json��java�ֶε�ӳ���ϵ��
 */
public class Basic {
	@SerializedName("city")
	public String cityName;
	@SerializedName("id")
	public String weatherId;
	public Update update;

	public class Update {
		@SerializedName("loc")
		public String updateTime;
	}
}
