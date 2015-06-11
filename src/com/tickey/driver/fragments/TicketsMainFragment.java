package com.tickey.driver.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tickey.driver.R;

public class TicketsMainFragment extends Fragment{

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View content = inflater.inflate(R.layout.fragment_tickets_main, container, false);
		
		final ImageView buyTicketImageView = (ImageView) content.findViewById(R.id.iv_buy_ticket);
		final TextView buyTicketTextView = (TextView) content.findViewById(R.id.tv_buy_ticket);
		
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
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	
	
}
