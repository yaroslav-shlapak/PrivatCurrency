package com.voidgreen.privatcurrency.utilities;

import com.voidgreen.privatcurrency.data.CurrencyContract;

/**
 * Created by y.shlapak on Aug 25, 2015.
 */
public class Constants {
    public static final String TAG = "privatcurrency.tag";
    public static final String EXTRA_ITEM = "com.voidgreen.privatcurrency.EXTRA_ITEM";
    public static final String ACTION_UPDATE = "com.voidgreen.privatcurrency.ACTION_UPDATE";
    public static final String ACTION_CLICK = "com.voidgreen.privatcurrency.ACTION_CLICK";
    public static final String EXCHANGE_RATE_CARD = "https://api.privatbank.ua/p24api/pubinfo?exchange&json&coursid=11";
    public static final String EXCHANGE_RATE_CASH = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";
    public static final String WIDGET_ID = "com.voidgreen.privatcurrency.WIDGET_ID";

    public static final String[] CURRENCIES = {"RUR", "EUR", "USD"};

    public static final String[] DETAIL_COLUMNS_CARD = {
            CurrencyContract.CardEntry.TABLE_NAME + "." + CurrencyContract.CardEntry._ID,
            CurrencyContract.CardEntry.COLUMN_BASE_CURRENCY,
            CurrencyContract.CardEntry.COLUMN_CURRENCY,
            CurrencyContract.CardEntry.COLUMN_BUY,
            CurrencyContract.CardEntry.COLUMN_SALE
    };
    public static final String[] DETAIL_COLUMNS_CASH = {
            CurrencyContract.CashEntry.TABLE_NAME + "." + CurrencyContract.CashEntry._ID,
            CurrencyContract.CashEntry.COLUMN_BASE_CURRENCY,
            CurrencyContract.CashEntry.COLUMN_CURRENCY,
            CurrencyContract.CashEntry.COLUMN_BUY,
            CurrencyContract.CashEntry.COLUMN_SALE
    };

    public static final String[] DETAIL_COLUMNS_WIDGET = {
            CurrencyContract.WidgetEntry.TABLE_NAME + "." + CurrencyContract.WidgetEntry._ID,
            CurrencyContract.WidgetEntry.COLUMN_WIDGET_ID,
            CurrencyContract.WidgetEntry.COLUMN_COLOR,
            CurrencyContract.WidgetEntry.COLUMN_TYPE,
            CurrencyContract.WidgetEntry.COLUMN_UPDATE_INTERVAL
    };
    public static final int COL_BASE_CURRENCY = 1;
    public static final int COL_CURRENCY = 2;
    public static final int COL_BUY = 3;
    public static final int COL_SALE = 4;

    public static final int COL_WIDGET_ID = 1;
    public static final int COL_COLOR = 2;
    public static final int COL_TYPE = 3;
    public static final int COL_UPDATE_INTERVAL = 4;
}
