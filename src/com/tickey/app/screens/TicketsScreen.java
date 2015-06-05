package com.tickey.app.screens;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request.Priority;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tickey.R;
import com.tickey.app.common.BaseApplication;
import com.tickey.app.data.model.Employee;
import com.tickey.app.data.model.ServerResponse;
import com.tickey.app.network.callback.TickeyError;
import com.tickey.app.network.helper.GsonRequest;
import com.tickey.app.network.helper.Urls;
import com.tickey.app.utility.Authorization;
import com.tickey.app.utility.Me;
import com.tickey.app.utility.NNAsyncTask;
import com.tickey.app.view.custom.CircleNetworkImageView;

public class TicketsScreen extends ActionBarActivity {
	
	private static final String TAG = TicketsScreen.class.getSimpleName();
	
	private Toolbar mToolbar;
	
	private CircleNetworkImageView mCircleImageView;
	private TextView mDriverName;
	
	private ImageLoader mImageLoader = BaseApplication.getInstance()
			.getImageLoader();	
	private RequestQueue mRequestQueue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_tickets_screen);
		
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
		}
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		mRequestQueue = BaseApplication.getInstance().getRequestQueue();
		mCircleImageView = (CircleNetworkImageView) findViewById(R.id.profileAvatar);
		mDriverName = (TextView) findViewById(R.id.tv_driverNmae);
		
		new NNAsyncTask(this,false) {
			
			@Override
			public boolean onPostLoad() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onLoad() {
				// TODO Auto-generated method stub
				Beacon beacon = new Beacon.Builder()
				
		        .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
		        .setId2("1")
		        .setId3("2")
		        .setManufacturer(0x004C) // Apple.  Change this for other beacon layouts
		        .setTxPower(-59)
		        .setDataFields(Arrays.asList(new Long[] {0l})) // Remove this for beacon layouts without d: fields
		        .build();
				
				// Change the layout below for other beacon types
				BeaconParser beaconParser = new BeaconParser()
				        .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
				BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser); 
				beaconTransmitter.startAdvertising(beacon);
				
				return false;
			}
		}.execute();
		

		String me = getIntent().getStringExtra(Me.class.getSimpleName());
		if(me != null) {
			Gson gson = new Gson();
			Employee meEmployee = gson.fromJson(me, Employee.class);
			setUpActionBarMe(meEmployee);
		} else {
			getMe();
		}		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tickets_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_logOut) {
			Authorization.logout(TicketsScreen.this);
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void getMe() {
		Type loginResponseType = new TypeToken<ServerResponse<Employee>>() {
		}.getType();

		GsonRequest<ServerResponse<Employee>> loginRequest = new GsonRequest<ServerResponse<Employee>>(
				Method.GET, Urls.ME, loginResponseType, null,
				Authorization.getAuthorizationHeader(getApplicationContext()),
				meReqSuccessListener(), meReqErrorListener());
		loginRequest.setShouldCache(true);

		loginRequest.mContentType = "application/json";
		loginRequest.setPriority(Priority.NORMAL);
//		MyLog.i("tag", ""+loginRequest);
		mRequestQueue.add(loginRequest);
	}

	private TickeyError meReqErrorListener() {
		return new TickeyError(this) {

			@Override
			public void onTickeyErrorResponse(VolleyError error) {
			}
		};
	}

	private Listener<ServerResponse<Employee>> meReqSuccessListener() {
		return new Listener<ServerResponse<Employee>>() {

			@Override
			public void onResponse(ServerResponse<Employee> response) {
				Employee employee = response.result;
				
				
				setUpActionBarMe(employee);
				
			}
		};
	}
	
	
	public void setUpActionBarMe(Employee me) {
		mCircleImageView.setImageUrl(me.imageUrl,mImageLoader);
		if(!TextUtils.isEmpty(me.name)) {
			mDriverName.setText(me.name);
		}
	}
}
