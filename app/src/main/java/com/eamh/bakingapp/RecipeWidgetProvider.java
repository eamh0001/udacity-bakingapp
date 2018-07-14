package com.eamh.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.eamh.bakingapp.models.Ingredient;
import com.eamh.bakingapp.models.Recipe;
import com.eamh.bakingapp.widget.IngredientsRemoteViewsService;
import com.eamh.bakingapp.widget.ListRemoteViewsService;
import com.eamh.bakingapp.widget.WidgetService;

import java.util.ArrayList;
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
                                            int[] appWidgetIds, ArrayList<Ingredient> ingredients){
        Log.d(TAG, "updateRecipesWidgets "+ingredients);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, ingredients);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId,
                                ArrayList<Ingredient> ingredients) {
        // Construct the RemoteViews object
        RemoteViews remoteViews;

        if (ingredients != null){
            remoteViews = loadWithRecipeIngrecients(context, ingredients);
        }else {
            remoteViews = loadWithRecipes(context);
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

    private static RemoteViews loadWithRecipes(Context context) {
        Log.d(TAG, "loadWithRecipes ");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setViewVisibility(R.id.appwidget_text, View.GONE);

        Intent remoteAdapterIntent = new Intent(context, ListRemoteViewsService.class);
        views.setRemoteAdapter(R.id.lvRecipes, remoteAdapterIntent);
        views.setEmptyView(R.id.lvRecipes, R.id.appwidget_text2);

        //Mounts the intent that will acts as a template to be filled in with ingredients extra by ListRemoteViewsFactory
        Intent ingredientsIntentTemplate = new Intent(context, WidgetService.class);
        ingredientsIntentTemplate.setAction(WidgetService.ACTION_SHOW_RECIPE_INGREDIENTS);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, ingredientsIntentTemplate, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setPendingIntentTemplate(R.id.lvRecipes, pendingIntent);

        return views;
    }

    private static RemoteViews loadWithRecipeIngrecients(Context context, ArrayList<Ingredient> ingredients) {

        Intent remoteAdapterIntent = new Intent(context, IngredientsRemoteViewsService.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(WidgetService.INTENT_KEY_RECIPE_INGREDIENTS_LIST, ingredients);

        remoteAdapterIntent.putExtra(IngredientsRemoteViewsService.KEY_BUNDLE_EXTRAS, bundle);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setRemoteAdapter(R.id.lvIngredients, remoteAdapterIntent);
        views.setEmptyView(R.id.lvIngredients, R.id.appwidget_text2);

        views.setViewVisibility(R.id.lvRecipes, View.GONE);
        views.setViewVisibility(R.id.lvIngredients, View.VISIBLE);
        return views;
    }
}

