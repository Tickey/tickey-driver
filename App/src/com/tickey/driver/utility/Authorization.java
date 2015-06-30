package com.tickey.driver.utility;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import com.tickey.driver.callback.AuthorizationCallbacks;
import com.tickey.driver.data.model.Employee;
import com.tickey.driver.data.model.ServerResponse;
import com.tickey.driver.network.callback.TickeyError;
import com.tickey.driver.network.helper.GsonRequest;
import com.tickey.driver.network.helper.Urls;
import com.tickey.driver.screens.LoginScreen;

public class Authorization {

	protected static final String TAG = Authorization.class.getSimpleName();

	public static final String USER_TOKEN = "token";
	public static final String PHONE_SALES_COUNT = "phone_sales_count";
	public static final String KEY_FB_ACCESS_TOKEN = "access_token";
	public static final String KEY_AUTH_HEADER = "Authorization";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_PASSWORD = "password";
	public static final String PARAM_KEY_DEVICE_ID = "deviceId";
	public static final String PARAM_KEY_DEVICE_NAME = "deviceName";

	public static String userAuthenticationToken;
	private static SharedPreferences mSharedPreferences;

	private RequestQueue mRequestQqueue;
	private Context mContext;

	private AuthorizationCallbacks mAuthCallback;

	public static void initialize(Context context) {

		mSharedPreferences = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);

		userAuthenticationToken = mSharedPreferences
				.getString(USER_TOKEN, null);
	}

	public static boolean isLoggedIn(Context context) {
		if (mSharedPreferences == null) {
			initialize(context);
		}

		return !TextUtils.isEmpty(userAuthenticationToken);
	}

	public static String getUserAuthenticationToken(Context context) {
		if (mSharedPreferences == null) {
			initialize(context);
		}

		return userAuthenticationToken;
	}

	public static void setUserAuthenticationToken(Context context,
			String userAuthenticationToken) {

		if (mSharedPreferences == null) {
			initialize(context);
		}

		Authorization.userAuthenticationToken = userAuthenticationToken;
		mSharedPreferences.edit()
				.putString(USER_TOKEN, userAuthenticationToken).commit();
		mSharedPreferences.edit().putInt(PHONE_SALES_COUNT, 0).commit();
	}

	public void facebookLogin(Context context, String accessToken,
			String gcmId, String deviceName, String deviceId,
			AuthorizationCallbacks authCallbacks) {
		mAuthCallback = authCallbacks;

		if (mAuthCallback != null) {
			mAuthCallback.preExecute();
		}
		facebookLogin(context, accessToken, gcmId, deviceName, deviceId);
	}

	public void facebookLogin(Context context, String accessToken,
			String gcmId, String deviceName, String deviceId) {
		String url = "";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Authorization.KEY_FB_ACCESS_TOKEN, accessToken);
		params.put(Authorization.PARAM_KEY_DEVICE_ID, deviceId);
		params.put(Authorization.PARAM_KEY_DEVICE_NAME, deviceName);
		params.put(Google.PROPERTY_REG_ID, gcmId);

		loginRequest(context, url, params);
	}

	public void login(Context context, String username, String password,
			String gcmId, String deviceName, String deviceId,
			AuthorizationCallbacks authCallbacks) {
		mAuthCallback = authCallbacks;

		if (mAuthCallback != null) {
			mAuthCallback.preExecute();
		}
		login(context, username, password, gcmId, deviceName, deviceId);
	}

	public void login(Context context, String username, String password,
			String gcmId, String deviceName, String deviceId) {
		// TODO provide right url fucking bitch
		String url = Urls.LOGIN;

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(KEY_USERNAME, username);
		params.put(KEY_PASSWORD, password);
		params.put(Authorization.PARAM_KEY_DEVICE_ID, deviceId);
		params.put(Authorization.PARAM_KEY_DEVICE_NAME, deviceName);
		params.put(Google.PROPERTY_REG_ID, gcmId);
		params.put("isTest", true);
		loginRequest(context, url, params);
	}

	private void loginRequest(Context context, String url,
			Map<String, Object> params) {

		mContext = context;
		if (mRequestQqueue == null) {

			mRequestQqueue = Volley.newRequestQueue(context);
		}

		Type loginResponseType = new TypeToken<ServerResponse<Employee>>() {
		}.getType();

		GsonRequest<ServerResponse<Employee>> loginRequest = new GsonRequest<ServerResponse<Employee>>(
				Method.POST, url, loginResponseType, params,
				loginReqSuccessListener(), loginReqErrorListener());

		loginRequest.mContentType = "application/json";
		mRequestQqueue.add(loginRequest);
	}

	private ErrorListener loginReqErrorListener() {

		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				TickeyError.log(error);

				if (mAuthCallback != null) {
					mAuthCallback.onErrorResponse(error);
				}
			}
		};
		return errorListener;
	}

	private Listener<ServerResponse<Employee>> loginReqSuccessListener() {
		Listener<ServerResponse<Employee>> responseListener = new Listener<ServerResponse<Employee>>() {

			@Override
			public void onResponse(ServerResponse<Employee> response) {
				
				MyLog.i(TAG, "" + response.result);
				
				Authorization.setUserAuthenticationToken(mContext,
						response.result.token);

				if (mAuthCallback != null) {
					mAuthCallback.onResponse(response);
				}
			}
		};
		return responseListener;
	}

	public static void invalidateLoginCredentials(Context context) {

		mSharedPreferences.edit().remove(USER_TOKEN).commit();
		mSharedPreferences.edit().remove(PHONE_SALES_COUNT).commit();
		userAuthenticationToken = null;
	}

	public static void logout(Activity activity) {

		Authorization.invalidateLoginCredentials(activity);

		Intent intent = new Intent(activity, LoginScreen.class);

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("loggedOut", true);
		
		activity.startActivity(intent);
		

		activity.finish();
	}

	public static HashMap<String, String> getAuthorizationHeader(Context context) {
		HashMap<String, String> map = new HashMap<String, String>();

		map.put(KEY_AUTH_HEADER, "bearer "
				+ getUserAuthenticationToken(context));

		return map;
	}
	
	public static int incrementPhoneSales() {
		int phonesSalesCount = mSharedPreferences.getInt(PHONE_SALES_COUNT, 0);
		phonesSalesCount++;
		mSharedPreferences.edit().putInt(PHONE_SALES_COUNT, phonesSalesCount).commit();
		return phonesSalesCount;
	}
	
	public static int getSessionPhoneSales() {
		return mSharedPreferences.getInt(PHONE_SALES_COUNT, 0);
	}
}