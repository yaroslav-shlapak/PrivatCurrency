package com.voidgreen.privatcurrency.settings;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.voidgreen.privatcurrency.R;
import com.voidgreen.privatcurrency.data.WidgetCofig;
import com.voidgreen.privatcurrency.utilities.Constants;
import com.voidgreen.privatcurrency.utilities.Utility;

/**
 * Created by y.shlapak on Jun 17, 2015.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    int mAppWidgetId;
    Context context;
    RemoteViews views;
    WidgetCofig widgetCofig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        context = getApplicationContext();
        views = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        widgetCofig = new WidgetCofig(
                mAppWidgetId,
                context
        );

        if(widgetCofig.isCursorReady()) {
            Utility.setExchangeType(widgetCofig.getType(), context);
            Utility.setTextColor(widgetCofig.getColor(), context);
            Utility.setUpdateTime(Integer.toString(widgetCofig.getUpdateInterval()), context);
        }


        //Utility.startAlarm(context);

        initSummary(getPreferenceScreen());

    }

    public void onApplyButtonClick(View v) {
        saveWidgetModification();
    }

    private void saveWidgetModification() {
        //Utility.saveBatteryInfo(context, Utility.getSavedBatteryInfo(context));
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] ids = {mAppWidgetId};
        Utility.updateAllWidgets(context, appWidgetManager, ids);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePrefSummary(findPreference(key));
        widgetCofig = new WidgetCofig(
                mAppWidgetId,
                Utility.getTextColor(context),
                Utility.getExchangeType(context),
                Utility.getUpdateTime(context),
                context
        );
    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }

    private void updatePrefSummary(Preference p) {
        if(p instanceof ListPreference) {
            ListPreference preference = (ListPreference) p;
            p.setSummary(preference.getEntry());
            Log.d(Constants.TAG, "SettingsActivity:updatePrefSummary preference.getEntry() = " + preference.getEntry());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
