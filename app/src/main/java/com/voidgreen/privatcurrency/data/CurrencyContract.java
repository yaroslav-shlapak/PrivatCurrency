package com.voidgreen.privatcurrency.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by y.shlapak on Aug 05, 2015.
 */
public class CurrencyContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.voidgreen.privatcurrency.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_CASH = "cash";
    public static final String PATH_CARD = "card";
    public static final String PATH_WIDGET = "widget";

    ;
    public static final String TABLE_NAME_CARD = "cardTable";

    public static class CurrencyEntry extends MyBaseColumns {
        public static final String COLUMN_BASE_CURRENCY = "baseCurrency";
        public static final String COLUMN_CURRENCY = "currency";
        public static final String COLUMN_BUY = "buyPrice";
        public static final String COLUMN_SALE = "salePrice";

    }

    public static class MyBaseColumns implements BaseColumns {
        public static String getContentType(String path) {
            return ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                    + CONTENT_AUTHORITY + "/" + path;
        }
        public static String getContentItemType(String path) {
            return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                    + CONTENT_AUTHORITY + "/" + path;
        }
        public static Uri getContentUri(String path) {
            return BASE_CONTENT_URI.buildUpon().appendPath(path).build();
        }

        public static Uri getBuildUri(Uri content, long id) {
            return ContentUris.withAppendedId(content, id);
        }
    }

    public static final class CashEntry extends CurrencyEntry{
        public static final Uri CONTENT_URI = getContentUri(PATH_CASH);
        public static final String TABLE_NAME = "cashTable";
        public static final String CONTENT_TYPE = getContentType(PATH_CASH);
        public static final String CONTENT_ITEM_TYPE = getContentItemType(PATH_CASH);

        public static Uri buildCashUri(long id) {
            return getBuildUri(CONTENT_URI, id);
        }
    }

    public static final class CardEntry extends CurrencyEntry{
        public static final Uri CONTENT_URI = getContentUri(PATH_CARD);
        public static final String TABLE_NAME = "cardTable";
        public static final String CONTENT_TYPE = getContentType(PATH_CARD);
        public static final String CONTENT_ITEM_TYPE = getContentItemType(PATH_CARD);

        public static Uri buildCardUri(long id) {
            return getBuildUri(CONTENT_URI, id);
        }
    }

    public static class WidgetEntry extends MyBaseColumns {
        public static final String COLUMN_COLOR = "widgetColor";
        public static final String COLUMN_TYPE = "operationType";
        public static final String COLUMN_UPDATE_INTERVAL = "buyPrice";

        public static final Uri CONTENT_URI = getContentUri(PATH_WIDGET);
        public static final String TABLE_NAME = "widgetTable";
        public static final String CONTENT_TYPE = getContentType(PATH_WIDGET);
        public static final String CONTENT_ITEM_TYPE = getContentItemType(PATH_WIDGET);

    }

}
