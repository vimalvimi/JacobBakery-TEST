package com.roxybakestudio.jacobbakery.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.roxybakestudio.jacobbakery.R;
import com.roxybakestudio.jacobbakery.adapter.RecipeAdapterWidget;
import com.roxybakestudio.jacobbakery.data.RecipeContract;
import com.roxybakestudio.jacobbakery.helper.Utils;
import com.roxybakestudio.jacobbakery.rest.RetrofitCall;

public class IngredientConfiguration extends Activity implements RecipeAdapterWidget.ClickListener {

    private static final String TAG = "IngredientConfiguration";

    private static final String PREFS_NAME = "com.roxybakestudio.jacobbakery.widget.IngredientWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    RecyclerView mRecyclerView;
    TextView mTextViewNoInternet;

    Cursor mCursor;

    private RecipeAdapterWidget recipeAdapter;

    public IngredientConfiguration() {
    }

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.ingredient_widget_config);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }


        mTextViewNoInternet = (TextView) findViewById(R.id.widget_no_internet_error_text);
        mRecyclerView = (RecyclerView) findViewById(R.id.widget_recycler_view_recipes);

        if (Utils.isNetworkAvailable(this)) {
            RetrofitCall.getRecipes(this);
            getRecyclerView();
        } else {
            getError();
        }
    }

    private void getRecyclerView() {
        Log.d(TAG, "getRecyclerView: ");
        mTextViewNoInternet.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, Utils.getScreenWidth(this));

        mRecyclerView.setLayoutManager(layoutManager);
        recipeAdapter = new RecipeAdapterWidget(this,this);
        mRecyclerView.setAdapter(recipeAdapter);

        getDataFromCursor();
    }

    private void getError() {
        mRecyclerView.setVisibility(View.GONE);
        mTextViewNoInternet.setVisibility(View.VISIBLE);
    }

    public void getDataFromCursor() {
        String[] projection = {
                RecipeContract.RecipeMain.COLUMN_NAME,
                RecipeContract.RecipeMain.COLUMN_RECIPE_ID,
                RecipeContract.RecipeMain.COLUMN_SERVINGS,
        };

        mCursor = this.getContentResolver().query(
                RecipeContract.RecipeMain.CONTENT_URI,
                projection,
                null,
                null,
                null);

        recipeAdapter.swapCursor(mCursor);
        if (mCursor != null && mCursor.getCount() != 0) {
            mCursor.moveToFirst();
        }
    }

    @Override
    public void itemClicked(View view, int position) {
        Log.d(TAG, "itemClicked: HAHA" + position);

        final Context context = IngredientConfiguration.this;

        //SaveTitlePref
        saveTitlePref(context, mAppWidgetId, String.valueOf(position));

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        IngredientWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();

    }

    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }
}
