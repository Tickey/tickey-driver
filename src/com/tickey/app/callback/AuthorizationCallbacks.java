package com.tickey.app.callback;

import com.android.volley.VolleyError;
import com.tickey.app.data.model.Employee;
import com.tickey.app.data.model.ServerResponse;
import com.tickey.app.data.model.User;

public abstract class AuthorizationCallbacks {
	public abstract void preExecute();
	public abstract void onResponse(ServerResponse<Employee> response);
	public abstract void onErrorResponse(VolleyError error);
}
