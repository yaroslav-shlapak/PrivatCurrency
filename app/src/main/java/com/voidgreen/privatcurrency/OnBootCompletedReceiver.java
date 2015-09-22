package com.voidgreen.privatcurrency;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import com.voidgreen.privatcurrency.utilities.Utility;

/**
 * Created by Void on 22-Sep-15.
 */
public class OnBootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Utility.updateAllWidgetsFromOutside(context);
    }
}

