package com.voidgreen.privatcurrency.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;

import com.voidgreen.privatcurrency.utilities.Constants;

/**
 * Created by yaroslav on 9/6/15.
 */
public class WidgetConfig {
    Cursor cursor;
    public WidgetConfig(int id, int color, String type, int updateInterval, Context context) {

        // Constructs a selection clause that matches the word that the user entered.
        String mSelectionClause = CurrencyContract.WidgetEntry.COLUMN_WIDGET_ID + " = ?";

        // Moves the user's input string to the selection arguments.
        String[] mSelectionArgs = {""};

        mSelectionArgs[0] = "" + id;

        cursor = context.getContentResolver().query(
                CurrencyContract.WidgetEntry.CONTENT_URI,
                Constants.DETAIL_COLUMNS_WIDGET,
                mSelectionClause,
                mSelectionArgs,
                null);

        int count = cursor.getCount();
        ContentValues contentValues = getContentValues(id, color, type, updateInterval);
        if(count < 1) {
            context.getContentResolver().insert(
                    CurrencyContract.WidgetEntry.CONTENT_URI,   // the user dictionary content URI
                    contentValues                          // the values to insert
            );
        } else if(count > 0) {
            context.getContentResolver().update(
                    CurrencyContract.WidgetEntry.CONTENT_URI,   // the user dictionary content URI
                    contentValues,                          // the values to insert
                    mSelectionClause,
                    mSelectionArgs
            );
            cursor = context.getContentResolver().query(
                    CurrencyContract.WidgetEntry.CONTENT_URI,
                    Constants.DETAIL_COLUMNS_WIDGET,
                    mSelectionClause,
                    mSelectionArgs,
                    null);
            if(isCursorReady()) {
                setId(cursor.getInt(Constants.COL_WIDGET_ID));
                setUpdateInterval(cursor.getInt(Constants.COL_UPDATE_INTERVAL));
                setType(cursor.getString(Constants.COL_TYPE));
                setColor(cursor.getInt(Constants.COL_COLOR));
            }
        }
    }

    public void closeCursor() {
        if (cursor != null) {
            cursor.close();
        }
    }

    public WidgetConfig(int id, Context context) {

        // Constructs a selection clause that matches the word that the user entered.
        String mSelectionClause = CurrencyContract.WidgetEntry.COLUMN_WIDGET_ID + " = ?";

        // Moves the user's input string to the selection arguments.
        String[] mSelectionArgs = {""};

        mSelectionArgs[0] = "" + id;

        final long token = Binder.clearCallingIdentity();
        try {
            getCursor(context, mSelectionArgs, mSelectionClause);
        } finally {
            Binder.restoreCallingIdentity(token);
        }

        if(isCursorReady()) {
            setId(cursor.getInt(Constants.COL_WIDGET_ID));
            setUpdateInterval(cursor.getInt(Constants.COL_UPDATE_INTERVAL));
            setType(cursor.getString(Constants.COL_TYPE));
            setColor(cursor.getInt(Constants.COL_COLOR));
        }
    }

    public boolean isCursorReady() {
        //Log.d(Constants.TAG, "WidgetConfig :isCursorReady = " + (cursor != null && cursor.moveToFirst()));
        return cursor != null && cursor.moveToFirst();
    }

    public void getCursor(Context context, String[] mSelectionArgs, String mSelectionClause) {
        cursor = context.getContentResolver().query(
                CurrencyContract.WidgetEntry.CONTENT_URI,
                Constants.DETAIL_COLUMNS_WIDGET,
                mSelectionClause,
                mSelectionArgs,
                null);
    }


    private int id;
    private int color;
    private String type;
    private int updateInterval;

    public int getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    private ContentValues getContentValues(int id, int color, String type, int updateInterval) {
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(CurrencyContract.WidgetEntry.COLUMN_WIDGET_ID, id);
        mNewValues.put(CurrencyContract.WidgetEntry.COLUMN_COLOR, color);
        mNewValues.put(CurrencyContract.WidgetEntry.COLUMN_TYPE, type);
        mNewValues.put(CurrencyContract.WidgetEntry.COLUMN_UPDATE_INTERVAL, updateInterval);
        return mNewValues;
    }

}
