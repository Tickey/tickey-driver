package com.tickey.app.network.helper;

public class Urls {

	public static final String BASE_NES_HOME = "http://192.168.0.7/";
	
	public static final String WEBVOX = "http://10.0.1.75/TickeyTestServer/public/";
	
	public static final String BASE_V1 = "http://api.tickey.me/v1/";

//	public static final String STAGE_V1 = "http://stage.api.tickey.me/v1/";
	
	public static final String STAGE_V1 = "http://mk.api.tickey.me/v1/";
	
	public static final String STAGE_PAYMENTS_V1 = "http://stage.api.tickey.me/payments/v1/";
	
	public static final String GET_POSTS = STAGE_V1 + "lines/%s/posts?page=%d&pageSize=%d";
	public static final String GET_POST = STAGE_V1 + "lines/%s/posts/%s";
	public static final String PUBLISH_POST = STAGE_V1 + "lines/%s/feedback";
	public static final String FACBOOK_LOGIN = STAGE_V1 + "auth/facebook";
	public static final String ME = STAGE_V1 + "auth/me";
	public static final String LIKE_POST = STAGE_V1 + "lines/%s/posts/%s/like";
	public static final String SUGGEST_LINE_NAME = STAGE_V1 + "lines/%s/names/suggest";
	public static final String GET_LINE_NAMES = STAGE_V1 + "lines/%s/names/all";
	public static final String VOTE_FOR_LINE_NAMES = STAGE_V1 + "lines/%s/names/like";
	public static final String GET_EMPLOYEES = STAGE_V1 + "employees/bus/%s";
	public static final String RATE_EMPLOYEE = STAGE_V1 + "employees/%s/rate/%d";
	public static final String GET_LINE_NAME_GAME_STATE = STAGE_V1 + "lines/%s/names/phase";
	public static final String OPEN_FAREGATE = STAGE_V1 + "faregate/demoapp";
	public static final String BUY_TICKET = STAGE_PAYMENTS_V1 + "braintree/ride";
	
	public static final String UPLOAD_IMAGE = "http://api.everlive.com/v1/O2nVwi9maNQ6ULMI/Files";
	
	public static final String TEST_ME = BASE_NES_HOME + "TickeyTestServer/public/";
	
	public static final String LINES = STAGE_V1 + "lines";
	
	public static final String POST_REG_LINE = STAGE_V1 + "lines/%s/bus";
	
	
	public static final String BEACON = WEBVOX + "beacon";
}
