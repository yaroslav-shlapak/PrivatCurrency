package com.voidgreen.privatcurrency.settings;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RemoteViews;

import com.voidgreen.privatcurrency.R;
import com.voidgreen.privatcurrency.data.WidgetConfig;
import com.voidgreen.privatcurrency.utilities.Constants;
import com.voidgreen.privatcurrency.utilities.Utility;

/**
 * Created by y.shlapak on Jun 17, 2015.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    int mAppWidgetId;
    Context context;
    RemoteViews views;
    WidgetConfig widgetConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        context = getApplicationContext();
        views = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            int id = intent.getIntExtra(Constants.WIDGET_ID, -1);
            if(id == -1) {
                mAppWidgetId = extras.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
            } else {
                mAppWidgetId = id;
            }
        }
        Log.d(Constants.TAG, "SettingsActivity:onCreate mAppWidgetId= " + mAppWidgetId);

        widgetConfig = new WidgetConfig(
                mAppWidgetId,
                context
        );

        if(widgetConfig.isCursorReady()) {
            Log.d(Constants.TAG, "SettingsActivity:onCreate exchangeType= " + widgetConfig.getType());
            Log.d(Constants.TAG, "SettingsActivity:onCreate color= " + widgetConfig.getColor());
            Log.d(Constants.TAG, "SettingsActivity:onCreate updateInterval= " + widgetConfig.getUpdateInterval());
            Utility.setExchangeType(widgetConfig.getType(), context);
            Utility.setTextColor(widgetConfig.getColor(), context);
            Utility.setUpdateTime(Integer.toString(widgetConfig.getUpdateInterval()), context);
        }
        widgetConfig.closeCursor();


        //Utility.startAlarm(context);
        addPreferencesFromResource(R.xml.preferences);
        initSummary(getPreferenceScreen());

    }

    public void onApplyButtonClick(View v) {
        saveWidgetModification();
    }

    private void saveWidgetModification() {
        //Utility.saveBatteryInfo(context, Utility.getSavedBatteryInfo(context));
        updateWidgetInfo();
        PendingIntent pendingIntent = Utility.getAlarmIntent(context);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

/*        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] ids = {mAppWidgetId};
        Utility.startDownload(context);
        Utility.updateAllWidgets(context, appWidgetManager, ids);*/

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePrefSummary(findPreference(key));
        updateWidgetInfo();


    }
    private void updateWidgetInfo() {
        widgetConfig = new WidgetConfig(
                mAppWidgetId,
                Utility.getTextColor(context),
                Utility.getExchangeType(context),
                Utility.getUpdateTime(context),
                context
        );
        Log.d(Constants.TAG, "SettingsActivity:onSharedPreferenceChanged exchangeType= " + widgetConfig.getType());
        Log.d(Constants.TAG, "SettingsActivity:onSharedPreferenceChanged color= " + widgetConfig.getColor());
        Log.d(Constants.TAG, "SettingsActivity:onSharedPreferenceChanged updateInterval= " + widgetConfig.getUpdateInterval());
        widgetConfig.closeCursor();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_play:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.voidgreen.privatcurrency"));
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
