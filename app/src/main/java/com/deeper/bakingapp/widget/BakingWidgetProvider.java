package com.deeper.bakingapp.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.deeper.bakingapp.R;
import com.deeper.bakingapp.data.model.Recipe;
import com.deeper.bakingapp.ui.MainActivity;
import com.deeper.bakingapp.ui.RecipeDetailsActivity;
import com.deeper.bakingapp.ui.StepperActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        Recipe recipe = BakingWidgetConfigureActivity.loadRecipePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews remoteViews = getRecipeGridRemoteView(context, recipe, appWidgetId);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    public static RemoteViews getRecipeGridRemoteView(Context context, Recipe recipe, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);

        Intent intent = new Intent(context, RecipeDetailsActivity.class);
        intent.putExtra(StepperActivity.RECIPE_KEY, recipe);

        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //views.setPendingIntentTemplate(R.id.widget_grid_view, pendingIntent);
        views.setOnClickPendingIntent(R.id.widget_grid_view, pendingIntent);

        if(recipe != null) {
            views.setTextViewText(R.id.food_name, recipe.getName());

            Intent gridIntent = new Intent(context, GridWidgetService.class);
            gridIntent.putExtra(GridWidgetService.KEY_WIDGET, appWidgetId);
            views.setRemoteAdapter(R.id.widget_grid_view, gridIntent);
        }

        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        updateAppWidget(context, appWidgetManager, appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent != null) {
            if ("android.appwidget.action.APPWIDGET_UPDATE".equals(intent.getAction())) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] appwidgetIds = appWidgetManager.getAppWidgetIds(
                        new ComponentName(context, getClass())
                );
                appWidgetManager.notifyAppWidgetViewDataChanged(appwidgetIds, R.id.widget_grid_view);
                onUpdate(context, appWidgetManager, appwidgetIds);
            }
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            BakingWidgetConfigureActivity.deleteRecipePref(context, appWidgetId);
        }
    }
}