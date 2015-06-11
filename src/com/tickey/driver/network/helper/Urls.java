package com.tickey.driver.network.helper;

public class Urls {

	public static final String BASE_NES_HOME = "http://192.168.0.7/";
	
	public static final String WEBVOX = "http://10.0.1.75/TickeyTestServer/public/";
	
	public static final String BASE_V1 = "http://api.tickey.me/v1/";

//	public static final String STAGE_V1 = "http://stage.api.tickey.me/v1/";
	
	
	
	public static final String STAGE_V1 = "http://mk.api.tickey.me/v1/";
	
	public static final String UPLOAD_IMAGE = "http://api.everlive.com/v1/O2nVwi9maNQ6ULMI/Files";
	
	public static final String TEST_ME = BASE_NES_HOME + "TickeyTestServer/public/";
	
	public static final String LINES = STAGE_V1 + "lines";
	
	public static final String POST_REG_LINE = STAGE_V1 + "lines/%s/bus";
	
	public static final String UPDATE_BUS_LOCATION = STAGE_V1 + "buses/%s/location";
	
	public static final String ME = STAGE_V1 + "auth/me";
	
	public static final String BEACON = WEBVOX + "beacon";
}
