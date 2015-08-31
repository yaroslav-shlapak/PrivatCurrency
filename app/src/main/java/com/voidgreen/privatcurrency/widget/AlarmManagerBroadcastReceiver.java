package com.voidgreen.privatcurrency.widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.voidgreen.privatcurrency.data.DownloadCurrencyTask;
import com.voidgreen.privatcurrency.utilities.Constants;

/**
 * Created by VOID on 13-06-15.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, CurrencyWidgetProvider.class);

        Log.d(Constants.TAG, "AlarmManagerBroadcastReceiver onReceive");
        new DownloadCurrencyTask(context).execute(Constants.EXCHANGE_RATE_CARD);

        Utility.updateAllWidgets(context, appWidgetManager, appWidgetManager.getAppWidgetIds(thisWidget));

    }


}

