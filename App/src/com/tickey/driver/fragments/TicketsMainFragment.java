package com.tickey.driver.fragments;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.tickey.driver.R;
import com.tickey.driver.common.BaseApplication;
import com.tickey.driver.data.model.User;
import com.tickey.driver.screens.TicketsScreen;
import com.tickey.driver.utility.MyLog;
import com.tickey.driver.view.custom.RoundedImageView;

public class TicketsMainFragment extends Fragment{

	private RoundedImageView lastBuyerAvatar;
	private TextView lastBuyerName;
    private BroadcastReceiver mTicketsBuyingReceiver;
    private ImageLoader mImageLoader;
    private View borderView;
    private float px;

    private boolean isRunning = false;
    private ArrayList<User> buffer;
    public static final String TAG = TicketsMainFragment.class.getSimpleName();
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		buffer = new ArrayList<User>();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		isRunning = true;
//		View content = inflater.inflate(R.layout.fragment_tickets_main, container, false);
		View content = inflater.inflate(R.layout.fragment_tickets_main_new, container, false);
		
		mImageLoader = BaseApplication.getInstance().getImageLoader();
		borderView = content.findViewById(R.id.borderView);
		
		lastBuyerAvatar = (RoundedImageView) content.findViewById(R.id.last_buyer_avatar);
		lastBuyerName = (TextView) content.findViewById(R.id.last_buyer_name);

		lastBuyerAvatar.setDefaultImageResId(R.drawable.ic_employee_avatar);
		
		float density = getActivity().getResources().getDisplayMetrics().density;
		px = 250 * density;
		
		mTicketsBuyingReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				User newUser = new Gson().fromJson(intent.getStringExtra("buyerObject"), User.class);
				if(isRunning) {
					if(newUser != null) {
						//lastBuyerAvatar.setImageDrawable(null);

						
//						lastBuyerAvatar.setImageUrl("https://31.media.tumblr.com/avatar_48fd47f91171_128.png", mImageLoader, true);
//						lastBuyerAvatar.setImageUrl("https://scontent-ams3-1.xx.fbcdn.net/hphotos-xpa1/t31.0-8/1401310_808669665847064_7901199621549789934_o.jpg", mImageLoader, true);
					}
				} else {
					buffer.add(newUser);
				}

			}
		};
		
		
		

		
		return content;
	}

	public void commitBuy(User newUser) {
		String imageUrl = newUser.imageUrl + "?width=" + Math.round(px);
		MyLog.i("", "Image url - " + imageUrl);
		lastBuyerAvatar.setImageUrl(imageUrl, mImageLoader);
		lastBuyerName.setText(newUser.fullName);
		borderView.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mTicketsBuyingReceiver,
//                new IntentFilter(GcmPreferences.TYPE_TICKET));
//        getActivity().stopService(collectorIntent);
        isRunning = true;

	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub

		MyLog.i(TAG, "paused");

        
        
        super.onPause();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		isRunning = false;
//        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mTicketsBuyingReceiver);
		super.onDestroy();
		
	}
	
	public void setAvatar(User user) {
		if(user != null) {
			lastBuyerAvatar.setImageUrl(user.imageUrl + "?width=" + Math.round(px), mImageLoader);
			lastBuyerName.setText(user.fullName);
			borderView.setVisibility(View.VISIBLE);
		} else {
			
//			lastBuyerAvatar.setImageUrl("https://bs1.cdn.telerik.com/v1/O2nVwi9maNQ6ULMI/aae07000-0a13-11e5-b7e8-01960c9961db", mImageLoader);
//			lastBuyerAvatar.setImageDrawable(getActivity().getDrawable(R.drawable.ic_employee_avatar));
			lastBuyerAvatar.setImageUrl(null, mImageLoader);
			lastBuyerName.setText("Empty");
		}

	}
	
	
}
