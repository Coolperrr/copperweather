package com.copperweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class Suggestion {

	@SerializedName("comf")
	public Comfort comfort;
	@SerializedName("flu")
	public Flu flu;
	@SerializedName("drsg")
	public Dress dress;
	@SerializedName("uv")
	public UV uv;
	public Sport sport;

	public class Comfort {
		@SerializedName("txt")
		public String info;
	}

	public class Flu {
		@SerializedName("txt")
		public String info;
	}

	public class Dress {
		@SerializedName("txt")
		public String info;
	}

	public class UV {
		@SerializedName("txt")
		public String info;
	}

	public class Sport {
		@SerializedName("txt")
		public String info;
	}
}
