package com.eamh.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.eamh.bakingapp.R;
import com.eamh.bakingapp.models.Ingredient;
import com.eamh.bakingapp.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class IngredientsRemoteViewsService extends RemoteViewsService{

    public static final String KEY_BUNDLE_EXTRAS = "KBE";
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        List<Ingredient> ingredients = null;

        if (intent.hasExtra(KEY_BUNDLE_EXTRAS)) {
            Bundle extras = intent.getBundleExtra(KEY_BUNDLE_EXTRAS);
            ingredients = extras.getParcelableArrayList(WidgetService.INTENT_KEY_RECIPE_INGREDIENTS_LIST);
        }
        if (ingredients == null){
            ingredients = new ArrayList<>();
        }

        return new IngredientsRemoteViewsFactory(this.getApplicationContext(), ingredients);
    }
}

class IngredientsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private static final String TAG = ListRemoteViewsFactory.class.getName();
    private final Context context;
    private List<Ingredient> ingredients;

    public IngredientsRemoteViewsFactory(Context context, List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @Override
    public void onCreate() { }

    @Override
    public void onDataSetChanged() { }

    @Override
    public void onDestroy() {
        if (ingredients != null)
            ingredients.clear();
    }

    @Override
    public int getCount() {
        return ingredients != null ? ingredients.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        Log.d(TAG, "getViewAt "+i);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        if (ingredients != null){
            Ingredient ingredient = ingredients.get(i);
            Log.d(TAG, ingredient.toString());
            remoteViews.setTextViewText(R.id.tvInfo, context.getString(R.string.format_ingredient_details,ingredient.getQuantity(), ingredient.getMeasure(), ingredient.getIngredient()));
        }
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;// Treat all items in the GridView the same
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