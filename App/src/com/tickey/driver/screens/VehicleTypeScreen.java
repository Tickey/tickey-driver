package com.tickey.driver.screens;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Request.Priority;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.reflect.TypeToken;
import com.tickey.driver.R;
import com.tickey.driver.common.BaseApplication;
import com.tickey.driver.data.model.Employee;
import com.tickey.driver.data.model.Line;
import com.tickey.driver.data.model.ServerResponse;
import com.tickey.driver.network.callback.TickeyError;
import com.tickey.driver.network.helper.GsonRequest;
import com.tickey.driver.network.helper.Urls;
import com.tickey.driver.utility.Authorization;
import com.tickey.driver.utility.MyLog;
import com.tickey.driver.view.adapter.VehicleTypeAdapter;
import com.tickey.driver.view.custom.CircleNetworkImageView;
import com.tickey.driver.view.custom.SlidingTabLayout;

public class VehicleTypeScreen extends AppCompatActivity implements BeaconConsumer ,RangeNotifier{ 

	public static final String TAG = VehicleTypeScreen.class.getSimpleName();
	 public static final Region REGION_BUS_ONEDOOR = new Region(
			   "TICKEY Bus One Door",
			   Identifier.parse("b5e343ee-0a98-11e5-a6c0-1697f925ec7b"), null,
			   null);
	private SlidingTabLayout mTabs;
	private ViewPager mPager;
	private VehicleTypeAdapter typeAdapter;
	protected Toolbar mToolbar;
	private Menu menu;

	private CircleNetworkImageView mCircleImageView;
	private TextView mDriverName;
	
	private ArrayList<String> types;
	
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader = BaseApplication.getInstance()
			.getImageLoader();
	
	private Runnable isBeaconFound;
	
	private HashMap<String, ArrayList<String>> linesMap;
	
	private Beacon driverBeacon;
	
	private BeaconManager mBeaconManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vehicle_type_screen);
		
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
			// Device does not support Bluetooth
			mBluetoothAdapter.enable();
		}
		mBeaconManager = BeaconManager.getInstanceForApplication(VehicleTypeScreen.this);
		mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

		mBeaconManager.bind(this);
        Region region = new Region("com.example.myapp.boostrapRegion", null, null, null);
		
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
		}
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		



		mCircleImageView = (CircleNetworkImageView) findViewById(R.id.profileAvatar);
		mDriverName = (TextView) findViewById(R.id.tv_driverNmae);
		
		
		
		mRequestQueue = BaseApplication.getInstance().getRequestQueue();
		
		
		
		linesMap = new HashMap<String, ArrayList<String>>();
		
		Type linesResponseType = new TypeToken<ServerResponse<ArrayList<Line>>>() {}.getType();
		
		GsonRequest<ServerResponse<ArrayList<Line>>> linesRequest = new GsonRequest<ServerResponse<ArrayList<Line>>>(Method.GET, Urls.LINES, linesResponseType, null, Authorization.getAuthorizationHeader(getApplicationContext()), 
				new Listener<ServerResponse<ArrayList<Line>>>() {

					@Override
					public void onResponse(
							ServerResponse<ArrayList<Line>> response) {
						// TODO Auto-generated method stub
						ArrayList<Line> lines = response.result;
						types = new ArrayList<String>();
						
						for(Line line : lines ) {

							if(linesMap.get(line.type) == null) {
								linesMap.put(line.type, new ArrayList<String>());
								types.add(line.type);
							}
							linesMap.get(line.type).add(line.name);

							
						}
						
						MyLog.i(TAG, ""+linesMap + " lines types" + types);
						
						prepareFragments();
					}}, 
					
				new TickeyError(VehicleTypeScreen.this) {
					
					@Override
					public void onTickeyErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						
					}
				});
		linesRequest.setPriority(Priority.IMMEDIATE);
		mRequestQueue.add(linesRequest);
		
		getMe();
		
		final ProgressDialog dialog = new ProgressDialog(VehicleTypeScreen.this);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		
		dialog.setMessage("Waiting for beacon");
		dialog.show();
		final Handler mHandler = new Handler();
		
		isBeaconFound = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(driverBeacon != null) {
					dialog.cancel();
					MyLog.i(TAG, "Beacon found");
				} else {
					mHandler.postDelayed(isBeaconFound, 1500);
				}
			}
		};
		
		mHandler.post(isBeaconFound);
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.menu = menu;
		
		
		getMenuInflater().inflate(R.menu.vehicle_type_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_logOut) {
			Authorization.logout(VehicleTypeScreen.this);
		}
		return super.onOptionsItemSelected(item);
	}
	

	
	public void prepareFragments( ) {
		
		mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
		mTabs.setDistributeEvenly(true);
		mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }
        });
		mPager = (ViewPager) findViewById(R.id.viewPager);
		
		typeAdapter = new VehicleTypeAdapter(getSupportFragmentManager());
		typeAdapter.setSize(linesMap.size());
		typeAdapter.setLinesMap(linesMap);
		typeAdapter.setTypes(types);
		
		mPager.setAdapter(typeAdapter);
		mTabs.setViewPager(mPager);
		
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
				
				
				mCircleImageView.setImageUrl(employee.imageUrl,mImageLoader);
				if(!TextUtils.isEmpty(employee.name)) {
					mDriverName.setText(employee.name);
				}
				
			}
		};
	}



	@Override
	public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region arg1) {
		// TODO Auto-generated method stub

	    for (Beacon beacon: beacons) {
	    	if(beacon != null && driverBeacon == null) {
		    	driverBeacon = beacon;
		    	MyLog.i(TAG, "ID 1 - " + beacon.getId1() + " ID 3 - " + beacon.getId3());
		    	mBeaconManager.unbind(this);
	    	}

	    }
	}



	@Override
	public void onBeaconServiceConnect() {
		// TODO Auto-generated method stub
    	Region region = new Region("myMonitoringUniqueId", null, null, null);

       
        mBeaconManager.setRangeNotifier(this);
	    
        mBeaconManager.setMonitorNotifier(new MonitorNotifier() {
        @Override
        public void didEnterRegion(Region region) {
            MyLog.i(TAG, "I just saw an beacon for the first time!");        
        }

        @Override
        public void didExitRegion(Region region) {
        	MyLog.i(TAG, "I no longer see an beacon");
        }

        @Override
            public void didDetermineStateForRegion(int state, Region region) {
        	MyLog.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);        
            }
        });
	    try {
	        mBeaconManager.startRangingBeaconsInRegion(REGION_BUS_ONEDOOR);
	    } catch (RemoteException e) {
	        e.printStackTrace();
	    }
	    
	}
	
	public Beacon getDriverBeacon() {
		return this.driverBeacon;
	}

}