package com.tickey.app.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.tickey.R;
import com.tickey.app.callback.AuthorizationCallbacks;
import com.tickey.app.data.model.Employee;
import com.tickey.app.data.model.ServerResponse;
import com.tickey.app.data.model.User;
import com.tickey.app.screens.TicketsScreen;
import com.tickey.app.screens.VehicleNumberScreen;
import com.tickey.app.utility.Authorization;
import com.tickey.app.utility.Google;
import com.tickey.app.utility.Google.RegisterGCMIdCallbacks;
import com.tickey.app.utility.Hardware;
import com.tickey.app.utility.MyLog;


public class LoginFragment extends Fragment{

	private static String TAG = LoginFragment.class.getSimpleName();
	private Intent mNextIntent = null;
	private boolean mIsStarted = false;
	
	private Button loginButton;
	private TextView userName;
	private TextView password;
	
	private GoogleCloudMessaging gcm;

	
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
		userName.setText("nes"); password.setText("nes");
		
		loginButton = (Button) content.findViewById(R.id.loginBtn);
		loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
//				InstanceID instanceID = InstanceID.getInstance(this);
//				String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
//				        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
//				MyLog.i(TAG, "GCM Registration Token: " + token);
				
				// TODO Auto-generated method stub
				// Check device for Play Services APK. If check succeeds, proceed with
				// GCM registration.
				if (Google.checkPlayServices(getActivity())) {
					MyLog.v(TAG, "Google Play Services exist");
					gcm = GoogleCloudMessaging.getInstance(getActivity());
					String registerId = Google
							.getRegistrationId(getActivity().getApplicationContext());

					if (TextUtils.isEmpty(registerId)) {
						MyLog.v(TAG, "regId is not empty");
						Google.registerInBackground(getActivity(), new RegisterGCMIdCallbacks() {

							@Override
							public void idRegistered(String registerId) {

								MyLog.v(TAG, "regId is registered");

								initializeLogin(registerId);
							}
						});
					} else {
							
						initializeLogin(registerId);
					}
				} else {
					MyLog.i(TAG, "No valid Google Play Services APK found.");
				}
			}
		});
		
        return content;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
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

		mNextIntent = new Intent(getActivity().getApplicationContext(), VehicleNumberScreen.class);
//
		splashScreen();
		
		Toast.makeText(getActivity(), "Cant be logged", Toast.LENGTH_SHORT).show();

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
}
