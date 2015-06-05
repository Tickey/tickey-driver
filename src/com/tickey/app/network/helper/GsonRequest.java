package com.tickey.app.network.helper;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tickey.app.data.model.ServerResponse;
import com.tickey.app.network.callback.TickeyError;
import com.tickey.app.utility.MyLog;

public class GsonRequest<T> extends Request<T> {

	private static final String TYPE_JSON_ARRAY = "class org.json.JsonArray";

	private static final String TYPE_JSON_OBJECT = "class org.json.JSONObject";

	private static final String TAG = GsonRequest.class.getSimpleName();

	private Priority mPriority = Priority.LOW;

	private final Gson mGson = new Gson();
	private final Type mType;
	private Map<String, String> mHeaders;
	private Map<String, Object> mMapParams;
	private final Listener<T> mListener;
	public String mContentType;

	/**
	 * Make a GET request and return a parsed object from JSON.
	 * 
	 * @param url
	 *            URL of the request to make
	 * @param clazz
	 *            Relevant class object, for Gson's reflection
	 * @param params
	 *            Map of request body parameters
	 * @param headers
	 *            Map of request headers
	 */
	public GsonRequest(int method, String url, Type type,
			Map<String, Object> params, Map<String, String> headers,
			Listener<T> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		this.mType = type;
		this.mHeaders = headers;
		this.mMapParams = params;
		this.mListener = listener;
	}

	/**
	 * Make a GET request and return a parsed object from JSON.
	 * 
	 * @param url
	 *            URL of the request to make
	 * @param clazz
	 *            Relevant class object, for Gson's reflection
	 * @param params
	 *            Map of request body parameters
	 */
	public GsonRequest(int method, String url, Type type,
			Map<String, Object> params, Listener<T> listener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		this.mType = type;
		this.mMapParams = params;
		this.mListener = listener;
	}



	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return mHeaders != null ? mHeaders : super.getHeaders();
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		byte[] body = null;

		if (mMapParams != null) {
			JSONObject json = new JSONObject();

			Iterator<Entry<String, Object>> it = mMapParams.entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> pairs = (Map.Entry<String, Object>) it
						.next();
				try {
					json.put(pairs.getKey(), pairs.getValue());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				it.remove(); // avoids a ConcurrentModificationException
			}

			if (json != null) {
				MyLog.v(TAG, "json: " + json.toString());
				body = json.toString().getBytes();
			}
		}

		return body != null ? body : super.getBody();
	}

	@Override
	public Priority getPriority() {
		return mPriority;
	}

	public void setPriority(Priority priority) {
		mPriority = priority;
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

	@Override
	public String getBodyContentType() {
		if (mContentType == null)
			return super.getBodyContentType();
		else
			return mContentType;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));

			MyLog.v(TAG, "response: " + json);

			Object resultObject;

			if (mType.toString().equalsIgnoreCase(TYPE_JSON_OBJECT)) {
				resultObject = new JSONObject(json);
			} else if (mType.toString().equalsIgnoreCase(TYPE_JSON_ARRAY)) {
				resultObject = new JSONArray(json);
			} else {
				resultObject = mGson.fromJson(json, mType);
			}

			return Response.success((T) resultObject,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}



	
}
