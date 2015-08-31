package com.voidgreen.privatcurrency.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.voidgreen.privatcurrency.R;
import com.voidgreen.privatcurrency.utilities.Constants;

/**
 * Created by y.shlapak on Jun 10, 2015.
 */
public class CurrencyWidgetProvider extends AppWidgetProvider {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

/*        ComponentName thisWidget = new ComponentName(context,
                CurrencyWidgetProvider.class);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        for (int widgetId : appWidgetManager.getAppWidgetIds(thisWidget)) {
            views.
            appWidgetManager.updateAppWidget(widgetId, views);
        }*/
        Log.d(Constants.TAG, "CurrencyWidgetProvider onUpdate");
        for (int i=0; i<appWidgetIds.length; i++) {

            RemoteViews remoteViews = updateWidgetListView(context,
                    appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i],
                    remoteViews);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

/*    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        Utility.stopBatteryInfoService(context);
        Utility.stopUpdateService(context);
        Utility.stopAlarm();
        Utility.showToast(context, "CurrencyWidgetProvider:onDeleted");
    }*/


    private RemoteViews updateWidgetListView(Context context,
                                             int appWidgetId) {

        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(),R.layout.widget_layout);

        // Here we setup the intent which points to the StackViewService which will
        // provide the views for this collection.
        Intent svcIntent = new Intent(context, WidgetService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        svcIntent.setData(Uri.parse(
                svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter( R.id.listViewWidget,
                svcIntent);
        //setting an empty view in case of no data
        //remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
        return remoteViews;
    }
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        Utility.startAlarm(context);
        Utility.showToast(context, "CurrencyWidgetProvider:onEnabled");

    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        Utility.stopAlarm();
        Utility.showToast(context, "CurrencyWidgetProvider:onDisabled");
    }


}
