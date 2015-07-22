package com.tickey.driver.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.tickey.driver.R;
import com.tickey.driver.common.BaseApplication;
import com.tickey.driver.data.model.User;
import com.tickey.driver.interfaces.ItemTouchHelperAdapter;
import com.tickey.driver.screens.TicketsScreen;
import com.tickey.driver.utility.Authorization;
import com.tickey.driver.utility.MyLog;
import com.tickey.driver.view.custom.CircleNetworkImageView;

import java.util.ArrayList;

public class TicketsBuyersRecycleAdapter extends RecyclerView.Adapter<TicketsBuyersRecycleAdapter.TicketsBuyerViewHolder> implements ItemTouchHelperAdapter{

	private static final String TAG = TicketsBuyersRecycleAdapter.class.getSimpleName();

	private ArrayList<User> mDataSet;
	private LayoutInflater inflater;
	
	private SparseBooleanArray selectedItems;
	private TicketsBuyerViewHolder mSelectedHolder;
	private TicketsScreen instance;
	private String selectedLine;
	private int selectedItem = -1;
	private ImageLoader mImageLoader = BaseApplication.getInstance()
			.getImageLoader();
	private int lastPosition = -1;

	private Handler mHandler;

	public TicketsBuyersRecycleAdapter(Context context, ArrayList<User> dataSet) {
		inflater = LayoutInflater.from(context);
		instance = (TicketsScreen) context;
		mDataSet = dataSet;
		selectedItems = new SparseBooleanArray();
		mHandler = new Handler();

	}



	@Override
	public void onItemDismiss(final RecyclerView.ViewHolder holder) {
		final TicketsBuyerViewHolder vH = (TicketsBuyerViewHolder) holder;

		MyLog.i(TAG, "Item dissmised");
		vH.undoLayout.setVisibility(View.VISIBLE);
		vH.buyerLayout.setVisibility(View.GONE);

//				((TicketsBuyerViewHolder) holder).undoLayout.setVisibility(View.VISIBLE);


	}

	public class TicketsBuyerViewHolder extends RecyclerView.ViewHolder{

		public LinearLayout buyerLayout;

		public LinearLayout undoLayout;
		public TextView undoButton;
		public TextView deleteButton;

		public TextView mTextView;
		public CircleNetworkImageView buyerAvatar;
		public View theView;
		public int position;
		public ImageView emptyList;
		
		public TicketsBuyerViewHolder(View itemView) {
			super(itemView);
//			if(mDataSet.size() > 0) {

				buyerLayout = (LinearLayout) itemView.findViewById(R.id.buyer_layout);
				undoLayout = (LinearLayout) itemView.findViewById(R.id.undo_layout);

				deleteButton = (TextView) undoLayout.findViewById(R.id.txt_delete);
				undoButton = (TextView) undoLayout.findViewById(R.id.txt_undo);

				buyerAvatar = (CircleNetworkImageView) itemView.findViewById(R.id.niv_buyers_avatar);
				buyerAvatar.setDefaultImageResId(R.drawable.ic_employee_avatar);
				buyerAvatar.setErrorImageResId(R.drawable.ic_employee_avatar);
				
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
	public void onBindViewHolder(final TicketsBuyerViewHolder holder, int position) {
		// TODO Auto-generated method stub
//		if(mDataSet.size() > 0) {
			User buyer = mDataSet.get(position);
			holder.position = position;
			holder.mTextView.setText(buyer.fullName);
			holder.deleteButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						MyLog.i(TAG, "Deleted Focus");
						v.performClick();
					}
				}
			});
			holder.deleteButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					holder.deleteButton.setClickable(false);
					remove(holder.getAdapterPosition());
					MyLog.i(TAG, "Deleted");
				}
			});
			holder.undoButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus) {
						MyLog.i(TAG, "Undo Focus");
						v.performClick();
					}
				}
			});
			holder.undoButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					holder.undoLayout.setVisibility(View.INVISIBLE);
					holder.buyerLayout.setVisibility(View.VISIBLE);
					MyLog.i(TAG, "Undo");
				}
			});
			if(buyer.imageUrl != null && !buyer.imageUrl.equals("")) {
				
				try {
					holder.buyerAvatar.setImageUrl(buyer.imageUrl, mImageLoader);
					

				} catch (Exception e) {
					// TODO: handle exception

					holder.buyerAvatar.setImageUrl("https://31.media.tumblr.com/avatar_48fd47f91171_128.png", mImageLoader);
				}
				
			} else {

				holder.buyerAvatar.setImageUrl("https://31.media.tumblr.com/avatar_48fd47f91171_128.png", mImageLoader);
			}
//		} 

		
		
	}

	@Override
	public TicketsBuyerViewHolder onCreateViewHolder(ViewGroup parent, final int position) {
		// TODO Auto-generated method stub
//		MyLog.i("tag", "parent heigth " + parent.getMeasuredHeight() / 4);
		TicketsBuyerViewHolder holder;
//		if(mDataSet.size() > 0) {
			View layout = inflater.inflate(R.layout.tickets_buyer_layout, parent, false);
//			final LinearLayout buyerLayout = (LinearLayout) layout.findViewById(R.id.buyer_layout);
//			final LinearLayout undoLayout = (LinearLayout) layout.findViewById(R.id.undo_layout);
//			buyerLayout.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//
//				}
//			});

//			TextView deleteButton = (TextView) layout.findViewById(R.id.txt_delete);
//			deleteButton.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					mDataSet.remove(position);
//					notifyItemRemoved(position);
//					MyLog.i(TAG, "Deleted");
//					Log.i(TAG, "Deleted");
//				}
//			});
//			TextView undoButton = (TextView) layout.findViewById(R.id.txt_undo);
//			undoButton.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					undoLayout.setVisibility(View.INVISIBLE);
//					buyerLayout.setVisibility(View.VISIBLE);
//					MyLog.i(TAG, "Undo");
//				}
//			});
			holder = new TicketsBuyerViewHolder(layout);


		return holder;
	}
	
    public void remove(int position) {
		Authorization.removeBuyerFromSession(position);
        mDataSet.remove(position);
        notifyItemRemoved(position);
        if(mDataSet.size() > 0) {
        	instance.getTicketsMainFragmet().setAvatar(mDataSet.get(0));
        } else {
        	instance.getTicketsMainFragmet().setAvatar(null);
        	instance.getTicketsBuyersFragment().emptyList();
        }
        
    }



}
