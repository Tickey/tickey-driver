package com.tickey.driver.data.model;

import com.google.gson.annotations.SerializedName;

public class Employees {

	@SerializedName("driver")
	public Employee driver;

	@SerializedName("conductor")
	public Employee conductor;
}
