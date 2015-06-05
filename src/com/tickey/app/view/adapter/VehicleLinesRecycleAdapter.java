package com.tickey.app.view.adapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.tickey.R;
import com.tickey.app.common.BaseApplication;
import com.tickey.app.data.model.Beacon;
import com.tickey.app.data.model.ServerResponse;
import com.tickey.app.network.callback.TickeyError;
import com.tickey.app.network.helper.GsonRequest;
import com.tickey.app.network.helper.Urls;
import com.tickey.app.screens.TicketsScreen;
import com.tickey.app.screens.VehicleNumberScreen;
import com.tickey.app.utility.Authorization;
import com.tickey.app.utility.MyLog;

public class VehicleLinesRecycleAdapter extends RecyclerView.Adapter<VehicleLinesRecycleAdapter.VehicleTypeViewHolder>{
	
	private static final String TAG = VehicleLinesRecycleAdapter.class.getSimpleName();
	
	private ArrayList<String> mDataSet;
	private LayoutInflater inflater;
	
	private SparseBooleanArray selectedItems;
	private VehicleTypeViewHolder mSelectedHolder;
	private Activity instance;
	private String selectedLine;
	private int selectedItem = -1;
	
	public VehicleLinesRecycleAdapter(Context context, ArrayList<String> dataSet) {
		inflater = LayoutInflater.from(context);
		instance = (Activity) context;
		mDataSet = dataSet;
		selectedItems = new SparseBooleanArray();
		
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
					startTicketScreen();
//					startTicketScreen();
					// TODO Auto-generated method stuff
//					if(selectedItem == position) {
//						selectedItem = -1;
//						theView.setSelected(false);
//					} else {
//		            	if(mSelectedHolder != null) {
//		            		mSelectedHolder.theView.setSelected(false);
//		            		mSelectedHolder = VehicleTypeViewHolder.this;
//		            	}
//						selectedItem = position;
//						theView.setSelected(true);
//					}
					
//		            if (selectedItems.get(position, false)) {
//		                selectedItems.delete(position);
//		                theView.setSelected(false);
//		                
//		            }
//		            else {
//		            	if(mSelectedHolder != null) {
//		            		mSelectedHolder.theView.setSelected(false);
//		            	}
//		            	selectedItems.clear();
//		                selectedItems.put(position, true);
//		                theView.setSelected(true);
//		                mSelectedHolder = VehicleTypeViewHolder.this;
//		                
//		                
//		            }
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
//		MyLog.i("test", "mDataSet: " + mDataSet + " Position " + position + "itme " + mDataSet.get(position));
		holder.position = position;
		holder.mTextView.setText(mDataSet.get(position));
		
		
//		holder.mTextView.setText("" + position);
//		holder.position = position;
//		if(!selectedItems.get(position, false)) {
//			holder.theView.setSelected(false);
//		} else {
//			holder.theView.setSelected(true);
//		}
//		if(selectedItem != position) {
//			holder.theView.setSelected(false);
//		} else {
//			holder.theView.setSelected(true);
//		}
//		MyLog.v("test", "Selected Position " + selectedItem + " Position " + position);
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
		RequestQueue mRequestQueue = BaseApplication.getInstance().getRequestQueue();
		
		Type beaconReposneType = new TypeToken<ServerResponse<String>>() {}.getType();
		
		String vehicleNumber = instance.getIntent().getStringExtra(VehicleNumberScreen.VEHICLE_NUMBER);
		
		String url = String.format(Urls.POST_REG_LINE, selectedLine);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("regNumber", vehicleNumber);
		
		GsonRequest<ServerResponse<String>> beaconRequest = new GsonRequest<ServerResponse<String>>(Method.POST, url, beaconReposneType, params, Authorization.getAuthorizationHeader(instance.getApplicationContext()), 
				new Listener<ServerResponse<String>>() {

					@Override
					public void onResponse(
							ServerResponse<String> response) {
						// TODO Auto-generated method stub
						Intent mNextIntent = new Intent(instance, TicketsScreen.class);
						instance.finish();
						
						mNextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_SINGLE_TOP);
						
//						mNextIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION
//								| Intent.FLAG_ACTIVITY_CLEAR_TOP
//								| Intent.FLAG_ACTIVITY_SINGLE_TOP);
						
						mNextIntent.putExtra("lineNumber", selectedLine);
						
						instance.startActivity(mNextIntent);
					}}, 
					
				new TickeyError(instance) {
					
					@Override
					public void onTickeyErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
//						MyLog.e(TAG, error.getMessage());
					}
				});
		
		try {
			MyLog.i(TAG, ""+ beaconRequest.getHeaders());
		} catch (AuthFailureError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mRequestQueue.add(beaconRequest);
	}

}
