package com.tickey.driver.view.adapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.tickey.driver.R;
import com.tickey.driver.common.BaseApplication;
import com.tickey.driver.data.model.Bus;
import com.tickey.driver.data.model.ServerResponse;
import com.tickey.driver.network.callback.TickeyError;
import com.tickey.driver.network.helper.GsonRequest;
import com.tickey.driver.network.helper.Urls;
import com.tickey.driver.screens.TicketsScreen;
import com.tickey.driver.screens.VehicleNumberScreen;
import com.tickey.driver.utility.Authorization;
import com.tickey.driver.utility.MyLog;

public class VehicleLinesRecycleAdapter extends RecyclerView.Adapter<VehicleLinesRecycleAdapter.VehicleTypeViewHolder>{
	
	private static final String TAG = VehicleLinesRecycleAdapter.class.getSimpleName();
	
	private ArrayList<String> mDataSet;
	private LayoutInflater inflater;
	
	private SparseBooleanArray selectedItems;
	private VehicleTypeViewHolder mSelectedHolder;
	private Activity instance;
	private String selectedLine;
	private int selectedItem = -1;
	private ProgressDialog loadingDialog;	
	
	public VehicleLinesRecycleAdapter(Context context, ArrayList<String> dataSet) {
		inflater = LayoutInflater.from(context);
		instance = (Activity) context;
		mDataSet = dataSet;
		selectedItems = new SparseBooleanArray();
		loadingDialog = new ProgressDialog(instance);
		loadingDialog.setCancelable(false);
		loadingDialog.setCanceledOnTouchOutside(false);
		loadingDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	public class VehicleTypeViewHolder extends RecyclerView.ViewHolder{

		public TextView mTextView;
		public View theView;
		public int position;
		
		public VehicleTypeViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			
			theView = itemView;
			mTextView = (TextView) itemView.findViewById(R.id.tv_vehicleLineNumber);
			itemView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					selectedLine = mDataSet.get(position);
					v.setSelected(true);
					mSelectedHolder = VehicleTypeViewHolder.this;
					mSelectedHolder.theView.setClickable(false);
					startTicketScreen();
				}
			});
		}

		
	}

	
	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return super.getItemId(position);
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return mDataSet.size();
	}

	@Override
	public void onBindViewHolder(VehicleTypeViewHolder holder, int position) {
		// TODO Auto-generated method stub

		holder.position = position;
		holder.mTextView.setText(mDataSet.get(position));
		
		

	}

	@Override
	public VehicleTypeViewHolder onCreateViewHolder(ViewGroup parent, int position) {
		// TODO Auto-generated method stub
		final View view = inflater.inflate(R.layout.vehicle_line_number_layout, parent, false);
		VehicleTypeViewHolder holder = new VehicleTypeViewHolder(view);
//		MyLog.i("test", "Selected Position " + selectedItem + " Position " + position);
		return holder;
	}
	
	public void startTicketScreen() {
		loadingDialog.show();
		RequestQueue mRequestQueue = BaseApplication.getInstance().getRequestQueue();
		
		Type beaconReposneType = new TypeToken<ServerResponse<Bus>>() {}.getType();
		
		String vehicleNumber = instance.getIntent().getStringExtra(VehicleNumberScreen.VEHICLE_NUMBER);
		
		String url = String.format(Urls.POST_REG_LINE, selectedLine);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("regNumber", vehicleNumber);

		GsonRequest<ServerResponse<Bus>> beaconRequest = new GsonRequest<ServerResponse<Bus>>(Method.POST, url, beaconReposneType, params, Authorization.getAuthorizationHeader(instance.getApplicationContext()), 
				new Listener<ServerResponse<Bus>>() {

					@Override
					public void onResponse(
							ServerResponse<Bus> response) {
						// TODO Auto-generated method stub
						loadingDialog.dismiss();						
						Bus mBus = response.result;
						
						Intent mNextIntent = new Intent(instance, TicketsScreen.class);
						instance.finish();
						
						mNextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_SINGLE_TOP);
						
						
						mNextIntent.putExtra("lineNumber", selectedLine);
						mNextIntent.putExtra("beaconId", mBus.beaconId);
						
						instance.startActivity(mNextIntent);
					}}, 
					
				new TickeyError(instance) {
					
					@Override
					public void onTickeyErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						MyLog.e(TAG, ""+error.getMessage());
						loadingDialog.dismiss();
						Toast.makeText(instance, instance.getResources().getString(R.string.line_selection_error), Toast.LENGTH_SHORT).show();
						mSelectedHolder.theView.setClickable(true);
						mSelectedHolder.theView.setSelected(false);
					}
				});
		
		try {
			MyLog.i(TAG, ""+ beaconRequest.getHeaders());
		} catch (AuthFailureError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		beaconRequest.mContentType = "application/json";
		mRequestQueue.add(beaconRequest);
	}

}
