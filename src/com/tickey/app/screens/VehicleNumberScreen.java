package com.tickey.app.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tickey.R;

public class VehicleNumberScreen extends Activity {

	private TextView done;
	private EditText vehicleNumber;
	public static String VEHICLE_NUMBER = "VEHICLE_NUMBER";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vehicle_number_screen);
		
		vehicleNumber = (EditText) findViewById(R.id.vehicleNumberET);
		
		done = (TextView) findViewById(R.id.doneTV);
		
		done.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(vehicleNumber.getText().toString())) {
					Toast.makeText(VehicleNumberScreen.this, getResources().getString(R.string.not_empty), Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(VehicleNumberScreen.this, VehicleTypeScreen.class);
					intent.putExtra(VEHICLE_NUMBER, vehicleNumber.getText().toString());
					VehicleNumberScreen.this.startActivity(intent);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.vehicle_number_screen, menu);
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
