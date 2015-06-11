package com.tickey.driver.fragments;

import java.util.ArrayList;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tickey.driver.R;
import com.tickey.driver.utility.MyLog;
import com.tickey.driver.view.adapter.VehicleLinesRecycleAdapter;

public class VehicleTypeFragment extends Fragment{

	private ArrayList<String> lines;
	private TextView linesTV;
	
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
	
	public static VehicleTypeFragment getInstance(ArrayList<String> lines) {

		Bundle args = new Bundle();
		args.putStringArrayList("lines", lines);
		VehicleTypeFragment fragment = new VehicleTypeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lines = getArguments().getStringArrayList("lines");
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		View layout = inflater.inflate(R.layout.fragment_vehicle_type, container, false);
		linesTV = (TextView) layout.findViewById(R.id.linesTV);
		
		mRecyclerView = (RecyclerView) layout.findViewById(R.id.vehichleType);
		mRecyclerView.setHasFixedSize(true);
		
		mLayoutManager = new GridLayoutManager(getActivity(), 5);
		mRecyclerView.setLayoutManager(mLayoutManager);

		mRecyclerView.addItemDecoration(new SpacesItemDecoration(40));
		mAdapter = new VehicleLinesRecycleAdapter(getActivity(), lines);
		mAdapter.setHasStableIds(true);
		

		
		mRecyclerView.setAdapter(mAdapter);

		MyLog.i("", "" + mLayoutManager.getChildAt(0));
		
		
		return layout;
	}


	public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
		  private int space;
		  private int top = 0, bottom = 0, left = 0, right = 0;
		  public SpacesItemDecoration(int space) {
		    this.space = space;
		  }
		  public SpacesItemDecoration(int top, int bottom, int left, int right) {
			    this.top = top;
			    this.bottom = bottom;
			    this.left = left;
			    this.right = right;
		  }
		  
		  @Override
		  public void getItemOffsets(Rect outRect, View view, 
		      RecyclerView parent, RecyclerView.State state) {
			outRect.top = (this.top != 0)  ? this.top : 0;
		    outRect.left = (this.left != 0)  ? this.left : space;
		    outRect.right = (this.right != 0)  ? this.right : space;
		    outRect.bottom = (this.bottom != 0)  ? this.bottom : space;

		    // Add top margin only for the first item to avoid double space between items
//		    if(parent.getChildPosition(view) == 0)
//		        outRect.top = space;
		  }
		}
	
}
