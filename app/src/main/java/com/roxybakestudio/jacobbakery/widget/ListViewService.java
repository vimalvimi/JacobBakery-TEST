package com.roxybakestudio.jacobbakery.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.roxybakestudio.jacobbakery.R;
import com.roxybakestudio.jacobbakery.data.RecipeContract;
import com.roxybakestudio.jacobbakery.rest.RetrofitCall;

public class ListViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Cursor mCursor;

    int mRecipeId = 1;

    public ListRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        RetrofitCall.getRecipes(mContext);
    }

    @Override
    public void onDataSetChanged() {

        String recipeIdString = Integer.toString(mRecipeId);

        String[] projectionIngredients = {
                RecipeContract.RecipeIngredients.COLUMN_RECIPE_NAME,
                RecipeContract.RecipeIngredients.COLUMN_QUANTITY,
                RecipeContract.RecipeIngredients.COLUMN_MEASURE,
                RecipeContract.RecipeIngredients.COLUMN_INGREDIENT
        };

        Uri uriIngredients = RecipeContract.RecipeIngredients
                .buildIngredientUriWithId(Long.parseLong(recipeIdString));

        mCursor = mContext.getContentResolver().query(
                uriIngredients,
                projectionIngredients,
                null,
                null,
                null);
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(position);

        String title = mCursor.getString(RecipeContract.RecipeIngredients.INDEX_COLUMN_RECIPE_NAME);
        String quantity = mCursor.getString(RecipeContract.RecipeIngredients.INDEX_COLUMN_QUANTITY);
        String measure = mCursor.getString(RecipeContract.RecipeIngredients.INDEX_COLUMN_MEASURE);
        String ingredient = mCursor.getString(RecipeContract.RecipeIngredients.INDEX_COLUMN_INGREDIENT);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_ingredients_widget);
        views.setTextViewText(R.id.widget_listview_title, title);
        views.setTextViewText(R.id.widget_ingredient_quantity_listitem, quantity);
        views.setTextViewText(R.id.widget_ingredient_measure_listitem, measure);
        views.setTextViewText(R.id.widget_ingredient_listitem, ingredient);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
