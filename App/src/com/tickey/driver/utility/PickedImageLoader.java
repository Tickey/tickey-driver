package com.tickey.driver.utility;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.ParcelFileDescriptor;

import com.tickey.driver.data.model.Image;

public abstract class PickedImageLoader extends AsyncTask<Uri, Void, Image> {

	@SuppressWarnings("unused")
	private static final String TAG = PickedImageLoader.class.getSimpleName();

	protected abstract void onPostExecuteListener(Image result);

	protected abstract void onPreExecuteListener();

	private Context mContext;
	private int mResultCode;

	public PickedImageLoader(Context context, int resultCode) {
		mContext = context;
		mResultCode = resultCode;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		onPreExecuteListener();
	}

	@Override
	protected Image doInBackground(Uri... params) {
		Bitmap bitmap = null;
		Uri selectedImageUri = params[0];

		if (selectedImageUri == null) {
			return new Image(null, null);
		}

		ContentResolver contentResolver = mContext.getContentResolver();

		if (mResultCode == Image.TAKE_PHOTO) {
			bitmap = getCameraPhoto(selectedImageUri, contentResolver);
		} else if (mResultCode == Image.PICK_IMAGE) {
			bitmap = getPickedImage(selectedImageUri, contentResolver);
		}

		if (bitmap == null) {
			return null;
		}

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		try {
			byteArrayOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new Image(bitmap, byteArray);
	}

	private Bitmap getPickedImage(Uri selectedImageUri,
			ContentResolver contentResolver) {
		Bitmap bitmap = null;

		if (Build.VERSION.SDK_INT < 19) {
			String imagePath = Url.getImagePath(selectedImageUri, mContext);
			bitmap = BitmapFactory.decodeFile(imagePath);

		} else {

			try {
				ParcelFileDescriptor parcelFileDescriptor = contentResolver
						.openFileDescriptor(selectedImageUri, "r");
				FileDescriptor fileDescriptor = parcelFileDescriptor
						.getFileDescriptor();

				bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		return bitmap;
	}

	private Bitmap getCameraPhoto(Uri selectedImageUri,
			ContentResolver contentResolver) {
		Bitmap bitmap = null;
		contentResolver.notifyChange(selectedImageUri, null);

		try {
			bitmap = android.provider.MediaStore.Images.Media.getBitmap(
					contentResolver, selectedImageUri);
		} catch (Exception e) {
			MyLog.e("Camera", e.toString());
		}

		return bitmap;
	}

	@Override
	protected void onPostExecute(Image result) {
		super.onPostExecute(result);

		onPostExecuteListener(result);
	}

}
