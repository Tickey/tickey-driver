package com.tickey.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tickey.R;
import com.tickey.app.view.adapter.TicketsBuyersRecycleAdapter;
import com.tickey.app.view.custom.DividerItemDecoration;

public class TicketsBuyersFragment extends Fragment{

	
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View layout = inflater.inflate(R.layout.fragment_tickets_buyers, container, false);
		
		mRecyclerView = (RecyclerView) layout.findViewById(R.id.rv_ticketsBuyers);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
		
		mLayoutManager = new LinearLayoutManager(getActivity());

		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setAdapter(new TicketsBuyersRecycleAdapter(getActivity(), null));
		
		return layout;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	
	
}
