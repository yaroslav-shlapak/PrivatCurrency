package com.voidgreen.privatcurrency.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.voidgreen.privatcurrency.utilities.Constants;

/**
 * Created by y.shlapak on Jun 10, 2015.
 */
public class CurrencyWidgetProvider extends AppWidgetProvider {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.d(Constants.TAG, "CurrencyWidgetProvider onUpdate");
        Utility.updateAllWidgets(context, appWidgetManager, appWidgetIds);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        //Utility.stopAlarm();
        Utility.showToast(context, "CurrencyWidgetProvider:onDeleted");
    }


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        //Utility.startAlarm(context);
        Utility.showToast(context, "CurrencyWidgetProvider:onEnabled");
        Utility.scheduleUpdate(context);

    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        //Utility.stopAlarm();
        Utility.showToast(context, "CurrencyWidgetProvider:onDisabled");
        Utility.clearUpdate(context);
    }


    private static PendingIntent getPendingSelfIntent(Context context, String action, String... content) {
        Intent intent = new Intent(context, CurrencyWidgetProvider.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Constants.ACTION_UPDATE.equals(intent.getAction())) {
            Utility.downloadData(context, Constants.EXCHANGE_RATE_CARD);
            onUpdate(context);
        } else super.onReceive(context, intent);
    }

    private void onUpdate(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(),getClass().getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
