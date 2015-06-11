package com.tickey.driver.callback;

import com.android.volley.VolleyError;
import com.tickey.driver.data.model.Employee;
import com.tickey.driver.data.model.ServerResponse;
import com.tickey.driver.data.model.User;

public abstract class AuthorizationCallbacks {
	public abstract void preExecute();
	public abstract void onResponse(ServerResponse<Employee> response);
	public abstract void onErrorResponse(VolleyError error);
}
