package com.tickey.driver.fragments;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tickey.driver.R;
import com.tickey.driver.data.model.User;
import com.tickey.driver.gcm.GcmPreferences;
import com.tickey.driver.utility.Authorization;
import com.tickey.driver.utility.MyLog;
import com.tickey.driver.view.adapter.TicketsBuyersRecycleAdapter;
import com.tickey.driver.view.adapter.TicketsBuyersRecycleAdapter.OnItemClickListener;
import com.tickey.driver.view.custom.DividerItemDecoration;

public class TicketsBuyersFragment extends Fragment{

	private static final String TAG = TicketsBuyersFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private View borderView;
    private TicketsBuyersRecycleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private BroadcastReceiver mTicketsBuyingReceiver;
    private ArrayList<User> mBuyersDataSet;
    private LinearLayout layout;
    private LinearLayout emptyList;
    private TicketsMainFragment main;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mBuyersDataSet = Authorization.getBuyerSession();
		if(mBuyersDataSet == null) {
			mBuyersDataSet = new ArrayList<User>();
		}
		
		mTicketsBuyingReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
			

				User newUser = new Gson().fromJson(intent.getStringExtra("buyerObject"), User.class);
				if(newUser != null) {
					if(mBuyersDataSet.size() == 0) {
						mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
						layout.removeAllViews();
						layout.addView(borderView);
						layout.addView(mRecyclerView);
					}
					mBuyersDataSet.add(0, newUser);
					Authorization.saveBuyersForSession(newUser);
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
		
		borderView = layout.findViewById(R.id.borderView);
		
		emptyList = (LinearLayout) inflater.inflate(R.layout.empty_byers_list, layout, false);
		
		if(mBuyersDataSet.size() == 0) {
			layout.removeAllViews();
//			mRecyclerView.setVisibility(View.INVISIBLE);
			layout.addView(borderView);
			layout.addView(emptyList);
		} else {
			main = (TicketsMainFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.tickets_main_fragment);
			main.setAvatar(mBuyersDataSet.get(0));
		}
	
		

		
		mLayoutManager = new LinearLayoutManager(getActivity());
		
		OnItemClickListener onItemTouch = new OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
				// TODO Auto-generated method stub
				MyLog.i(TAG, "Item touchted pos " + position);
			}
		};
		
		mAdapter = new TicketsBuyersRecycleAdapter(getActivity(), mBuyersDataSet,  onItemTouch);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setAdapter(mAdapter);
		
	    // init swipe to dismiss logic
	    ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            // callback for drag-n-drop, false to skip this feature
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // callback for swipe to dismiss, removing item from data and adapter
			mAdapter.onItemDismiss(viewHolder);
        }

        @Override
		public int getSwipeDirs(RecyclerView recyclerView,
				ViewHolder viewHolder) {
        	if(((TicketsBuyersRecycleAdapter.TicketsBuyerViewHolder) viewHolder).undoLayout.getVisibility() == View.VISIBLE) {
        		return 0; 
        	}
			// TODO Auto-generated method stub
			return super.getSwipeDirs(recyclerView, viewHolder);
		}


		public void onChildDraw(Canvas c, RecyclerView recyclerView,
								RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState,
								boolean isCurrentlyActive) {
//			MyLog.i(TAG, "onChildDraw");
			getDefaultUIUtil().onDraw(c, recyclerView,
					((TicketsBuyersRecycleAdapter.TicketsBuyerViewHolder) viewHolder).buyerLayout, dX, dY,
					actionState, isCurrentlyActive);

		}

	});

    swipeToDismissTouchHelper.attachToRecyclerView(mRecyclerView);
		
//        final SwipeToDismissTouchListener<RecyclerViewAdapter> touchListener =
//                new SwipeToDismissTouchListener<>(
//                        new RecyclerViewAdapter(mRecyclerView),
//                        new SwipeToDismissTouchListener.DismissCallbacks<RecyclerViewAdapter>() {
//                            @Override
//                            public boolean canDismiss(int position) {
//                                return true;
//                            }
//
//                            @Override
//                            public void onDismiss(RecyclerViewAdapter view, int position) {
//                            	mAdapter.remove(position);
//                            }
//                        });
//
//        mRecyclerView.setOnTouchListener(touchListener);
//        // Setting this scroll listener is required to ensure that during ListView scrolling,
//        // we don't look for swipes.
//        mRecyclerView.setOnScrollListener((RecyclerView.OnScrollListener) touchListener.makeScrollListener());
//
//        mRecyclerView.addOnItemTouchListener(new SwipeableItemClickListener(getActivity(),
//        		new OnItemClickListener() {
//					
//					@Override
//					public void onItemClick(View view, int position, MotionEvent e) {
//						// TODO Auto-generated method stub
//                        if (view.getId() == R.id.txt_delete) {
//                            touchListener.processPendingDismisses();
//                        } else if (view.getId() == R.id.txt_undo) {
//                            touchListener.undoPendingDismiss();
//                        } else { // R.id.txt_data
//                        	LinearLayout parent = (LinearLayout) view.getParent();
////                        	parent.setOnClickListener(new OnClickListener() {
////								
////								@Override
////								public void onClick(View v) {
////									// TODO Auto-generated method stub
////									Toast.makeText(getActivity(), "Parent clicked ", Toast.LENGTH_SHORT).show();
////								}
////							});
////                        	parent.dispatchTouchEvent(e);
//                        	MyLog.i("", "id = " + view.getId() + " layout id = " + R.id.buyer_layout + " parent id = " + parent.getId());
////                            Toast.makeText(getActivity(), "Position " + position, Toast.LENGTH_LONG).show();
//                        }
//                    
//					}
//				}));
		
		
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
	
	public void emptyList() {
		layout.removeAllViews();
//		mRecyclerView.setVisibility(View.INVISIBLE);
		layout.addView(borderView);
		layout.addView(emptyList);
	}
	
}
