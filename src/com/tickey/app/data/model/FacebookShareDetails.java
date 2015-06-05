package com.tickey.app.data.model;

import com.google.gson.annotations.SerializedName;

public class FacebookShareDetails {

	@SerializedName("canonical_url")
	public String url;
	
	@SerializedName("id")
	public String id;
}
