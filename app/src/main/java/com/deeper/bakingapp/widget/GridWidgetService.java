package com.deeper.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.deeper.bakingapp.R;
import com.deeper.bakingapp.data.model.Ingredient;

import java.util.List;

/**
 * Created by Gianni on 09/03/18.
 */

public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    List<Ingredient> mIngredients;

    GridRemoteViewsFactory(Context applicationContext){//, ArrayList<Ingredient> ingredients) {
        mContext = applicationContext;
        //mIngredients = ingredients;
    }

    @Override
    public void onCreate() { }

    @Override
    public void onDataSetChanged() { }

    @Override
    public void onDestroy() { }

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
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.baking_widget_item_layout);
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
