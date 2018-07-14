package com.eamh.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.eamh.bakingapp.R;
import com.eamh.bakingapp.api.BakingApi;
import com.eamh.bakingapp.models.Recipe;

import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.List;

public class ListRemoteViewsService extends RemoteViewsService{


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return ListRemoteViewsFactory_.getInstance_(this.getApplicationContext());
//        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

@EBean
class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private static final String TAG = ListRemoteViewsFactory.class.getName();
    private final Context context;
    private List<Recipe> recipes;
//    private List<String> recipes = new ArrayList<>();

    @RestService
    BakingApi bakingApi;

    public ListRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() { }

    @Override
    public void onDataSetChanged() {
        recipes = bakingApi.getBakingData();

//        for (int i = 0; i < 10; i++) {
//            recipes.add("Recipe "+(1+i));
//        }
    }

    @Override
    public void onDestroy() {
        if (recipes != null)
            recipes.clear();
    }

    @Override
    public int getCount() {
        return recipes != null ? recipes.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        Log.d(TAG, "getViewAt "+i);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        if (recipes != null){
            Recipe recipe = recipes.get(i);
            Log.d(TAG, recipe.toString());
            remoteViews.setTextViewText(R.id.tvInfo, recipe.getName());

            //Fills the IntentTemplate from RecipeWidgetProvider
            Bundle extras = new Bundle();
            extras.putParcelableArrayList(WidgetService.INTENT_KEY_RECIPE_INGREDIENTS_LIST, recipe.getIngredients());
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            remoteViews.setOnClickFillInIntent(R.id.flRoot, fillInIntent);
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