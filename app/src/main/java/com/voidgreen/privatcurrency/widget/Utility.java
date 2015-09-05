package com.voidgreen.privatcurrency.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.voidgreen.privatcurrency.R;
import com.voidgreen.privatcurrency.data.CurrencyContract;
import com.voidgreen.privatcurrency.data.DownloadCurrencyTask;
import com.voidgreen.privatcurrency.utilities.Constants;

/**
 * Created by y.shlapak on Jun 15, 2015.
 */
public class Utility {

    public static  void updateAllWidgetsFromOutside(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, CurrencyWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        Utility.updateAllWidgets(context, appWidgetManager, appWidgetIds);
    }

    public static void updateAllWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        DownloadCurrencyTask.mUriCard = CurrencyContract.CardEntry.CONTENT_URI;
        DownloadCurrencyTask.mUriCash = CurrencyContract.CashEntry.CONTENT_URI;


        for (int widgetId : appWidgetIds) {


            Log.d(Constants.TAG, "Utility updateAllWidgets : widgetId = " + widgetId);

            RemoteViews remoteViews = updateWidgetListView(context, widgetId);
            appWidgetManager.updateAppWidget(widgetId,
                    remoteViews);
            appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.listViewWidget);

        }

    }

    private static RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        // Here we setup the intent which points to the widget which will
        // provide the views for this collection.
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), R.layout.widget_layout);

        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, WidgetService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        // The empty view is displayed when the collection has no items. It should be a sibling
        // of the collection view.
        remoteViews.setRemoteAdapter(R.id.listViewWidget, svcIntent);
        //setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
        Log.d(Constants.TAG, "Utility updateWidgetListView");
        return remoteViews;
    }


    public static void showToast(Context context, String string) {
        //Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }




    public static void scheduleUpdate(Context context) {

        Log.d(Constants.TAG, "Utility scheduleUpdate");

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pi = getAlarmIntent(context);
        am.cancel(pi);
        am.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + 3000, 60 * 1000, pi);
    }

    private static PendingIntent getAlarmIntent(Context context) {
        Intent intent = new Intent(context, CurrencyWidgetProvider.class);
        intent.setAction(Constants.ACTION_UPDATE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        return pi;
    }

    public static void clearUpdate(Context context) {
        Log.d(Constants.TAG, "Utility clearUpdate");
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(getAlarmIntent(context));
    }

    public static void downloadData(Context context, String type) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadCurrencyTask(context).execute(type);

            Log.d(Constants.TAG, "ViewsFactory onCreate");
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.connectionProblem), Toast.LENGTH_LONG);
        }
    }

}
