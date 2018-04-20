package com.deeper.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.deeper.bakingapp.R;
import com.deeper.bakingapp.data.model.Ingredient;
import com.deeper.bakingapp.data.model.Recipe;

import java.util.ArrayList;

/**
 * Created by Gianni on 09/03/18.
 */

public class ListWidgetService extends RemoteViewsService {

    public static final String INGREDIENTS_KEY = "ingredients";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(INGREDIENTS_KEY, -1);

        Recipe recipe = BakingAppWidgetConfigureActivity
                .loadTitlePref(this.getApplicationContext(), appWidgetId);

        return new ListRemoteViewsFactory(this.getApplicationContext(),
                new ArrayList<>(recipe.getIngredients()));
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext;
        ArrayList<Ingredient> ingredientList;

        ListRemoteViewsFactory(Context applicationContext, ArrayList<Ingredient> ingredients) {
            mContext = applicationContext;
            ingredientList = ingredients;
        }

        @Override
        public void onCreate() {

        }

        //called on start and when notifyAppWidgetViewDataChanged is called
        @Override
        public void onDataSetChanged() {
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return (ingredientList != null) ? ingredientList.size() : 0;
        }

        /**
         * This method acts like the onBindViewHolder method in an Adapter
         *
         * @param position The current position of the item in the GridView to be displayed
         * @return The RemoteViews object to display for the provided postion
         */
        @Override
        public RemoteViews getViewAt(int position) {
            Ingredient ingredient = ingredientList.get(position);

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.baking_widget_item_layout);
            remoteViews.setTextViewText(R.id.ingredient_name, ingredient.getIngredient());
            remoteViews.setTextViewText(R.id.ingredient_quantity, String.valueOf(ingredient.getQuantity()));

            return remoteViews;

        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1; // Treat all items in the GridView the same
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
