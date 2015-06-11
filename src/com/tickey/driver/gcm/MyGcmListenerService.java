/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tickey.driver.gcm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmListenerService;
import com.tickey.driver.utility.MyLog;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        MyLog.d(TAG, "From: " + from);
        MyLog.d(TAG, "Message: " + message);
        MyLog.d(TAG, "Type: " + data.getString("type"));
        MyLog.d(TAG, "Type: " + data.getString("user"));
        
        String type = data.getString("type");

        switch (type) {
		case GcmPreferences.TYPE_TICKET:
			sendTicketNotification(data.getString("user"));
			break;
		default:
			break;
		}
        
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendTicketNotification(String user) {
    	
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(GcmPreferences.TYPE_TICKET);
        registrationComplete.putExtra("buyerObject", user);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

    }
}
