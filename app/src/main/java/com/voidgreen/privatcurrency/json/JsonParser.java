package com.voidgreen.privatcurrency.json;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by y.shlapak on Jul 29, 2015.
 */
public class JsonParser {

    public static List readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readMessagesArray(reader);
        } finally {
            reader.close();
        }
    }

    public static List readMessagesArray(JsonReader reader) throws IOException {
        List messages = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    public static JsonMessage readMessage(JsonReader reader) throws IOException {
        String currency = null;
        String baseCurrency = null;
        String buyPrice = null;
        String salePrice = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(JsonMessage.CURRENCY)) {
                currency = reader.nextString();
            } else if (name.equals(JsonMessage.BASE_CURRENCY)) {
                baseCurrency = reader.nextString();
            } else if (name.equals(JsonMessage.BUY)) {
                buyPrice = reader.nextString();
            } else if (name.equals(JsonMessage.SALE)) {
                salePrice = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new JsonMessage(currency, baseCurrency, buyPrice, salePrice);
    }

    public List readDoublesArray(JsonReader reader) throws IOException {
        List doubles = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            doubles.add(reader.nextDouble());
        }
        reader.endArray();
        return doubles;
    }
}


