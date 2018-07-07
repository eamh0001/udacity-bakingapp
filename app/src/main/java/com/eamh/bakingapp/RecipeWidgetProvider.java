package com.eamh.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.eamh.bakingapp.models.Ingredient;
import com.eamh.bakingapp.models.Recipe;
import com.eamh.bakingapp.widget.ListRemoteViewsService;
import com.eamh.bakingapp.widget.WidgetService;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String TAG = RecipeWidgetProvider.class.getName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        WidgetService.startActionGetRecipes(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateRecipesWidgets(Context context, AppWidgetManager appWidgetManager,
                                            int[] appWidgetIds, List<Recipe> recipes, List<Ingredient> ingredients){
        Log.d(TAG, "updateRecipesWidgets "+ingredients+" "+recipes);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipes, ingredients);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId,
                                List<Recipe> recipes, List<Ingredient> ingredients) {
        // Construct the RemoteViews object
        RemoteViews remoteViews;

        if (ingredients != null){
            remoteViews = loadWithRecipeIngrecients(context, ingredients);
        }else {
            if (recipes != null){
                remoteViews = loadWithRecipes(context, recipes);
            }else {
                remoteViews = loadEmptyView(context);
            }
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private static RemoteViews loadEmptyView(Context context) {
        Log.d(TAG, "loadEmptyView ");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        CharSequence widgetText = "potato";
        views.setTextViewText(R.id.appwidget_text, widgetText);
        Intent getRecipesIntent = new Intent(context, WidgetService.class);
        getRecipesIntent.setAction(WidgetService.ACTION_GET_RECIPES);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, getRecipesIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        return views;
    }

    private static RemoteViews loadWithRecipes(Context context, List<Recipe> recipes) {
        Log.d(TAG, "loadWithRecipes ");
        //TODO load recipes listview and manage click
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setViewVisibility(R.id.appwidget_text, View.GONE);

//        CharSequence widgetText = "loadWithRecipes";
//        views.setTextViewText(R.id.appwidget_text, widgetText);
        Intent ingredientsIntent = new Intent(context, WidgetService.class);
        ingredientsIntent.setAction(WidgetService.ACTION_GET_RECIPE_INGREDIENTS);
        ingredientsIntent.putExtra(WidgetService.INTENT_KEY_RECIPE_ID, 0);
//        PendingIntent pendingIntent = PendingIntent.getService(context, 0, ingredientsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        Intent intent = new Intent(context, ListRemoteViewsService.class);
        views.setRemoteAdapter(R.id.lvRecipes, intent);

        PendingIntent pendingIntent = PendingIntent.getService(context, 0, ingredientsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.appwidget_text, pendingIntent);
        views.setEmptyView(R.id.lvRecipes, R.id.appwidget_text2);
        return views;
    }

    private static RemoteViews loadWithRecipeIngrecients(Context context, List<Ingredient> ingredients) {
        Log.d(TAG, "loadWithRecipeIngrecients ");
        //TODO load ingredients listview
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        CharSequence widgetText = "loadWithRecipeIngrecients";
        views.setTextViewText(R.id.appwidget_text2, widgetText);
        views.setViewVisibility(R.id.appwidget_text, View.GONE);
        views.setViewVisibility(R.id.appwidget_text2, View.VISIBLE);
        return views;
    }
}

