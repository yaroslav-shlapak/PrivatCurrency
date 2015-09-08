package com.voidgreen.privatcurrency.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.voidgreen.privatcurrency.R;
import com.voidgreen.privatcurrency.utilities.Constants;

/**
 * Created by y.shlapak on Aug 27, 2015.
 */
public class CurrencyAdapter extends CursorAdapter {

    public CurrencyAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_todo, viewGroup, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewCurrency = (TextView) view.findViewById(R.id.textViewCurrency);
        TextView textViewBuyPrice = (TextView) view.findViewById(R.id.textViewBuyPrice);
        TextView textViewSalePrice = (TextView) view.findViewById(R.id.textViewSalePrice);

        String currency = cursor.getString(Constants.COL_CURRENCY);
        String buy = cursor.getString(Constants.COL_BUY);
        String sale = cursor.getString(Constants.COL_SALE);

        textViewCurrency.setText(currency);
        textViewBuyPrice.setText(buy);
        textViewSalePrice.setText(sale);
    }
}
