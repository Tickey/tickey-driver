package com.tickey.driver.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.tickey.driver.R;
import com.tickey.driver.common.BaseApplication;
import com.tickey.driver.data.model.User;
import com.tickey.driver.gcm.GcmPreferences;
import com.tickey.driver.screens.TicketsScreen;
import com.tickey.driver.view.custom.CircleNetworkImageView;
import com.tickey.driver.view.custom.DividerItemDecoration;
import com.tickey.driver.view.custom.RoundedImageView;

public class TicketsMainFragment extends Fragment{

	private RoundedImageView lastBuyerAvatar;
	private TextView lastBuyerName;
    private BroadcastReceiver mTicketsBuyingReceiver;
    private ImageLoader mImageLoader;
    private View borderView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
//		View content = inflater.inflate(R.layout.fragment_tickets_main, container, false);
		View content = inflater.inflate(R.layout.fragment_tickets_main_new, container, false);
		
		mImageLoader = BaseApplication.getInstance().getImageLoader();
		borderView = content.findViewById(R.id.borderView);
		
		lastBuyerAvatar = (RoundedImageView) content.findViewById(R.id.last_buyer_avatar);
		lastBuyerName = (TextView) content.findViewById(R.id.last_buyer_name);
		
		float density = getActivity().getResources().getDisplayMetrics().density;
		final float px = 250 * density;
		
		mTicketsBuyingReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				User newUser = new Gson().fromJson(intent.getStringExtra("buyerObject"), User.class);
				if(newUser != null) {
					
					lastBuyerAvatar.setImageUrl(newUser.imageUrl + "?width=" + px, mImageLoader);
					lastBuyerName.setText(newUser.fullName);
					borderView.setVisibility(View.VISIBLE);
					((TicketsScreen) (getActivity())).commitPhoneSale();
//					lastBuyerAvatar.setImageUrl("https://31.media.tumblr.com/avatar_48fd47f91171_128.png", mImageLoader, true);
//					lastBuyerAvatar.setImageUrl("https://scontent-ams3-1.xx.fbcdn.net/hphotos-xpa1/t31.0-8/1401310_808669665847064_7901199621549789934_o.jpg", mImageLoader, true);
				}
			}
		};
//		final ImageView buyTicketImageView = (ImageView) content.findViewById(R.id.iv_buy_ticket);
//		final TextView buyTicketTextView = (TextView) content.findViewById(R.id.tv_buy_ticket);
		
//		final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//		
//		ViewTreeObserver vto = buyTicketImageView.getViewTreeObserver(); 
//		
//		Display display = getActivity().getWindowManager().getDefaultDisplay();
//		final DisplayMetrics metrics = new DisplayMetrics();
//		display.getRealMetrics(metrics);
//		
//		try {
////			Method mGetRawH = Display.class.getMethod("getRawHeight");
////			Method mGetRawW = Display.class.getMethod("getRawWidth");
////			int rawWidth = (Integer) mGetRawW.invoke(display);
////			int rawHeight = (Integer) mGetRawH.invoke(display);
////			float d = getActivity().getResources().getDisplayMetrics().density;
//            int width = metrics.widthPixels;
//            int height = metrics.heightPixels;
//			int margin = (int) ((height * 0.0666f));
//			params.setMargins(0,0,0,margin);
//			params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
//			buyTicketTextView.setLayoutParams(params);
////			width = (int) (width * 0.0666f);
//			MyLog.i("", " width : " + width + " Width * percent: " + (width * 0.666f) +"  margin " + margin);
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		

		
//		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
//		    @Override 
//		    public void onGlobalLayout() { 
//		        buyTicketImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
//		        int margin = (int) (buyTicketImageView.getMeasuredWidth() * 0.11f) ;
//				MyLog.i("", "Measured width : " + buyTicketImageView.getMeasuredWidth() + " Width: " + buyTicketImageView.getWidth() +"  margin " + margin);
//				
//				params.setMargins(0,0,0,margin);
//				params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
////				buyTicketTextView.setLayoutParams(params);
//		    } 
//		});
//		
		
		
		
		

		
		return content;
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
