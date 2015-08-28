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

/**
 * Created by y.shlapak on Jun 15, 2015.
 */
public class Utility {
    public final static String DEFAULT_STRING = "4000";
    private static AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;

    public static String getSavedBatteryInfo(Context context) {
        SharedPreferences batteryInfoSharedPref = context.getSharedPreferences(context.getString(R.string.voltage_widget_shared_pref),
                Context.MODE_PRIVATE);
        return batteryInfoSharedPref.getString
                (context.getString(R.string.battery_info_shared_pref_key), DEFAULT_STRING);
    }

    public static void saveBatteryInfo(Context context, String batteryInfo) {
        SharedPreferences batteryInfoSharedPref = context.getSharedPreferences(context.getString(R.string.voltage_widget_shared_pref),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = batteryInfoSharedPref.edit();
        editor.putString(context.getString(R.string.battery_info_shared_pref_key), batteryInfo);
        editor.apply();
    }

    public static void updateAllWidgets(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context,
                VoltageWidgetProvider.class);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        for (int widgetId : appWidgetManager.getAppWidgetIds(thisWidget)) {
            updateWidget(context, appWidgetManager, views, widgetId);
        }
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, RemoteViews views, int widgetId) {
        VoltageWidgetData voltageWidgetData = new VoltageWidgetData(context);
        views.setTextColor(R.id.batteryInfoTextViewWidget, voltageWidgetData.getTextColor());
        views.setTextColor(R.id.mV, voltageWidgetData.getTextColor());
        views.setFloat(R.id.batteryInfoTextViewWidget, "setTextSize", voltageWidgetData.getTextSize());
        views.setFloat(R.id.mV, "setTextSize", voltageWidgetData.getTextSize() / 3);

        views.setTextViewText(R.id.batteryInfoTextViewWidget, Utility.getSavedBatteryInfo(context));
        appWidgetManager.updateAppWidget(widgetId, views);
    }

    public static void startBatteryInfoService(Context context) {
        Intent i = new Intent(context, BatteryInfoService.class);
        context.startService(i);
    }

    public static void stopBatteryInfoService(Context context) {
        Intent i = new Intent(context, BatteryInfoService.class);
        context.stopService(i);
    }

    public static void startUpdateService(Context context) {
        Intent i = new Intent(context, UpdateService.class);
        context.startService(i);
    }

    public static void stopUpdateService(Context context) {
        Intent i = new Intent(context, UpdateService.class);
        context.stopService(i);
    }


    public static void showToast(Context context, String string) {
        //Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }

    public static void startAlarm(Context context) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmManagerBroadcastReceiver.class), 0);
        alarmMgr.setInexactRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime() + 10 * 1000, 180 * 1000, alarmIntent);
    }

    public static void stopAlarm() {
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }

    }
}
