package com.deeper.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.deeper.bakingapp.R;
import com.deeper.bakingapp.data.model.Ingredient;
import com.deeper.bakingapp.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gianni on 09/03/18.
 */

public class GridWidgetService extends RemoteViewsService {

    public static final String KEY_WIDGET = "id_ingredients";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(KEY_WIDGET, -1);

        Recipe recipe = BakingWidgetConfigureActivity.loadRecipePref(this.getApplicationContext(),
                appWidgetId);

        return new GridRemoteViewsFactory(this.getApplicationContext(),
                new ArrayList<>(recipe.getIngredients()));
    }

    class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext;
        List<Ingredient> mIngredients;

        GridRemoteViewsFactory(Context applicationContext, ArrayList<Ingredient> ingredients) {
            mContext = applicationContext;
            mIngredients = ingredients;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            if (mIngredients != null && mIngredients.size() > 0) return mIngredients.size();
            return 0;
        }

        /**
         * This method acts like the onBindViewHolder method in an Adapter
         *
         * @param position The current position of the item in the GridView to be displayed
         * @return The RemoteViews object to display for the provided postion
         */
        @Override
        public RemoteViews getViewAt(int position) {
            Ingredient ingredient = mIngredients.get(position);

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.baking_widget_item_layout);
            views.setTextViewText(R.id.ingredient_name, ingredient.getIngredient());
            views.setTextViewText(R.id.ingredient_quantity, String.valueOf(ingredient.getQuantity()));

            return views;
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