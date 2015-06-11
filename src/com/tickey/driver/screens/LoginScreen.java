package com.tickey.driver.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.tickey.driver.R;
import com.tickey.driver.common.BaseApplication;
import com.tickey.driver.utility.Authorization;
import com.tickey.driver.utility.Me;

public class LoginScreen extends FragmentActivity {

	private Me me = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//		getActionBar().hide();
		final Handler mHandler = new Handler();
		final Runnable run = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setContentView(R.layout.activity_login_screen);
			}
		};
		setContentView(R.layout.activity_spash_screen);
		if(!getIntent().getBooleanExtra("loggedOut", false)) {
			if(Authorization.isLoggedIn(getApplicationContext())) {
				Toast.makeText(getApplicationContext(), "is loged", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(this, TicketsScreen.class);
				
				me = new Me(this,BaseApplication.getInstance().getRequestQueue()) {
					
					@Override
					public void response() {
						// TODO Auto-generated method stub
						if(me.getMeEmployee().busAssigned != null) {
							final Intent intent = new Intent(LoginScreen.this, TicketsScreen.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_SINGLE_TOP);
							
							intent.putExtra(Me.class.getSimpleName(), this.getJsonMe());
							
							mHandler.postDelayed(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									LoginScreen.this.startActivity(intent);
								}
							}, 1000);
						} else {

							setContentView(R.layout.activity_login_screen );
							Toast.makeText(LoginScreen.this, getString(R.string.no_buss_assigned), Toast.LENGTH_LONG).show();
						}
						
					}
					
					@Override
					public void error() {
						// TODO Auto-generated method stub
						
						mHandler.postDelayed(run, 1000);
						
					}
				};
				
				me.getMe();
			} else {
				Toast.makeText(getApplicationContext(), "is not loged", Toast.LENGTH_LONG).show();
				mHandler.postDelayed(run, 1000);
			}
		} else {
			setContentView(R.layout.activity_login_screen);
		}

		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
