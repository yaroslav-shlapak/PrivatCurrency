package com.voidgreen.privatcurrency.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

/**
 * Created by y.shlapak on Jun 10, 2015.
 */
public class VoltageWidgetProvider extends AppWidgetProvider {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Utility.updateAllWidgets(context);
        Utility.showToast(context, "VoltageWidgetProvider:onUpdate");
    }

/*    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        Utility.stopBatteryInfoService(context);
        Utility.stopUpdateService(context);
        Utility.stopAlarm();
        Utility.showToast(context, "VoltageWidgetProvider:onDeleted");
    }*/

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        Utility.startBatteryInfoService(context);
        Utility.startUpdateService(context);
        Utility.startAlarm(context);
        Utility.showToast(context, "VoltageWidgetProvider:onEnabled");

    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        Utility.stopBatteryInfoService(context);
        Utility.stopUpdateService(context);
        Utility.stopAlarm();
        Utility.showToast(context, "VoltageWidgetProvider:onDisabled");
    }


}
