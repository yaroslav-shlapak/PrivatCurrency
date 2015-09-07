package com.voidgreen.privatcurrency.utilities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.voidgreen.privatcurrency.R;
import com.voidgreen.privatcurrency.data.CurrencyContract;
import com.voidgreen.privatcurrency.data.DownloadCurrencyTask;
import com.voidgreen.privatcurrency.widget.CurrencyWidgetProvider;
import com.voidgreen.privatcurrency.widget.WidgetService;

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



        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pi = getAlarmIntent(context);
        am.cancel(pi);

        int updatePeriod = getUpdateTime(context);
        Log.d(Constants.TAG, "Utility scheduleUpdate updatePeriod = " + updatePeriod);
        am.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + 3000, updatePeriod * 1000, pi);
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

    public static int getUpdateTime(Context context) {
        String defaultValue = "720";
        Resources resources = context.getResources();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return Integer.parseInt(sharedPreferences.getString(resources.getString(R.string.pref_update_period_key), defaultValue));
    }

    public static String getExchangeType(Context context) {
        String defaultValue = "cash";
        Resources resources = context.getResources();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString(resources.getString(R.string.pref_update_period_key), defaultValue);
    }

    public static int getTextColor(Context context) {
        int defaultValue = Color.WHITE;
        Resources resources = context.getResources();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getInt(resources.getString(R.string.pref_text_color_key), defaultValue);
    }

    public static void setUpdateTime(String updateTime, Context context) {
        Resources resources = context.getResources();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(resources.getString(R.string.pref_update_period_key), updateTime);
        editor.commit();
    }

    public static void setExchangeType(String type, Context context) {
        Resources resources = context.getResources();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(resources.getString(R.string.pref_update_period_key), type);
        editor.commit();
    }

    public static void setTextColor(int color, Context context) {
        Resources resources = context.getResources();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(resources.getString(R.string.pref_text_color_key), color);
        editor.commit();
    }

}
