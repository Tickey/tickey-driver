package com.tickey.driver.data.model;

import com.google.gson.annotations.SerializedName;

public class ServerResponse<T> {

	@SerializedName("status")
	public int status;

	@SerializedName("message")
	public String message;

	@SerializedName("result")
	public T result;
}
