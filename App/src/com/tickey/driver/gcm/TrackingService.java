package com.tickey.driver.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.tickey.driver.utility.MyLog;
import com.tickey.driver.utility.NNAsyncTask;

public class TrackingService extends IntentService implements LocationListener, ConnectionCallbacks, OnConnectionFailedListener{

	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	
	private Location mCurrentLocation;
	private static final String TAG = TrackingService.class.getSimpleName();
	
	
	private static int FASTEST_INTERVAL = 1000;
	private static int INTVERVAL = 2000;
	
	public TrackingService() {
		super(TrackingService.class.getSimpleName());
		// TODO Auto-generated constructor stub

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		buildLocationClient();
		startUpdateLocation(intent);

	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

	    return super.onStartCommand(intent,flags,startId);
	}
	@Override
	public void onDestroy() {
		//Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
	}	
	
    private void startUpdateLocation(Intent intent) {
//        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),
//            0, intent, 0);
        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(INTVERVAL)
            .setFastestInterval(FASTEST_INTERVAL)
            .setSmallestDisplacement(2);

        LocationServices.FusedLocationApi.requestLocationUpdates(
        		mGoogleApiClient, mLocationRequest, this);
        MyLog.d(TAG, "Location update started");
    }

    private GoogleApiClient buildLocationClient() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) return mGoogleApiClient;
        
        buildGoogleApiClient();

        mGoogleApiClient.connect();
        try {
            while (mGoogleApiClient.isConnecting()) {
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException("Thread interrupted", e);
        }
        return mGoogleApiClient;
    }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
        mCurrentLocation = location;
        MyLog.i(TAG, "Long " + mCurrentLocation.getLongitude() + " Lat " + mCurrentLocation.getLatitude());
        Toast.makeText(getApplicationContext(), "Lat - " + mCurrentLocation.getLatitude() + " Lng - " + mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();;
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
		MyLog.d(TAG, "Location client. Connected");
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		 MyLog.d(TAG, "Location client. Suspended");
	}

	
}
