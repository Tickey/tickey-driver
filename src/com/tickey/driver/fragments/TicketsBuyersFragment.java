package com.tickey.driver.fragments;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tickey.driver.R;
import com.tickey.driver.data.model.User;
import com.tickey.driver.gcm.GcmPreferences;
import com.tickey.driver.utility.MyLog;
import com.tickey.driver.view.adapter.TicketsBuyersRecycleAdapter;
import com.tickey.driver.view.custom.DividerItemDecoration;

public class TicketsBuyersFragment extends Fragment{

	private static final String TAG = TicketsBuyersFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private BroadcastReceiver mTicketsBuyingReceiver;
    private ArrayList<User> mBuyersDataSet;
    private LinearLayout layout;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mBuyersDataSet = new ArrayList<User>();
		mTicketsBuyingReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
			

				User newUser = new Gson().fromJson(intent.getStringExtra("buyerObject"), User.class);
				if(newUser != null) {
					if(mBuyersDataSet.size() == 0) {
						mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
						layout.removeAllViews();
						layout.addView(mRecyclerView);
					}
					mBuyersDataSet.add(0, newUser);
					mRecyclerView.invalidate();
					mAdapter.notifyItemInserted(0);
					
//					mAdapter.notifyDataSetChanged();
					mRecyclerView.scrollToPosition(0);
				}

				

			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		layout = (LinearLayout) inflater.inflate(R.layout.fragment_tickets_buyers, container, false);
		
		mRecyclerView = (RecyclerView) layout.findViewById(R.id.rv_ticketsBuyers);
		mRecyclerView.setHasFixedSize(true);
		LinearLayout emptyList = (LinearLayout) inflater.inflate(R.layout.empty_byers_list, layout, false);
		
		if(mBuyersDataSet.size() == 0) {
			layout.removeAllViews();
//			mRecyclerView.setVisibility(View.INVISIBLE);
			layout.addView(emptyList);
		}
	
		

		
		mLayoutManager = new LinearLayoutManager(getActivity());
		mAdapter = new TicketsBuyersRecycleAdapter(getActivity(), mBuyersDataSet);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setAdapter(mAdapter);
		
		return layout;
	}



	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mTicketsBuyingReceiver,
                new IntentFilter(GcmPreferences.TYPE_TICKET));
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mTicketsBuyingReceiver);
        super.onPause();
	}
	
	
}
