package com.voidgreen.privatcurrency.data;

/**
 * Created by yaroslav on 9/6/15.
 */
public class WidgetCofig {
    public WidgetCofig(int id, int color, String type, int updateInterval) {
        setId(id);
        setColor(color);
        setType(type);
        setUpdateInterval(updateInterval);
    }t

    private int id;
    private int color;
    private String type;
    private int updateInterval;

    public int getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}
