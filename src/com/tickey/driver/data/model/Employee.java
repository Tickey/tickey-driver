package com.tickey.driver.data.model;

import com.google.gson.annotations.SerializedName;

public class Employee {

	public static final int MAX_RATE = 5;

	public static enum EmployeeType {
		DRIVER, CONDUCTOR
	}

	@SerializedName("_id")
	public String id;

	@SerializedName("name")
	public String name;

	@SerializedName("imageUrl")
	public String imageUrl;

	@SerializedName("rating")
	public float rating;
	
	@SerializedName("token")
	public String token;
	
	@SerializedName("currentBus")
	public Bus busAssigned;
	
	public EmployeeType employeeType;

}
