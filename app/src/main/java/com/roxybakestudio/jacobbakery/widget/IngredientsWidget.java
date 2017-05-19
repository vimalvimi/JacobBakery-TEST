package com.roxybakestudio.jacobbakery.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.roxybakestudio.jacobbakery.R;
import com.roxybakestudio.jacobbakery.model.Ingredients;
import com.roxybakestudio.jacobbakery.model.Recipe;
import com.roxybakestudio.jacobbakery.rest.RecipeService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link IngredientsWidgetConfigureActivity IngredientsWidgetConfigureActivity}
 */
public class IngredientsWidget extends AppWidgetProvider {

    private static final String TAG = "IngredientsWidget";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: " + intent.getAction());
        super.onReceive(context, intent);
    }

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        final CharSequence widgetText = IngredientsWidgetConfigureActivity.loadTitlePref(context, appWidgetId);

        RecipeService recipeService = RecipeService.retrofit.create(RecipeService.class);
        Call<List<Recipe>> listCall = recipeService.getAllRecipes();
        listCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();

                ArrayList<Ingredients> ingredients = new ArrayList<>();

                for (int i = 0; i < recipes.size(); i++) {
                    if (widgetText.equals(recipes.get(i).getName())) {
                        Recipe recipe = recipes.get(i);
                        for (int j = 0; j < recipe.getIngredients().size(); j++) {
                            ingredients.add(new Ingredients(
                                    recipe.getIngredients().get(j).getQuantity(),
                                    recipe.getIngredients().get(j).getMeasure(),
                                    recipe.getIngredients().get(j).getIngredient()));
                        }
                    }
                }
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
                views.setTextViewText(R.id.appwidget_listview, recipes.get(1).toString());
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(final Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            IngredientsWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }


    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}

