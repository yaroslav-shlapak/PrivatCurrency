package com.voidgreen.privatcurrency.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

import com.voidgreen.privatcurrency.R;
import com.voidgreen.privatcurrency.utilities.Constants;

/**
 * Created by y.shlapak on Jun 15, 2015.
 */
public class Utility {
    private static AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;


    public static void updateAllWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int widgetId : appWidgetIds) {

            Log.d(Constants.TAG, "Utility updateAllWidgets : widgetId = " + widgetId);

            RemoteViews remoteViews = updateWidgetListView(context, widgetId);
            appWidgetManager.updateAppWidget(widgetId,
                    remoteViews);
        }

    }

    private static RemoteViews updateWidgetListView(Context context,
                                                    int appWidgetId) {

        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), R.layout.widget_layout);

        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, WidgetService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(
                svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
                svcIntent);
        //setting an empty view in case of no data
        //remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
        return remoteViews;
    }


    public static void showToast(Context context, String string) {
        //Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }

    public static void startAlarm(Context context) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmManagerBroadcastReceiver.class), 0);
        alarmMgr.setInexactRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime() + 3 * 1000, 10000, alarmIntent);
        Log.d(Constants.TAG, "Utility startAlarm");
    }

    public static void stopAlarm() {
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
            Log.d(Constants.TAG, "Utility stopAlarm");
        }

    }

    public static void scheduleUpdate(Context context) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pi = getAlarmIntent(context);
        am.cancel(pi);
        am.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + 3000, 10000, pi);
    }

    private static PendingIntent getAlarmIntent(Context context) {
        Intent intent = new Intent(context, CurrencyWidgetProvider.class);
        intent.setAction(Constants.ACTION_UPDATE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        return pi;
    }

    public static void clearUpdate(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(getAlarmIntent(context));
    }

}
