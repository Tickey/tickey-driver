package com.tickey.app.utility;

import java.lang.reflect.Type;

import android.app.Activity;
import android.text.TextUtils;

import com.android.volley.Request.Method;
import com.android.volley.Request.Priority;
import com.android.volley.Response.Listener;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tickey.app.data.model.Employee;
import com.tickey.app.data.model.ServerResponse;
import com.tickey.app.network.callback.TickeyError;
import com.tickey.app.network.helper.GsonRequest;
import com.tickey.app.network.helper.Urls;

public abstract class Me {
	
	private Activity instance;
	private RequestQueue mRequestQueue;
	private Employee me;
	
	public Me(Activity instance, RequestQueue mRequestQueue) {
		this.instance = instance;
		this.mRequestQueue = mRequestQueue;
	}
	
	public void getMe() {
		Type loginResponseType = new TypeToken<ServerResponse<Employee>>() {
		}.getType();

		GsonRequest<ServerResponse<Employee>> loginRequest = new GsonRequest<ServerResponse<Employee>>(
				Method.GET, Urls.ME, loginResponseType, null,
				Authorization.getAuthorizationHeader(instance.getApplicationContext()),
				meReqSuccessListener(), meReqErrorListener());
		loginRequest.setShouldCache(true);

		loginRequest.mContentType = "application/json";
		loginRequest.setPriority(Priority.NORMAL);
//		MyLog.i("tag", ""+loginRequest);
		mRequestQueue.add(loginRequest);
	}

	public abstract void error();
	
	private TickeyError meReqErrorListener() {
		return new TickeyError(instance) {

			@Override
			public void onTickeyErrorResponse(VolleyError error) {
				error();
			}
		};
	}

	public abstract void response() ;
	
	
	private Listener<ServerResponse<Employee>> meReqSuccessListener() {
		return new Listener<ServerResponse<Employee>>() {

			@Override
			public void onResponse(ServerResponse<Employee> response) {
				me = response.result;
				response();				
			}
		};
	}

	public String getJsonMe() {
		Gson gson = new Gson();
		return gson.toJson(me);
	}
	
	
}
