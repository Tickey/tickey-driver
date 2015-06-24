package com.tickey.driver.screens;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Request.Priority;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tickey.driver.R;
import com.tickey.driver.common.BaseApplication;
import com.tickey.driver.data.model.Employee;
import com.tickey.driver.data.model.ServerResponse;
import com.tickey.driver.network.callback.TickeyError;
import com.tickey.driver.network.helper.GsonRequest;
import com.tickey.driver.network.helper.Urls;
import com.tickey.driver.utility.Authorization;
import com.tickey.driver.utility.Me;
import com.tickey.driver.utility.MyLog;
import com.tickey.driver.utility.NNAsyncTask;
import com.tickey.driver.view.custom.CircleNetworkImageView;

public class TicketsScreen extends ActionBarActivity implements LocationListener, ConnectionCallbacks, OnConnectionFailedListener{
	
	private static final String TAG = TicketsScreen.class.getSimpleName();
	
	private Toolbar mToolbar;
	
	private CircleNetworkImageView mCircleImageView;
	private TextView mDriverName;
	
	private TextView mPhoneSalesCount;
	
	private Employee meEmployee; 
	
	private ImageLoader mImageLoader = BaseApplication.getInstance()
			.getImageLoader();	
	private RequestQueue mRequestQueue;
	
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	
	private Location mCurrentLocation;
	private Gson gson;
	private static int FASTEST_INTERVAL = 1000;
	private static int INTVERVAL = 2000;
	
	private Beacon beacon;
	private BeaconParser beaconParser;
	private BeaconTransmitter beaconTransmitter;
	
	private NNAsyncTask startBeacon;
	private Dialog dialog;
	private MenuItem mBusInfo;
	private Handler mHandler;
	private Runnable actionBarBusInfoAdding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_tickets_screen);
		hideSystemUI();
		mRequestQueue = BaseApplication.getInstance().getRequestQueue();
		mHandler = new Handler();
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
		}
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		mCircleImageView = (CircleNetworkImageView) findViewById(R.id.profileAvatar);
		mDriverName = (TextView) findViewById(R.id.tv_driverNmae);
		
		String me = getIntent().getStringExtra(Me.class.getSimpleName());
		gson = new Gson();
		if(me != null) {
			
			meEmployee = gson.fromJson(me, Employee.class);
			setUpActionBarMe(meEmployee);
		} else {
			getMe();
		}	
		
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
		    // Device does not support Bluetooth
			mBluetoothAdapter.enable();
		}
		
		startBeacon = new NNAsyncTask(this, false) {
			
			@Override
			public boolean onPostLoad() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onLoad() {
				// TODO Auto-generated method stub
				beacon = new Beacon.Builder()				
		        .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
		        .setId2("1")
		        .setId3(meEmployee.busAssigned.beaconId)
		        .setManufacturer(0x01FD) // Apple.  Change this for other beacon layouts
		        .setTxPower(-75)
		        .setDataFields(Arrays.asList(new Long[] {0l})) // Remove this for beacon layouts without d: fields
		        .build();
				
				// Change the layout below for other beacon types
				beaconParser = new BeaconParser()
				        .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
				beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser); 
				beaconTransmitter.startAdvertising(beacon);
				return false;
			}
		};
		
	    buildGoogleApiClient();
	    
		new NNAsyncTask(this, false) {
			
			@Override
			public boolean onPostLoad() {
				// TODO Auto-generated method stub
				
				return false;
			}
			
			@Override
			public boolean onLoad() {
				while(meEmployee == null) {
					synchronized (this) {
						try {
							this.wait(1500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
								
				buildLocationClient();
				return true;
				
			}
		}.execute();
		
		startBeacon.execute();
		
		

	

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.log_out_dialog))
               .setPositiveButton(getString(R.string.action_logOut), new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   Authorization.logout(TicketsScreen.this);
                   }
               })
               .setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                	   dialog.cancel();
                   }
               });
		dialog = builder.create();

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tickets_screen, menu);
		MenuItem mItem = menu.findItem(R.id.phone_sell);
		mPhoneSalesCount = (TextView) mItem.getActionView().findViewById(R.id.phone_sales_count);
		mPhoneSalesCount.setText(""+Authorization.getSessionPhoneSales());

		
		mBusInfo = menu.findItem(R.id.bus_info);
		
		actionBarBusInfoAdding  = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(meEmployee != null) {
					mBusInfo.setTitle("  " + meEmployee.busAssigned.line + " | " + meEmployee.busAssigned.regNumber);
				} else {
					mHandler.postDelayed(actionBarBusInfoAdding, 1000);
				}
			}
		};
		mHandler.post(actionBarBusInfoAdding);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_logOut) {
//			Authorization.logout(TicketsScreen.this);
			dialog.show();
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
				meEmployee = response.result;
				
				
				setUpActionBarMe(meEmployee);
				
			}
		};
	}
	
	
	public void setUpActionBarMe(Employee me) {
		mCircleImageView.setImageUrl(me.imageUrl,mImageLoader);
		if(!TextUtils.isEmpty(me.name)) {
			mDriverName.setText(me.name);
		}
		if(mBusInfo != null) {
			
		}
	}
	
	public void createLocationRequest() {
	      mLocationRequest = LocationRequest.create()
	              .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
	              .setInterval(INTVERVAL)
	              .setFastestInterval(FASTEST_INTERVAL);
//	              .setSmallestDisplacement(2);
	}
	
    private void startUpdateLocation() {
    	
      LocationServices.FusedLocationApi.requestLocationUpdates(
        		mGoogleApiClient, mLocationRequest, this);
	  MyLog.d(TAG, "Location update started");
    }
    
    protected void stopLocationUpdates() {
    	if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
    	}
        MyLog.d(TAG, "Location update paused");
    }

  @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		stopLocationUpdates();
		if(beaconTransmitter != null) {
			beaconTransmitter.stopAdvertising();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	    if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && mLocationRequest != null) {
	        startUpdateLocation();
        } else {
        	buildLocationClient();
        }
	    if(beaconTransmitter != null && beacon != null) {
	    	try {
	    		beaconTransmitter.startAdvertising(beacon);
	    	} catch(Exception e) {
	    		startBeacon.execute();
	    	}
	    	
	    }
	}

