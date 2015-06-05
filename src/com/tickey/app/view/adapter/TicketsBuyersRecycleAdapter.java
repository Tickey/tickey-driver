package com.tickey.app.view.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.tickey.R;
import com.tickey.app.common.BaseApplication;
import com.tickey.app.view.custom.CircleNetworkImageView;

public class TicketsBuyersRecycleAdapter extends RecyclerView.Adapter<TicketsBuyersRecycleAdapter.TicketsBuyerViewHolder>{
	
	private ArrayList<String> mDataSet;
	private LayoutInflater inflater;
	
	private SparseBooleanArray selectedItems;
	private TicketsBuyerViewHolder mSelectedHolder;
	private Activity instance;
	private String selectedLine;
	private int selectedItem = -1;
	private ImageLoader mImageLoader = BaseApplication.getInstance()
			.getImageLoader();
	
	public TicketsBuyersRecycleAdapter(Context context, ArrayList<String> dataSet) {
		inflater = LayoutInflater.from(context);
		instance = (Activity) context;
		mDataSet = dataSet;
		selectedItems = new SparseBooleanArray();
		
	}
	
	public class TicketsBuyerViewHolder extends RecyclerView.ViewHolder{

		public TextView mTextView;
		public CircleNetworkImageView buyerAvatar;
		public View theView;
		public int position;
		
		public TicketsBuyerViewHolder(View itemView) {
			super(itemView);
			
			buyerAvatar = (CircleNetworkImageView) itemView.findViewById(R.id.niv_buyers_avatar);
			buyerAvatar.setImageUrl("https://scontent-fra3-1.xx.fbcdn.net/hphotos-xpf1/v/t1.0-9/10448240_10203044565529675_8771187453303547074_n.jpg?oh=98a5a30aa4401af5202eece20e04d3f3&oe=55F432A0", mImageLoader);
			mTextView = (TextView) itemView.findViewById(R.id.tv_buyers_name);
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
		return 10;
	}

	@Override
	public void onBindViewHolder(TicketsBuyerViewHolder holder, int position) {
		// TODO Auto-generated method stub
		
		holder.position = position;
		holder.mTextView.setText("FirstName LastName" + position);
		

	}

	@Override
	public TicketsBuyerViewHolder onCreateViewHolder(ViewGroup parent, int position) {
		// TODO Auto-generated method stub
		View layout = inflater.inflate(R.layout.tickets_buyer_layout, parent, false);
		
		layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		
		TicketsBuyerViewHolder holder = new TicketsBuyerViewHolder(layout);

		return holder;
	}
	


}
