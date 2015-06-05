package com.tickey.app.utility;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

public class Url {

	public static String getMimeType(String url) {
		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		if (extension != null) {
			MimeTypeMap mime = MimeTypeMap.getSingleton();
			type = mime.getMimeTypeFromExtension(extension);
		}
		return type;
	}

	/**
	 * helper to retrieve the path of an image URI
	 */
	public static String getImagePath(Uri uri, Context context) {
		if (uri == null) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, projection,
				null, null, null);
		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		return uri.getPath();
	}

	public static String getFileName(Uri uri, Context context) {
		String fileName = null;

		String scheme = uri.getScheme();
		
		if (scheme.equals("file")) {
			fileName = uri.getLastPathSegment();
		} else if (scheme.equals("content")) {
			String[] proj = { MediaStore.Images.Media.TITLE };
			Cursor cursor = context.getContentResolver().query(uri, proj, null,
					null, null);
			if (cursor != null && cursor.getCount() != 0) {
				int columnIndex = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
				cursor.moveToFirst();
				fileName = cursor.getString(columnIndex);
			}
			if (cursor != null) {
				cursor.close();
			}
		}

		return fileName;
	}
}
