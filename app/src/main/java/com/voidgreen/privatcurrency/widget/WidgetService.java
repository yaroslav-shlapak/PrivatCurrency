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
import com.voidgreen.privatcurrency.data.CurrencyContract;
import com.voidgreen.privatcurrency.utilities.Constants;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new ViewsFactory(this.getApplicationContext(),
                intent));
    }
}

class ViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Cursor cursor;
    private Context context = null;
    private int appWidgetId;


    public ViewsFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d(Constants.TAG, "ViewsFactory ViewsFactory");
    }

    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.item_todo);

        cursor.moveToPosition(position);
        String currency = cursor.getString(Constants.COL_CURRENCY);
        String baseCurrency = cursor.getString(Constants.COL_BASE_CURRENCY);
        String buy = cursor.getString(Constants.COL_BUY);
        String sale = cursor.getString(Constants.COL_SALE);

        remoteView.setTextViewText(R.id.textViewCurrency, currency);
        remoteView.setTextViewText(R.id.textViewBaseCurrency, baseCurrency);
        remoteView.setTextViewText(R.id.textViewBuyPrice, buy);
        remoteView.setTextViewText(R.id.textViewSalePrice, sale);

        Log.d(Constants.TAG, "ViewsFactory getViewAt");
        return (remoteView);
    }

    @Override
    public void onCreate() {

        getCursor();
    }

    private void getCursor() {
        cursor = context.getContentResolver().query(
                CurrencyContract.CardEntry.CONTENT_URI,
                Constants.DETAIL_COLUMNS_CARD,
                null,
                null,
                null);
    }

    @Override
    public void onDataSetChanged() {
        final long token = Binder.clearCallingIdentity();
        try {
            getCursor();
        } finally {
            Binder.restoreCallingIdentity(token);
        }
        Log.d(Constants.TAG, "ViewsFactory onDataSetChanged");
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }
        Log.d(Constants.TAG, "ViewsFactory onDestroy");
    }

    @Override
    public int getCount() {

        if (cursor != null) {
            Log.d(Constants.TAG, "getCount cursor.getCount() = " + cursor.getCount());
            return cursor.getCount();

        } else {
            Log.d(Constants.TAG, "getCount cursor = null");
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