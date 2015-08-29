package com.voidgreen.privatcurrency.widget;

/**
 * Created by yaroslav on 8/29/15.
 */
import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new ViewsFactory(this.getApplicationContext(),
                intent));
    }
}