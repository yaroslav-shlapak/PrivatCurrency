package com.voidgreen.privatcurrency.widget;

/**
 * Created by yaroslav on 8/29/15.
 */

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.voidgreen.privatcurrency.R;
import com.voidgreen.privatcurrency.data.WidgetConfig;
import com.voidgreen.privatcurrency.settings.SettingsActivity;
import com.voidgreen.privatcurrency.utilities.Constants;
import com.voidgreen.privatcurrency.utilities.Utility;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new ViewsFactory(this.getApplicationContext(),
                intent));
    }
}

class ViewsFactory implements RemoteViewsService.RemoteViewsFactory{
    private Cursor cursor;
    private Context context = null;
    private int appWidgetId;
    WidgetConfig widgetConfig;


    public ViewsFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        //Log.d(Constants.TAG, "ViewsFactory ViewsFactory");
    }

    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.item_todo);

        if(cursor.moveToPosition(position)) {
            String currency = Utility.getCurrencySymbol(cursor.getString(Constants.COL_CURRENCY));
            String buy = cursor.getString(Constants.COL_BUY);
            String sale = cursor.getString(Constants.COL_SALE);

            remoteView.setTextViewText(R.id.textViewCurrency, currency);
            remoteView.setTextViewText(R.id.textViewBuyPrice, buy);
            remoteView.setTextViewText(R.id.textViewSalePrice, sale);

            remoteView.setTextColor(R.id.textViewCurrency, widgetConfig.getColor());
            remoteView.setTextColor(R.id.textViewBuyPrice, widgetConfig.getColor());
            remoteView.setTextColor(R.id.textViewSalePrice, widgetConfig.getColor());
        }
        //Setting activity call by onClick
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.putExtra(Constants.WIDGET_ID, appWidgetId);
        //Log.d(Constants.TAG, "ViewsFactory:getViewAt appWidgetId = " + appWidgetId);
        remoteView.setOnClickFillInIntent(R.id.itemTodo, intent);

        //Log.d(Constants.TAG, "ViewsFactory getViewAt");
        return remoteView;
    }

    @Override
    public void onCreate() {


    }

    private void getCursor() {
        cursor = context.getContentResolver().query(
                Utility.getUriByType(widgetConfig.getType()),
                Utility.getDetailColumnsByType(widgetConfig.getType()),
                null,
                null,
                null);
    }

    @Override
    public void onDataSetChanged() {
        // Refresh the cursor
        if (cursor != null) {
            cursor.close();
        }
        if (widgetConfig != null) {
            widgetConfig.closeCursor();
        }
        widgetConfig = new WidgetConfig(appWidgetId, context);

        final long token = Binder.clearCallingIdentity();
        try {
            getCursor();
        } finally {
            Binder.restoreCallingIdentity(token);
        }
        //Log.d(Constants.TAG, "ViewsFactory onDataSetChanged");
    }

    @Override
    public void onDestroy() {
        closeCursors();
        //Log.d(Constants.TAG, "ViewsFactory onDestroy");
    }
    public void closeCursors() {
        //Log.d(Constants.TAG, "ViewsFactory closeCursors");
        if(widgetConfig != null){
            widgetConfig.closeCursor();
        }
        if (cursor != null) {
            //Log.d(Constants.TAG, "ViewsFactory closeCursors cursor.close()");
            cursor.close();
        }
    }


    @Override
    public int getCount() {

        if (cursor != null) {
            //Log.d(Constants.TAG, "getCount cursor.getCount() = " + cursor.getCount());
            return cursor.getCount();

        } else {
            //Log.d(Constants.TAG, "getCount cursor = null");
            return 0;
        }
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}