package com.roxybakestudio.jacobbakery.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.roxybakestudio.jacobbakery.R;
import com.roxybakestudio.jacobbakery.adapter.RecipeAdapter;
import com.roxybakestudio.jacobbakery.model.Recipe;
import com.roxybakestudio.jacobbakery.rest.RecipeService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientsWidgetConfigureActivity extends Activity implements RecipeAdapter.ClickListener {

    private static final String TAG = "IngredientsWidgetConfig";

    private static final String PREFS_NAME = "com.roxybakestudio.jacobbakery.widget.IngredientsWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private RecipeAdapter recipeAdapter;

    public IngredientsWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.ingredients_widget_configure);

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

        getFeed();
        configViews();
    }

    private void getFeed() {
        RecipeService recipeService = RecipeService.retrofit.create(RecipeService.class);
        Call<List<Recipe>> listCall = recipeService.getAllRecipes();
        listCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if (response.isSuccessful()) {
                    List<Recipe> recipes = response.body();

                    for (int i = 0; i < recipes.size(); i++) {
                        Recipe recipe = recipes.get(i);
                        recipeAdapter.addRecipe(recipe);
                    }
                } else {
                    Log.d(TAG, "RESPONSE CODE ERROR " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_recipes_widget);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, getScreenWidth());

        recyclerView.setLayoutManager(layoutManager);
        recipeAdapter = new RecipeAdapter(this);

        recipeAdapter.setClickListener(this);
        recyclerView.setAdapter(recipeAdapter);
    }

    @Override
    public void itemClicked(View view, int position) {
        Log.d(TAG, "itemClicked: " + position);

        final Context context = IngredientsWidgetConfigureActivity.this;

        Recipe recipe = recipeAdapter.getRecipes().get(position);

        //SaveTitlePref
        saveTitlePref(context, mAppWidgetId, recipe.getName());

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        IngredientsWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();

    }

    public int getScreenWidth() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return 1;
        } else {
            return 2;
        }
    }

    // Write the prefix to the SharedPreferences object for this widget
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

