package com.tickey.driver.view.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.tickey.driver.R;
import com.tickey.driver.common.BaseApplication;
import com.tickey.driver.data.model.User;
import com.tickey.driver.utility.MyLog;
import com.tickey.driver.view.custom.CircleNetworkImageView;

public class TicketsBuyersRecycleAdapter extends RecyclerView.Adapter<TicketsBuyersRecycleAdapter.TicketsBuyerViewHolder>{
	
	private ArrayList<User> mDataSet;
	private LayoutInflater inflater;
	
	private SparseBooleanArray selectedItems;
	private TicketsBuyerViewHolder mSelectedHolder;
	private Activity instance;
	private String selectedLine;
	private int selectedItem = -1;
	private ImageLoader mImageLoader = BaseApplication.getInstance()
			.getImageLoader();
	private int lastPosition = -1;
	
	public TicketsBuyersRecycleAdapter(Context context, ArrayList<User> dataSet) {
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
		public ImageView emptyList;
		
		public TicketsBuyerViewHolder(View itemView) {
			super(itemView);
//			if(mDataSet.size() > 0) {
				buyerAvatar = (CircleNetworkImageView) itemView.findViewById(R.id.niv_buyers_avatar);
				
				mTextView = (TextView) itemView.findViewById(R.id.tv_buyers_name);
//			}
		}

		
	}

	
	

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return mDataSet.size();
		
	}

	@Override
	public void onBindViewHolder(TicketsBuyerViewHolder holder, int position) {
		// TODO Auto-generated method stub
//		if(mDataSet.size() > 0) {
			User buyer = mDataSet.get(position);
			holder.position = position;
			holder.mTextView.setText(buyer.fullName);
			if(buyer.imageUrl != null && !buyer.imageUrl.equals("")) {
				holder.buyerAvatar.setImageUrl(buyer.imageUrl, mImageLoader);
			} else {
				holder.buyerAvatar.setImageUrl("https://31.media.tumblr.com/avatar_48fd47f91171_128.png", mImageLoader);
			}
//		} 

		
		
	}

	@Override
	public TicketsBuyerViewHolder onCreateViewHolder(ViewGroup parent, int position) {
		// TODO Auto-generated method stub
		MyLog.i("tag", "parent heigth " + parent.getMeasuredHeight() / 4);
		TicketsBuyerViewHolder holder;
//		if(mDataSet.size() > 0) {
			View layout = inflater.inflate(R.layout.tickets_buyer_layout, parent, false);
			
			layout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

				}
			});
			
			holder = new TicketsBuyerViewHolder(layout);
//		} else {
//			View layout = inflater.inflate(R.layout.empty_byers_list, parent, false);
//			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//			params.setMargins(0, parent.getMeasuredHeight() / 4, 0, 0);
//			layout.setLayoutParams(params);
//			holder = new TicketsBuyerViewHolder(layout);
//		}

		return holder;
	}
	
    

}
