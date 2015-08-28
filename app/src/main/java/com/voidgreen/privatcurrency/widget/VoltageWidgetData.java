package com.voidgreen.privatcurrency.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.preference.PreferenceManager;

import com.voidgreen.privatcurrency.R;

/**
 * Created by y.shlapak on Jun 18, 2015.
 */
public class VoltageWidgetData {
    SharedPreferences sharedPreferences;
    public int getTextSize() {
        return textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public String getUpdateInterval() {
        return updateInterval;
    }

    private int textSize = 40;
    private int textColor = Color.WHITE;
    private String updateInterval = "1000";

    VoltageWidgetData(Context context) {
        Resources resources = context.getResources();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        this.textSize = sharedPreferences.getInt(resources.getString(R.string.pref_text_size_key), this.textSize);
        this.textColor = sharedPreferences.getInt(resources.getString(R.string.pref_text_color_key), this.textColor);
        this.updateInterval = sharedPreferences.getString(resources.getString(R.string.pref_update_period_key), this.updateInterval);
    }
}
