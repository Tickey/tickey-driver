package com.tickey.app.utility;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

public class Facebook {

	private static final String TAG = Facebook.class.getSimpleName();

	public static final String PUBLISH_PERMISSION = "publish_actions";

	public static void shareStory(Activity activity, String link, String name,
			String caption, String description, String imageUrl,
			UiLifecycleHelper uiHelper) {

		if (isPermissionExisting(activity, PUBLISH_PERMISSION)) {
			FacebookDialog dialog = new FacebookDialog.ShareDialogBuilder(
					activity).setLink(link).setName(name).setCaption(caption)
					.setDescription(description).setPicture(imageUrl)
					.setApplicationName("Tickey").build();
			uiHelper.trackPendingDialogCall(dialog.present());
		} else {
			Toast.makeText(activity,
					"Tickey is not allowed to post on your wall",
					Toast.LENGTH_SHORT).show();
		}
	}

	public static boolean isPermissionExisting(Activity activity,
			String permission) {

		boolean result = false;

		Session session = Session.getActiveSession();

		if (session == null) {
			session = Session.openActiveSessionFromCache(activity);
		}

		if (session != null && session.isOpened()) {
			MyLog.v(TAG, "session is opened");
			List<String> permissions = session.getPermissions();

			if (permissions != null && !TextUtils.isEmpty(permission)) {
				for (String perm : permissions) {
					MyLog.v(TAG, "loooop");
					if (perm.equalsIgnoreCase(permission)) {
						MyLog.v(TAG, "fooooooooound");
						result = true;

						break;
					}
				}
			}
		}

		return result;
	}

}
