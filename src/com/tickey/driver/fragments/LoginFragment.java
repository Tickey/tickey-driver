package com.tickey.driver.fragments;



import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.tickey.driver.R;
import com.tickey.driver.callback.AuthorizationCallbacks;
import com.tickey.driver.data.model.Employee;
import com.tickey.driver.data.model.ServerResponse;
import com.tickey.driver.gcm.GcmPreferences;
import com.tickey.driver.gcm.RegistrationIntentService;
import com.tickey.driver.screens.LoginScreen;
import com.tickey.driver.screens.VehicleNumberScreen;
import com.tickey.driver.utility.Authorization;
import com.tickey.driver.utility.Hardware;
import com.tickey.driver.utility.MyLog;


public class LoginFragment extends Fragment{

	private static String TAG = LoginFragment.class.getSimpleName();
	private Intent mNextIntent = null;
	private boolean mIsStarted = false;
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private Button loginButton;
	private TextView userName;
	private TextView password;
	
	private GoogleCloudMessaging gcm;
	private BroadcastReceiver mRegistrationBroadcastReceiver;
	private ProgressDialog loadingDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        // Inflate the layout for this fragment
		View content = inflater.inflate(R.layout.fragment_login_screen, container, false);
		
		userName = (TextView) content.findViewById(R.id.userNameET);
		password = (TextView) content.findViewById(R.id.passwordET);
//		userName.setText("nes"); password.setText("nes");
		
		loadingDialog = new ProgressDialog(getActivity());
		loadingDialog.setCancelable(false);
		loadingDialog.setCanceledOnTouchOutside(false);
		loadingDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
			}
		});
		loadingDialog.setMessage("Loading...");


        
		loginButton = (Button) content.findViewById(R.id.loginBtn);
		loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loginButton.setClickable(false);

		        if (checkPlayServices()) {
		            // Start IntentService to register this application with GCM.
		        	loadingDialog.show();
		            Intent intent = new Intent(getActivity(), RegistrationIntentService.class);
		            getActivity().startService(intent);
		            
		        } else {
					MyLog.i(TAG, "No valid Google Play Services APK found.");
				}

			}
		});
		
		password.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        boolean handled = false;
		        if (actionId == EditorInfo.IME_ACTION_SEND) {
		            loginButton.callOnClick();
		            handled = true;
		        }
		        return handled;
			}
		});
		
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            	
            	boolean sentTokenToServer = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(GcmPreferences.SENT_TOKEN_TO_SERVER, false);	
            			
            	if(sentTokenToServer) {
                	String token = intent.getStringExtra("token");
                	initializeLogin(token);
            	} else {
            		Toast.makeText(context, context.getResources().getString(R.string.token_registration_error), Toast.LENGTH_SHORT).show();
            		loginButton.setClickable(true);
            	}
            	

            }
        };
        
        return content;
	}

	
	private void initializeLogin(String registerId) {
		
		new Authorization().login(getActivity().getApplicationContext(), userName.getText().toString(), password.getText().toString(), registerId,
				Hardware.getDeviceName(), Hardware
						.getDeviceId(getActivity().getApplication()), mAuthCallbacks);
		
	}
	
	private AuthorizationCallbacks mAuthCallbacks = new AuthorizationCallbacks() {

		@Override
		public void preExecute() {

		}

		@Override
		public void onResponse(ServerResponse<Employee> response) {
			Gson gson = new Gson();
			loggedIn(gson.toJson(response.result));
		}

		@Override
		public void onErrorResponse(VolleyError error) {
			notLoggedIn();
			
		}
	};

	private void loggedIn(String json) {

		mNextIntent = new Intent(getActivity().getApplicationContext(),
				VehicleNumberScreen.class);

		mNextIntent.putExtra(Employee.class.getSimpleName(), json);

		splashScreen();
		
	}

	private void notLoggedIn() {
		loadingDialog.dismiss();
		mNextIntent = new Intent(getActivity().getApplicationContext(), LoginScreen.class);
//
//		splashScreen();

		
		Toast.makeText(getActivity(), "Cant be logged", Toast.LENGTH_SHORT).show();
		loginButton.setClickable(true);

	}


	private void splashScreen() {

		if (!mIsStarted) {
			mIsStarted = true;
			getActivity().finish();
			mNextIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION
					| Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(mNextIntent);
		}
	}
	
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                getActivity().finish();
            }
            return false;
        }
        return true;
    }

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GcmPreferences.REGISTRATION_COMPLETE));
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
	}
    
	
}
