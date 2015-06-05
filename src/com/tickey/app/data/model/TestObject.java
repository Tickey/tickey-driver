package com.tickey.app.data.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class TestObject {

	@SerializedName("lines")
	public ArrayList<String> lines;
	
	@SerializedName("user")
	public User user;
}
