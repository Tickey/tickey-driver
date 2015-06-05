package com.tickey.app.utility;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class Hardware {

	public static boolean setBluetooth(boolean enable) {
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		boolean isEnabled = bluetoothAdapter.isEnabled();
		if (enable && !isEnabled) {
			return bluetoothAdapter.enable();
		} else if (!enable && isEnabled) {
			return bluetoothAdapter.disable();
		}

		return true;
	}

	public static String getDeviceId(Application application) {
		String packageName = application.getPackageName();

		SharedPreferences prefs = application.getApplicationContext()
				.getSharedPreferences(packageName, Context.MODE_PRIVATE);

		String keyDeviceId = packageName + "."
				+ Authorization.PARAM_KEY_DEVICE_ID;

		String deviceId = prefs.getString(keyDeviceId, null);

		if (deviceId == null) {
			TelephonyManager telephonyManager = (TelephonyManager) application
					.getSystemService(Context.TELEPHONY_SERVICE);

			deviceId = telephonyManager.getDeviceId();

			if (deviceId == null) {
				deviceId = Secure.getString(application.getContentResolver(),
						Secure.ANDROID_ID);
			} else {
				if (deviceId.length() == 0) {
					deviceId = Secure
							.getString(application.getContentResolver(),
									Secure.ANDROID_ID);
				}
			}

			prefs.edit().putString(keyDeviceId, deviceId).commit();
		}

		return deviceId;
	}

	public static String getDeviceName() {
		return android.os.Build.MODEL;
	}
}
