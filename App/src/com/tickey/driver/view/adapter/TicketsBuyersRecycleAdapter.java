package com.tickey.driver.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import java.util.HashMap;

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
	private HashMap<Integer, Runnable> runnables;
	
	private OnItemClickListener mOnItemClickListener;
	
	public interface OnItemClickListener {
	    public void onItemClick(View view, int position);
	}

	public TicketsBuyersRecycleAdapter(Context context, ArrayList<User> dataSet, OnItemClickListener onItemClickListener) {
		inflater = LayoutInflater.from(context);
		instance = (TicketsScreen) context;
		mDataSet = dataSet;
		selectedItems = new SparseBooleanArray();
		mOnItemClickListener = onItemClickListener;
		mHandler = new Handler();
		runnables = new HashMap<Integer, Runnable>();

	}



	@Override
	public void onItemDismiss(final RecyclerView.ViewHolder holder) {
		final TicketsBuyerViewHolder vH = (TicketsBuyerViewHolder) holder;

		MyLog.i(TAG, "Item dissmised");
		
		vH.undoLayout.setVisibility(View.VISIBLE);
		vH.buyerLayout.setVisibility(View.GONE);
		startRunnable(holder.getAdapterPosition(), holder);
//				((TicketsBuyerViewHolder) holder).undoLayout.setVisibility(View.VISIBLE);


	}

	public class TicketsBuyerViewHolder extends RecyclerView.ViewHolder{

		public LinearLayout buyerLayout;

		public LinearLayout undoLayout;
		
		public FrameLayout container;
		
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

				container = (FrameLayout) itemView.findViewById(R.id.lyt_container);
				
//				deleteButton = (TextView) undoLayout.findViewById(R.id.txt_delete);
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
	public void onBindViewHolder(final TicketsBuyerViewHolder holder, final int position) {
		MyLog.i(TAG, "item binded");
		// TODO Auto-generated method stub
//		if(mDataSet.size() > 0) {

			User buyer = mDataSet.get(position);
			holder.undoLayout.setVisibility(View.GONE);
			holder.buyerLayout.setVisibility(View.VISIBLE);


			
			holder.buyerLayout.setX(0f);
			MyLog.i(TAG, "buyer X - " + holder.buyerLayout.getX());
			
			holder.position = position;
			holder.mTextView.setText(buyer.fullName);
			holder.buyerLayout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MyLog.i(TAG, "From adapter click" );
					mOnItemClickListener.onItemClick(v, position);
				}
			});
//			holder.undoLayout.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					mOnItemClickListener.onItemClick(v, position);
//					removeRunnable(holder.getAdapterPosition());
//				}
//			});
//			holder.deleteButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//					if (hasFocus) {
//						MyLog.i(TAG, "Deleted Focus");
//						v.performClick();
//					}
//				}
//			});
//			holder.deleteButton.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					holder.deleteButton.setClickable(false);
//					removeRunnable(holder.getAdapterPosition());
//					remove(holder.getAdapterPosition());					
//					MyLog.i(TAG, "Deleted");
//				}
//			});
//			holder.undoButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//					if(hasFocus) {
//						MyLog.i(TAG, "Undo Focus");
//						v.performClick();
//					}
//				}
//			});
			holder.undoButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					holder.undoLayout.setVisibility(View.GONE);
					holder.buyerLayout.setVisibility(View.VISIBLE);
					removeRunnable(holder.getAdapterPosition());
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
		MyLog.i(TAG, "holder created");
		TicketsBuyerViewHolder holder;
		View layout = inflater.inflate(R.layout.tickets_buyer_layout, parent, false);

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
    


    public void startRunnable(final int position, final RecyclerView.ViewHolder holder) {
		final TicketsBuyerViewHolder vH = (TicketsBuyerViewHolder) holder;
    	
    	Runnable run = runnables.get(position);
    	
    	User user = mDataSet.get(position);
    	
    	if(run == null) {
    		run = new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
//					vH.undoLayout.setVisibility(View.GONE);
//					vH.buyerLayout.setVisibility(View.VISIBLE);
					remove(vH.getAdapterPosition());
					runnables.remove(position);
				}
			};
			
			runnables.put(position, run);
			mHandler.postDelayed(run, 4000);
    	}
    }
    
    public void removeRunnable(int position) {
    	
    	Runnable run = runnables.get(position);

    	MyLog.i(TAG, "remove runnable - " + run);
    	if(run != null) { 
    		mHandler.removeCallbacks(run);
    		runnables.remove(position);
    	}
    }


}
