package com.voidgreen.privatcurrency.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.voidgreen.privatcurrency.data.CurrencyContract.CardEntry;
import com.voidgreen.privatcurrency.data.CurrencyContract.CashEntry;
import com.voidgreen.privatcurrency.data.CurrencyContract.WidgetEntry;

/**
 * Created by y.shlapak on Aug 05, 2015.
 */

public class CurrencyDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "privatcurrency.db";

    public CurrencyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_CASH_TABLE =
                "CREATE TABLE " + CashEntry.TABLE_NAME + " (" +
                        CashEntry._ID + " INTEGER PRIMARY KEY," +
                        CashEntry.COLUMN_BASE_CURRENCY + " TEXT NOT NULL, " +
                        CashEntry.COLUMN_CURRENCY + " TEXT NOT NULL, " +
                        CashEntry.COLUMN_BUY + " REAL NOT NULL, " +
                        CashEntry.COLUMN_SALE + " REAL NOT NULL " +
                " );";

        final String SQL_CREATE_CARD_TABLE =
                "CREATE TABLE " + CardEntry.TABLE_NAME + " (" +
                        CardEntry._ID + " INTEGER PRIMARY KEY," +
                        CardEntry.COLUMN_BASE_CURRENCY + " TEXT NOT NULL, " +
                        CardEntry.COLUMN_CURRENCY + " TEXT NOT NULL, " +
                        CardEntry.COLUMN_BUY + " REAL NOT NULL, " +
                        CardEntry.COLUMN_SALE + " REAL NOT NULL " +
                        " );";

        final String SQL_CREATE_WIDGET_TABLE =
                "CREATE TABLE " + WidgetEntry.TABLE_NAME + " (" +
                        WidgetEntry._ID + " INTEGER PRIMARY KEY," +
                        WidgetEntry.COLUMN_COLOR + " INTEGER NOT NULL, " +
                        WidgetEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                        WidgetEntry.COLUMN_UPDATE_INTERVAL + " REAL NOT NULL, " +
                        " );";

        sqLiteDatabase.execSQL(SQL_CREATE_CASH_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CARD_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_WIDGET_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CardEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CashEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WidgetEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
