package com.deeper.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.deeper.bakingapp.R;

public class BakingAppWidgetUpdateService extends IntentService {

    public static final String ACTION_UPDATE_LIST_VIEW = "com.deeper.bakingapp.widget.bakingappwidgeupdateservice.update_widget_list";

    public BakingAppWidgetUpdateService() {
        super(BakingAppWidgetUpdateService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if(ACTION_UPDATE_LIST_VIEW.equals(action)){
                handleActionUpdateListView();
            }
        }
    }

    private void handleActionUpdateListView() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));

        BakingAppWidget.updateAllAppWidget(this, appWidgetManager, appWidgetIds);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
    }

    public static void updateListWidgets(Context context) {
        Intent intent = new Intent(context, BakingAppWidgetUpdateService.class);
        intent.setAction(ACTION_UPDATE_LIST_VIEW);
        context.startService(intent);
    }

}
