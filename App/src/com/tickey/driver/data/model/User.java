package com.tickey.driver.data.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class User {

	@SerializedName("facebookId")
	public String facebookId;

	@SerializedName("username")
	public String username;

	@SerializedName("password")
	public String password;

	@SerializedName("firstName")
	public String firstName;

	@SerializedName("lastName")
	public String lastName;
	
	@SerializedName("fullName")
	public String fullName;
	
	@SerializedName("birthday")
	public String birthday;

	@SerializedName("imageUrl")
	public String imageUrl;

	@SerializedName("provider")
	public String provider;

	@SerializedName("token")
	public String token;

	@SerializedName("createdAt")
	public String createdAt;

	public boolean isFacebook() {

		boolean result = false;

		if (!TextUtils.isEmpty(provider)) {
			if (provider.equalsIgnoreCase("facebook")) {
				result = true;
			}
		}

		return result;
	}

}
