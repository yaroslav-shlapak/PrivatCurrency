package com.voidgreen.privatcurrency.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.voidgreen.privatcurrency.DownloadCurrencyTask;
import com.voidgreen.privatcurrency.R;
import com.voidgreen.privatcurrency.data.CurrencyContract;
import com.voidgreen.privatcurrency.utilities.Constants;



/**
 * Created by yaroslav on 8/29/15.
 */
public class ViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Cursor cursor;
    private Context context = null;
    private int appWidgetId;


    public ViewsFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

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
        return(remoteView);
    }

    @Override
    public void onCreate() {

        DownloadCurrencyTask.mUriCard = CurrencyContract.CardEntry.CONTENT_URI;
        DownloadCurrencyTask.mUriCash = CurrencyContract.CashEntry.CONTENT_URI;

        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadCurrencyTask(context).execute(Constants.EXCHANGE_RATE_CARD);
            cursor = context.getContentResolver().query(
                    DownloadCurrencyTask.mUriCard,
                    Constants.DETAIL_COLUMNS_CARD,
                    null,
                    null,
                    null);
            Log.d(Constants.TAG, "ViewsFactory ViewsFactory");
        }
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        if(cursor != null) {
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        if(cursor != null) {
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
