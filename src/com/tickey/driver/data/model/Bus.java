package com.tickey.driver.data.model;

import com.google.gson.annotations.SerializedName;

public class Bus {
	
	@SerializedName("beacon")
	public String beaconId;
	
	@SerializedName("currentDriverId")
	public String currentDriverId;
	
	@SerializedName("line")
	public String line;
	
	@SerializedName("brand")
	public String brand;
	
	@SerializedName("regNumber")
	public String regNumber;
}
