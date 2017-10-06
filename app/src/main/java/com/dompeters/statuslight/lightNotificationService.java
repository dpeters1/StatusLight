package com.dompeters.statuslight;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by Dominic on 2017-09-22.
 */

public class lightNotificationService extends NotificationListenerService {

    private int sbnID;
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        Log.d("StatusLight", "onNotificationPosted: New Notification");
        sbnID = sbn.getId();
        MainActivity.sendRequest(dataSingleton.getInstance().ipAddr, 3, dataSingleton.getInstance().maxBrightness, this);
        // Implement what you want here
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        if(sbnID == sbn.getId()) {
            int ledMode = dataSingleton.getInstance().ledMode;
            int maxBrightness = dataSingleton.getInstance().maxBrightness;
            MainActivity.sendRequest(dataSingleton.getInstance().ipAddr, ledMode, maxBrightness, this);
        }
    }
}
