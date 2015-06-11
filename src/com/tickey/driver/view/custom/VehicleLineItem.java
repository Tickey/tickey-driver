package com.tickey.driver.view.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tickey.driver.R;

public class VehicleLineItem extends View{
	
	private TextView textView;
	
	public VehicleLineItem(Context context, ViewGroup parent) {
		super(context);
		View layout = LayoutInflater.from(context).inflate(R.layout.vehicle_line_number_layout, parent, true); 
		textView = (TextView) layout.findViewById(R.id.tv_vehicleLineNumber);
		
	}
	
	public void setText(String text) {
		textView.setText(text);
	}

}
