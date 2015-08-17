package com.voidgreen.privatcurrency.json;

/**
 * Created by y.shlapak on Jul 29, 2015.
 */
public class JsonMessage {
    public static final String CURRENCY = "ccy";
    public static final String BASE_CURRENCY = "base_ccy";
    public static final String BUY = "buy";
    public static final String SALE = "sale";

    private String currency;
    private String baseCurrency;
    private String buyPrice;
    private String salePrice;

    public JsonMessage(String currency, String baseCurrency, String buyPrice, String salePrice) {
        this.currency = currency;
        this.baseCurrency = baseCurrency;
        this.buyPrice = buyPrice;
        this.salePrice = salePrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }



}
