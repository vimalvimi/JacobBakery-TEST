package com.roxybakestudio.jacobbakery.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.roxybakestudio.jacobbakery.R;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientWidget extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: " + intent.getAction());
        super.onReceive(context, intent);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        final CharSequence recipeId = IngredientConfiguration.loadTitlePref(context, appWidgetId);

        String recipeIdInt = recipeId.toString();

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);

        Intent intent = new Intent(context, ListViewService.class);
        remoteViews.setRemoteAdapter(R.id.widget_listview, intent);

        remoteViews.setEmptyView(R.id.widget_listview, R.id.empty_view);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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