private GoogleApiClient buildLocationClient() {
      if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) return mGoogleApiClient;
      



//      try {
//          while (mGoogleApiClient.isConnecting()) {
//              Thread.sleep(200);
//          }
//      } catch (InterruptedException e) {
//          throw new IllegalStateException("Thread interrupted", e);
//      }
      createLocationRequest();
      mGoogleApiClient.connect();
      return mGoogleApiClient;
  }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
      mCurrentLocation = location;
      sentLocationToServer(mCurrentLocation.getLongitude(), mCurrentLocation.getLatitude());
      MyLog.i(TAG, "Long " + mCurrentLocation.getLongitude() + " Lat " + mCurrentLocation.getLatitude());
//      Toast.makeText(getApplicationContext(), "Lat - " + mCurrentLocation.getLatitude() + " Lng - " + mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();;
	}
  
	protected synchronized void buildGoogleApiClient() {
	    mGoogleApiClient = new GoogleApiClient.Builder(this)
	        .addConnectionCallbacks(this)
	        .addOnConnectionFailedListener(this)
	        .addApi(LocationServices.API)
	        .build();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
		throw new IllegalStateException("Failed connection to location manager " + connectionResult.toString());
	}

	@Override
	public void onConnected(Bundle bundle) {
		// TODO Auto-generated method stub
		startUpdateLocation();
		MyLog.d(TAG, "Location client. Connected");
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		 MyLog.d(TAG, "Location client. Suspended");
	}
	
	public void sentLocationToServer(Double longitude, Double latitude) {

		RequestQueue mRequestQueue = BaseApplication.getInstance().getRequestQueue();
		
		Type boolResponseType = new TypeToken<ServerResponse<String>>() {}.getType();
		
		String beaconId = this.getIntent().getStringExtra("beaconId");
		
		String url = String.format(Urls.UPDATE_BUS_LOCATION, meEmployee.busAssigned.beaconId);
		
		Map<String, Object> params = new HashMap<String, Object>();

		
		Double location[] = {longitude.doubleValue(),latitude.doubleValue()};
		ArrayList<String> locString = new ArrayList<String>();
		locString.add(String.valueOf(longitude));
		locString.add(String.valueOf(latitude));
		ArrayList<Double> locationArr = new ArrayList<Double>();
		locationArr.add(longitude);
		locationArr.add(latitude);
		params.put("location", locationArr);
//		params.put("longitude", longitude);
//		params.put("latitude", latitude);

		GsonRequest<ServerResponse<String>> busLocationUpdateRequest = new GsonRequest<ServerResponse<String>>(Method.POST, url, boolResponseType, params, Authorization.getAuthorizationHeader(this.getApplicationContext()), 
				new Listener<ServerResponse<String>>() {

					@Override
					public void onResponse(
							ServerResponse<String> response) {
						// TODO Auto-generated method stub
//						MyLog.i(TAG, "Location added to server successfuly");
					}}, 
					
				new TickeyError(this) {
					
					@Override
					public void onTickeyErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						MyLog.e(TAG, ""+error.getMessage());

					}
				});
		
		try {
			MyLog.i(TAG, ""+ busLocationUpdateRequest.getHeaders());
		} catch (AuthFailureError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		busLocationUpdateRequest.mContentType = "application/json";
		busLocationUpdateRequest.setPriority(Priority.NORMAL);
		mRequestQueue.add(busLocationUpdateRequest);
	
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		dialog.show();
	}
	

	public void commitPhoneSale() {
		mPhoneSalesCount.setText(""+Authorization.incrementPhoneSales());
	}
	
	// This snippet hides the system bars.
	private void hideSystemUI() {
	    // Set the IMMERSIVE flag.
	    // Set the content to appear under the system bars so that the content
	    // doesn't resize when the system bars hide and show.
	    getWindow().getDecorView().setSystemUiVisibility(
	                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	              | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	              | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	              | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
	              | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
	              | View.SYSTEM_UI_FLAG_IMMERSIVE);
	}

	// This snippet shows the system bars. It does this by removing all the flags
	// except for the ones that make the content appear under the system bars.
	private void showSystemUI() {
		getWindow().getDecorView().setSystemUiVisibility(
	               View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	             | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	             | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
	    if (hasFocus) {
	        getWindow().getDecorView().setSystemUiVisibility(
	                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
	                | View.SYSTEM_UI_FLAG_FULLSCREEN
	                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
	}
}
