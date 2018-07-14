package com.eamh.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.eamh.bakingapp.R;
import com.eamh.bakingapp.RecipeWidgetProvider;
import com.eamh.bakingapp.models.Ingredient;
import com.eamh.bakingapp.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class WidgetService extends IntentService {

    private static final String TAG = WidgetService.class.getName();

    public static final String ACTION_GET_RECIPES =
            "com.eamh.bakingapp.action.get_recipes";

    public static final String ACTION_SHOW_RECIPE_INGREDIENTS =
            "com.eamh.bakingapp.action.show_recipe_ingredients";

    public static final String INTENT_KEY_RECIPE_INGREDIENTS_LIST = "IKRIL";

    public WidgetService() {
        super(TAG);
    }

    public static void startActionGetRecipes(Context context){
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_GET_RECIPES);
        context.startService(intent);
    }

    public static void startActionGetRecipeIngredients(Context context, ArrayList<Ingredient> ingredients){
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_GET_RECIPES);
        intent.putParcelableArrayListExtra(INTENT_KEY_RECIPE_INGREDIENTS_LIST, ingredients);
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
                case ACTION_SHOW_RECIPE_INGREDIENTS:
                    if (intent.hasExtra(INTENT_KEY_RECIPE_INGREDIENTS_LIST)){
                        ArrayList<Ingredient> ingredients = intent.getParcelableArrayListExtra(INTENT_KEY_RECIPE_INGREDIENTS_LIST);
                        handleActionGetRecipeIngredients(ingredients);
                    }
                    break;
                default:
                    Log.e(TAG, "Action doesn't found: "+action);
            }
        }
    }

    private void handleActionGetRecipes() {
        Log.d(TAG, "handleActionGetRecipes");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lvRecipes);
        //Now update all widgets
        RecipeWidgetProvider.updateRecipesWidgets(this, appWidgetManager, appWidgetIds, null);
    }

    private void handleActionGetRecipeIngredients(ArrayList<Ingredient> ingredients) {
        Log.d(TAG, "handleActionGetRecipeIngredients "+ingredients);
        if (ingredients != null){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
            //Now update all widgets
            RecipeWidgetProvider.updateRecipesWidgets(this, appWidgetManager, appWidgetIds, ingredients);
        }
    }
}
