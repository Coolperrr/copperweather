package com.copperweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class Now {
	@SerializedName("tmp")
	public String temperature;
	@SerializedName("cond")
	public More more;
	public Wind wind;

	public class More {
		@SerializedName("txt")
		public String nowWeatherState;
		@SerializedName("code")
		public String nowWeatherCode;
	}

	public class Wind {
		@SerializedName("dir")
		public String nowWindDirection;
		@SerializedName("sc")
		public String nowWindScale;
		@SerializedName("spd")
		public String nowWindSpeed;
	}

}
