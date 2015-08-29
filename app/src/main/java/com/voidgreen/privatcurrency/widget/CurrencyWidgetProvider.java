package com.voidgreen.privatcurrency.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.voidgreen.privatcurrency.R;

/**
 * Created by y.shlapak on Jun 10, 2015.
 */
public class CurrencyWidgetProvider extends AppWidgetProvider {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

/*        ComponentName thisWidget = new ComponentName(context,
                CurrencyWidgetProvider.class);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        for (int widgetId : appWidgetManager.getAppWidgetIds(thisWidget)) {
            views.
            appWidgetManager.updateAppWidget(widgetId, views);
        }*/
        for (int i=0; i<appWidgetIds.length; i++) {
            Intent svcIntent=new Intent(ctxt, WidgetService.class);

            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews widget=new RemoteViews(ctxt.getPackageName(),
                    R.layout.widget_layout);

            widget.setRemoteAdapter(appWidgetIds[i], R.id.words,
                    svcIntent);

            Intent clickIntent=new Intent(ctxt, LoremActivity.class);
            PendingIntent clickPI=PendingIntent
                    .getActivity(ctxt, 0,
                            clickIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

            widget.setPendingIntentTemplate(R.id.words, clickPI);

            appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
        }
    }

/*    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        Utility.stopBatteryInfoService(context);
        Utility.stopUpdateService(context);
        Utility.stopAlarm();
        Utility.showToast(context, "CurrencyWidgetProvider:onDeleted");
    }*/

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
