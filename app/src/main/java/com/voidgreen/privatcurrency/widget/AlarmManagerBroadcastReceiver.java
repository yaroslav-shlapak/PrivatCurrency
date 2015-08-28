package com.voidgreen.privatcurrency.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by VOID on 13-06-15.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Utility.startBatteryInfoService(context);

        Utility.startUpdateService(context);

        Utility.updateAllWidgets(context);

        Utility.showToast(context, "AlarmManagerBroadcastReceiver");
    }

}

