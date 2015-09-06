package com.voidgreen.privatcurrency;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.voidgreen.privatcurrency.adapters.CurrencyAdapter;
import com.voidgreen.privatcurrency.data.CurrencyContract.CardEntry;
import com.voidgreen.privatcurrency.data.CurrencyContract.CashEntry;
import com.voidgreen.privatcurrency.data.DownloadCurrencyTask;
import com.voidgreen.privatcurrency.settings.SettingsActivity;
import com.voidgreen.privatcurrency.utilities.Constants;
import com.voidgreen.privatcurrency.utilities.Utility;

public class MainActivity extends Activity {
    ListView listViewCard, listViewCash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getContentResolver().delete(CardEntry.CONTENT_URI, null, null);
        getContentResolver().delete(CashEntry.CONTENT_URI, null, null);


        DownloadCurrencyTask.mUriCard = CardEntry.CONTENT_URI;
        DownloadCurrencyTask.mUriCash = CashEntry.CONTENT_URI;
        getLoaderManager().initLoader(0, null, new CardLoader());
        getLoaderManager().initLoader(1, null, new CashLoader());

        //textView.setText(getHttpRequest());

        // Find ListView to populate
        listViewCard = (ListView) findViewById(R.id.listViewCard);
        listViewCash = (ListView) findViewById(R.id.listViewCash);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.action_play:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.voidgreen.privatcurrency"));
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // When user clicks button, calls AsyncTask.
    // Before attempting to fetch the URL, makes sure that there is a network connection.
    public void myClickHandler(View view) {
        // Gets the URL from the UI's text field.
        Utility.downloadData(getApplicationContext(), Constants.EXCHANGE_RATE_CARD);
        Utility.downloadData(getApplicationContext(), Constants.EXCHANGE_RATE_CASH);
    }

    private class CardLoader implements LoaderManager.LoaderCallbacks<Cursor> {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (null != DownloadCurrencyTask.mUriCard) {
                // Now create and return a CursorLoader that will take care of
                // creating a Cursor for the data being displayed.
                return new CursorLoader(
                        getApplicationContext(),
                        DownloadCurrencyTask.mUriCard,
                        Constants.DETAIL_COLUMNS_CARD,
                        null,
                        null,
                        null
                );
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null && data.moveToFirst()) {

                DownloadCurrencyTask.cardCount = data.getCount();
                // Setup cursor adapter using cursor from last step
                CurrencyAdapter todoAdapter = new CurrencyAdapter(getApplicationContext(), data);
                // Attach cursor adapter to the ListView
                listViewCard.setAdapter(todoAdapter);

            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }

    }

    private class CashLoader implements LoaderManager.LoaderCallbacks<Cursor> {
        CurrencyAdapter todoAdapter;
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (null != DownloadCurrencyTask.mUriCash) {
                // Now create and return a CursorLoader that will take care of
                // creating a Cursor for the data being displayed.
                return new CursorLoader(
                        getApplicationContext(),
                        DownloadCurrencyTask.mUriCash,
                        Constants.DETAIL_COLUMNS_CASH,
                        null,
                        null,
                        null
                );
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null && data.moveToFirst()) {

                DownloadCurrencyTask.cashCount = data.getCount();
                // Setup cursor adapter using cursor from last step

                if(todoAdapter == null || todoAdapter.getCount() == 0) {

                    todoAdapter = new CurrencyAdapter(getApplicationContext(), data);
                    // Attach cursor adapter to the ListView
                    listViewCash.setAdapter(todoAdapter);
                    todoAdapter.changeCursor(data);
                } else {
                    todoAdapter.changeCursor(data);
                }
            }
        }
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }
    }

    }
