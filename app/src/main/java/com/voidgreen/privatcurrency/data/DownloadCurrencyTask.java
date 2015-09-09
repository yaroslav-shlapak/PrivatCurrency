package com.voidgreen.privatcurrency.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.voidgreen.privatcurrency.R;
import com.voidgreen.privatcurrency.json.JsonMessage;
import com.voidgreen.privatcurrency.json.JsonParser;
import com.voidgreen.privatcurrency.utilities.Constants;
import com.voidgreen.privatcurrency.utilities.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by y.shlapak on Aug 31, 2015.
 */ // Uses AsyncTask to create a task away from the main UI thread. This task takes a
// URL string and uses it to create an HttpUrlConnection. Once the connection
// has been established, the AsyncTask downloads the contents of the webpage as
// an InputStream. Finally, the InputStream is converted into a string, which is
// displayed in the UI by the AsyncTask's onPostExecute method.
public class DownloadCurrencyTask extends AsyncTask<String, Void, List> {
    private Context context;
    public static Uri mUriCard, mUriCash;
    public  static int cashCount, cardCount;

    public DownloadCurrencyTask(Context context) {
        this.context = context;
    }

    @Override
    protected List doInBackground(String... urls) {


        // params comes from the execute() call: params[0] is the url.
        try {
            List list = downloadUrl(urls[0]);
            list.add(urls[0]);
            return list;
        } catch (IOException e) {
            return null;
            //return "Unable to retrieve web page. URL may be invalid.";
        }

    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(List result) {
        if (result != null) {

            if (cashCount != (result.size() - 1)) {
                Cursor cursor;
                cursor = context.getContentResolver().query(
                        mUriCash,
                        Constants.DETAIL_COLUMNS_CASH,
                        null,
                        null,
                        null);
                cashCount = cursor.getCount();
            }
            if (cardCount != (result.size() - 1)) {
                Cursor cursor;
                cursor = context.getContentResolver().query(
                        mUriCard,
                        Constants.DETAIL_COLUMNS_CARD,
                        null,
                        null,
                        null);
                cardCount = cursor.getCount();
            }
            Resources resources = context.getResources();
            for (int i = 0; i < (result.size() - 1); i++) {
                JsonMessage jsonMessage = (JsonMessage) result.get(i);
                JsonMessage firstRow = new JsonMessage(
                        resources.getString(R.string.currencyHeader),
                        resources.getString(R.string.currencyBaseHeader),
                        resources.getString(R.string.bidHeader),
                        resources.getString(R.string.askHeader));

                switch ((String) result.get(result.size() - 1)) {
                    case Constants.EXCHANGE_RATE_CARD:

                        //Log.d(Constants.TAG, "onPostExecute EXCHANGE_RATE_CARD : result.size = " + result.size());
                        //Log.d(Constants.TAG, "onPostExecute EXCHANGE_RATE_CARD : cardCount = " + cardCount);
                        if (cardCount < result.size()) {
                            if (cardCount == 0) {
                                insertDataToDatabase(firstRow, CurrencyContract.CardEntry.CONTENT_URI);
                                cardCount++;
                            }
                            //Log.d(Constants.TAG, "onPostExecute EXCHANGE_RATE_CARD : insert");
                            insertDataToDatabase(jsonMessage, CurrencyContract.CardEntry.CONTENT_URI);


                        } else if (cardCount == result.size()) {
                            //Log.d(Constants.TAG, "onPostExecute EXCHANGE_RATE_CARD : update");
                            updateDatabase(jsonMessage, Constants.CURRENCIES[i], CurrencyContract.CardEntry.COLUMN_CURRENCY, CurrencyContract.CardEntry.CONTENT_URI);
                        }
                        break;
                    case Constants.EXCHANGE_RATE_CASH:

                        //Log.d(Constants.TAG, "onPostExecute EXCHANGE_RATE_CASH : result.size = " + result.size());
                        //Log.d(Constants.TAG, "onPostExecute EXCHANGE_RATE_CASH : cashCount = " + cashCount);

                        if (cashCount < result.size()) {
                            if (cashCount == 0) {
                                insertDataToDatabase(firstRow, CurrencyContract.CashEntry.CONTENT_URI);
                                cashCount++;
                            }
                            //Log.d(Constants.TAG, "onPostExecute EXCHANGE_RATE_CASH : insert");
                            insertDataToDatabase(jsonMessage, CurrencyContract.CashEntry.CONTENT_URI);
                        } else if (cashCount == result.size()) {
                            //Log.d(Constants.TAG, "onPostExecute EXCHANGE_RATE_CASH : update");
                            updateDatabase(jsonMessage, Constants.CURRENCIES[i], CurrencyContract.CashEntry.COLUMN_CURRENCY, CurrencyContract.CashEntry.CONTENT_URI);
                        }
                        break;
                }

            }
        }

        Utility.updateAllWidgetsFromOutside(context);
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

    private void insertDataToDatabase(JsonMessage jsonMessage, Uri contentUri) {
        ContentValues mNewValues = getContentValuesFromJson(jsonMessage);


        context.getContentResolver().insert(
                contentUri,   // the user dictionary content URI
                mNewValues                          // the values to insert
        );

    }

    private void updateDatabase(JsonMessage jsonMessage, String mSearchString, String selectionArg, Uri contentUri) {
        ContentValues mNewValues = getContentValuesFromJson(jsonMessage);

        // Constructs a selection clause that matches the word that the user entered.
        String mSelectionClause = selectionArg + " = ?";

        // Moves the user's input string to the selection arguments.
        String[] mSelectionArgs = {""};

        mSelectionArgs[0] = mSearchString;


        int id = context.getContentResolver().update(
                contentUri,   // the user dictionary content URI
                mNewValues,                          // the values to insert
                mSelectionClause,
                mSelectionArgs
        );
        //Log.d(Constants.TAG, "updateDatabase : id = " + id);
        //Log.d(Constants.TAG, "updateDatabase : mSearchString = " + mSearchString);
        //Log.d(Constants.TAG, "updateDatabase : mSelectionClause = " + mSelectionClause);

    }


    private ContentValues getContentValuesFromJson(JsonMessage jsonMessage) {
        // Defines an object to contain the new values to insert
        ContentValues mNewValues = new ContentValues();

    /*
     * Sets the values of each column and inserts the word. The arguments to the "put"
     * method are "column name" and "value"
     */
        mNewValues.put(CurrencyContract.CardEntry.COLUMN_BASE_CURRENCY, jsonMessage.getBaseCurrency());
        mNewValues.put(CurrencyContract.CardEntry.COLUMN_CURRENCY, jsonMessage.getCurrency());
        mNewValues.put(CurrencyContract.CardEntry.COLUMN_BUY, jsonMessage.getBuyPrice());
        mNewValues.put(CurrencyContract.CardEntry.COLUMN_SALE, jsonMessage.getSalePrice());
        return mNewValues;

    }
}
