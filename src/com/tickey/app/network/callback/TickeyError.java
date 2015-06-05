package com.tickey.app.network.callback;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.android.volley.NetworkResponse;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.tickey.app.utility.Authorization;
import com.tickey.app.utility.MyLog;

public abstract class TickeyError implements ErrorListener {

	private static final String TAG = TickeyError.class.getSimpleName();

	private Activity mActivity;

	public TickeyError(Activity activity) {
		mActivity = activity;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		onTickeyErrorResponse(error);
		log(error);

		NetworkResponse networkResponse = error.networkResponse;
		if (networkResponse != null
				&& (networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED || networkResponse.statusCode == HttpStatus.SC_FORBIDDEN)) {
			MyLog.v(TAG, "403");

			Authorization.logout(mActivity);
		}
	}

	public abstract void onTickeyErrorResponse(VolleyError error);

	public static void log(VolleyError error) {
		String json = null;

		NetworkResponse response = error.networkResponse;
		if (response != null && response.data != null) {
			json = new String(response.data);
			json = trimMessage(json, "message");
			if (json != null)
				displayMessage(json);
		}
	}

	private static String trimMessage(String json, String key) {
		String trimmedString = null;

		try {
			JSONObject obj = new JSONObject(json);
			trimmedString = obj.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return trimmedString;
	}

	// Somewhere that has access to a context
	private static void displayMessage(String message) {
		MyLog.v(TAG, message);
	}

}
