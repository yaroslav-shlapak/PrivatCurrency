package com.voidgreen.privatcurrency.widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.voidgreen.privatcurrency.R;

/**
 * Created by VOID on 13-06-15.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context,
                CurrencyWidgetProvider.class);

        //Log.d(Constants.TAG, "AlarmManagerBroadcastReceiver onReceive");
        for (int widgetId : appWidgetManager.getAppWidgetIds(thisWidget)) {

            RemoteViews remoteViews = updateWidgetListView(context,
                    widgetId);
            appWidgetManager.updateAppWidget(widgetId,
                    remoteViews);

            //appWidgetManager.updateAppWidget(widgetId, widget);
        }

        Utility.showToast(context, "AlarmManagerBroadcastReceiver");
    }
    private RemoteViews updateWidgetListView(Context context,
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

}

