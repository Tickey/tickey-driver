package com.tickey.app.view.adapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tickey.app.fragments.VehicleTypeFragment;

public class VehicleTypeAdapter extends FragmentPagerAdapter{

	private int size = 0;
	private HashMap<String, ArrayList<String>> linesMap;
	private ArrayList<String> types;
	
	public VehicleTypeAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		VehicleTypeFragment tab = VehicleTypeFragment.getInstance(linesMap.get(types.get(position)));
		return tab;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return size;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return types.get(position);
	}


	public int getSize() {
		return size;
	}


	public void setSize(int size) {
		this.size = size;
	}


	public HashMap<String, ArrayList<String>> getLinesMap() {
		return linesMap;
	}


	public void setLinesMap(HashMap<String, ArrayList<String>> linesMap) {
		this.linesMap = linesMap;
	}


	public ArrayList<String> getTypes() {
		return types;
	}


	public void setTypes(ArrayList<String> types) {
		this.types = types;
	}
	
	
}
