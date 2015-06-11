package com.tickey.driver.network.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;

public class MultipartRequest extends Request<JSONObject> {
	public static final String KEY_PICTURE = "1";

	private HttpEntity mHttpEntity;

	private Response.Listener<JSONObject> mListener;

	public MultipartRequest(String url, String filePath,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errorListener) {
		super(Method.POST, url, errorListener);
		mListener = listener;
		mHttpEntity = buildMultipartEntity(filePath);
	}

	public MultipartRequest(String url, File file,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errorListener) {
		super(Method.POST, url, errorListener);

		mListener = listener;
		mHttpEntity = buildMultipartEntity(file);
	}

	public MultipartRequest(String url, byte[] byteArray,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errorListener) {
		super(Method.POST, url, errorListener);

		mListener = listener;
		mHttpEntity = buildMultipartEntity(byteArray);
	}

	private HttpEntity buildMultipartEntity(String filePath) {
		File file = new File(filePath);
		return buildMultipartEntity(file);
	}

	private HttpEntity buildMultipartEntity(File file) {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		ContentType mimeType = ContentType.create("image/png");
		FileBody fileBody = new FileBody(file, mimeType);
		builder.addPart(KEY_PICTURE, fileBody);
		return builder.build();
	}

	private HttpEntity buildMultipartEntity(byte[] byteArray) {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		ContentType mimeType = ContentType.create("image/png");
		ByteArrayBody byteArrayBody = new ByteArrayBody(byteArray, mimeType,
				"image.png");
		builder.addPart(KEY_PICTURE, byteArrayBody);
		return builder.build();
	}

	@Override
	public String getBodyContentType() {
		return mHttpEntity.getContentType().getValue();
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			mHttpEntity.writeTo(bos);
		} catch (IOException e) {
			VolleyLog.e("IOException writing to ByteArrayOutputStream");
		}
		return bos.toByteArray();
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));

			return Response.success(new JSONObject(json),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException e) {
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		mListener.onResponse(response);
	}
}