package com.voidgreen.privatcurrency;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.voidgreen.privatcurrency.data.CurrencyContract.CardEntry;
import com.voidgreen.privatcurrency.json.JsonMessage;
import com.voidgreen.privatcurrency.json.JsonParser;
import com.voidgreen.privatcurrency.utilities.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>  {
    TextView textViewCurrency, textViewBaseCurrency, textViewBuyPrice, textViewSalePrice;
    private static final String DEBUG_TAG = "HttpExample";
    private Uri mUri;
    private static final String[] DETAIL_COLUMNS = {
            CardEntry.TABLE_NAME + "." + CardEntry._ID,
            CardEntry.COLUMN_BASE_CURRENCY,
            CardEntry.COLUMN_CURRENCY,
            CardEntry.COLUMN_BUY,
            CardEntry.COLUMN_SALE
    };
    // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
    // must change.
    public static final int COL_WEATHER_ID = 0;
    public static final int COL_BASE_CURRENCY = 1;
    public static final int COL_CURRENCY = 2;
    public static final int COL_BUY = 3;
    public static final int COL_SALE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mUri = CardEntry.CONTENT_URI;
        getLoaderManager().initLoader(0, null, this);

        textViewCurrency = (TextView) findViewById(R.id.textViewCurrency);
        textViewBaseCurrency = (TextView) findViewById(R.id.textViewBaseCurrency);
        textViewBuyPrice = (TextView) findViewById(R.id.textViewBuyPrice);
        textViewSalePrice = (TextView) findViewById(R.id.textViewSalePrice);
        //textView.setText(getHttpRequest());

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
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    // When user clicks button, calls AsyncTask.
    // Before attempting to fetch the URL, makes sure that there is a network connection.
    public void myClickHandler(View view) {
        // Gets the URL from the UI's text field.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(Constants.EXCHANGE_RATE_CARD, Constants.EXCHANGE_RATE_CASH);
        } else {
            //textView.setText("No network connection available.");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    this,
                    mUri,
                    DETAIL_COLUMNS,
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
            String currency = data.getString(COL_CURRENCY);
            String baseCurrency = data.getString(COL_BASE_CURRENCY);
            String buy = data.getString(COL_BUY);
            String sale = data.getString(COL_SALE);

            textViewCurrency.setText(currency);
            textViewBaseCurrency.setText(baseCurrency);
            textViewBuyPrice.setText(buy);
            textViewSalePrice.setText(sale);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<String, Void, List> {
        @Override
        protected List doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return null;
                //return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(List result) {
            if(result != null) {
                Cursor cursor = getContentResolver().query(
                        mUri,
                        DETAIL_COLUMNS,
                        null,
                        null,
                        null);
                int count = cursor.getCount();
                if(count == 0) {
                    for (int i = 0; i < result.size(); i++) {
                        JsonMessage jsonMessage = (JsonMessage) result.get(i);

                        insertDataToDatabase(jsonMessage);
                    }
                } else if(count == 3) {
                    for (int i = 0; i < result.size(); i++) {
                        JsonMessage jsonMessage = (JsonMessage) result.get(i);

                        updateDatabase(jsonMessage, Constants.CURENCIES[i], CardEntry.COLUMN_CURRENCY, CardEntry.CONTENT_URI);
                    }
                }

                JsonMessage jsonMessage = (JsonMessage) result.get(0);

                String currency = jsonMessage.getCurrency();
                String baseCurrency = jsonMessage.getBaseCurrency();
                String buy = jsonMessage.getBuyPrice();
                String sale = jsonMessage.getSalePrice();

                textViewCurrency.setText(currency);
                textViewBaseCurrency.setText(baseCurrency);
                textViewBuyPrice.setText(buy);
                textViewSalePrice.setText(sale);
            }
        }
    }

    private void fillData() {

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
/*        String[] from = new String[] { TodoTable.COLUMN_SUMMARY };
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.label };*/

        getLoaderManager().initLoader(0, null, this);
/*        adapter = new SimpleCursorAdapter(this, R.layout.todo_row, null, from,
                to, 0);

        setListAdapter(adapter);*/
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private List downloadUrl(String myurl) throws IOException {
        InputStream is = null;


        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            List list = readIt(is);
            return list;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public List readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        List list = JsonParser.readJsonStream(stream);
        return list;
    }

    private void insertDataToDatabase(JsonMessage jsonMessage) {
        ContentValues mNewValues = getContentValuesFromJson(jsonMessage);


            mUri = getContentResolver().insert(
                    CardEntry.CONTENT_URI,   // the user dictionary content URI
                    mNewValues                          // the values to insert
            );
            Log.d(Constants.TAG, "mUri = " + mUri);


    }

    private void updateDatabase(JsonMessage jsonMessage, String mSearchString, String selectionArg, Uri contentUri) {
        ContentValues mNewValues = getContentValuesFromJson(jsonMessage);

        // Constructs a selection clause that matches the word that the user entered.
        String mSelectionClause = selectionArg + " = ?";

        // Moves the user's input string to the selection arguments.
        String[] mSelectionArgs = {""};

        mSelectionArgs[0] = mSearchString;


        int id = getContentResolver().update(
                contentUri,   // the user dictionary content URI
                mNewValues,                          // the values to insert
                mSelectionClause,
                mSelectionArgs
        );
        Log.d(Constants.TAG, "updateDatabase : id = " + id);
        Log.d(Constants.TAG, "updateDatabase : mSearchString = " + mSearchString);
        Log.d(Constants.TAG, "updateDatabase : mSelectionClause = " + mSelectionClause);

    }


    private ContentValues getContentValuesFromJson(JsonMessage jsonMessage) {
        // Defines an object to contain the new values to insert
        ContentValues mNewValues = new ContentValues();

        /*
         * Sets the values of each column and inserts the word. The arguments to the "put"
         * method are "column name" and "value"
         */
        mNewValues.put(CardEntry.COLUMN_BASE_CURRENCY, "HUY"/*jsonMessage.getBaseCurrency()*/);
        mNewValues.put(CardEntry.COLUMN_CURRENCY, jsonMessage.getCurrency());
        mNewValues.put(CardEntry.COLUMN_BUY, jsonMessage.getBuyPrice());
        mNewValues.put(CardEntry.COLUMN_SALE, jsonMessage.getSalePrice());
        return mNewValues;

    }
    private void updateTextViews() {

    }
}
