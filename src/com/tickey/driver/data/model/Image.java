package com.tickey.driver.data.model;

import android.graphics.Bitmap;

public class Image {

	public static final int PICK_IMAGE = 1;
	public static final int TAKE_PHOTO = 2;
	
	public Bitmap bitmap;
	
	public byte[] imageByteArray;
	
	public Image(Bitmap bitmap, byte[] imageByteArray) {
		this.bitmap = bitmap;
		this.imageByteArray = imageByteArray;
	}
}
