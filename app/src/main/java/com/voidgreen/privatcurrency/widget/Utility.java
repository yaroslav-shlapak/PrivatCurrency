package com.voidgreen.privatcurrency.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.widget.RemoteViews;

import com.voidgreen.privatcurrency.R;

/**
 * Created by y.shlapak on Jun 15, 2015.
 */
public class Utility {
    public final static String DEFAULT_STRING = "4000";
    private static AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;


    public static void updateAllWidgets(Context context) {
/*        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context,
                CurrencyWidgetProvider.class);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        for (int widgetId : appWidgetManager.getAppWidgetIds(thisWidget)) {
            updateWidget(context, appWidgetManager, views, widgetId);
            appWidgetManager.updateAppWidget(widgetId, views);
        }*/
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, RemoteViews views, int widgetId) {


    }


    public static void showToast(Context context, String string) {
        //Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }

    public static void startAlarm(Context context) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmManagerBroadcastReceiver.class), 0);
        alarmMgr.setInexactRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime() + 10 * 1000, 1000, alarmIntent);
    }

    public static void stopAlarm() {
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }

    }
}
