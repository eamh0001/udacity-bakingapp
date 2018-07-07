package com.eamh.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.eamh.bakingapp.RecipeWidgetProvider;
import com.eamh.bakingapp.models.Ingredient;
import com.eamh.bakingapp.models.Recipe;

import java.util.ArrayList;

public class WidgetService extends IntentService {

    private static final String TAG = WidgetService.class.getName();

    public static final String ACTION_GET_RECIPES =
            "com.eamh.bakingapp.action.get_recipes";

    public static final String ACTION_GET_RECIPE_INGREDIENTS =
            "com.eamh.bakingapp.action.get_recipe_ingredients";

    public static final String INTENT_KEY_RECIPE_ID = "IKRI";

    public WidgetService() {
        super(TAG);
    }

    public static void startActionGetRecipes(Context context){
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_GET_RECIPES);
        context.startService(intent);
    }

    public static void startActionGetRecipeIngredients(Context context, int recipeId){
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_GET_RECIPES);
        intent.putExtra(INTENT_KEY_RECIPE_ID, recipeId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null){
            String action = intent.getAction();
            Log.d(TAG, "onHandleIntent "+action);
            switch (action){
                case ACTION_GET_RECIPES:
                    handleActionGetRecipes();
                    break;
                case ACTION_GET_RECIPE_INGREDIENTS:
                    if (intent.hasExtra(INTENT_KEY_RECIPE_ID)){
                        int recipeId = intent.getIntExtra(INTENT_KEY_RECIPE_ID, -1);
                        handleActionGetRecipeIngredients(recipeId);
                    }
                    break;
                default:
                    Log.e(TAG, "Action doesn't found: "+action);
            }
        }
    }

    private void handleActionGetRecipes() {
        //TODO getRecipes
        Log.d(TAG, "handleActionGetRecipes");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        //Now update all widgets
        RecipeWidgetProvider.updateRecipesWidgets(this, appWidgetManager, appWidgetIds, new ArrayList<Recipe>(), null);
    }

    private void handleActionGetRecipeIngredients(int recipeId) {
        Log.d(TAG, "handleActionGetRecipeIngredients "+recipeId);
//        if (recipeId > -1){
            //TODO getIngredients
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
            //Now update all widgets
            RecipeWidgetProvider.updateRecipesWidgets(this, appWidgetManager, appWidgetIds, null, new ArrayList<Ingredient>());
//        }
    }
}
