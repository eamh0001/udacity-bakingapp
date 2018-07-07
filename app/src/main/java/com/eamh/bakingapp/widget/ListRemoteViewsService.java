package com.eamh.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.eamh.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

public class ListRemoteViewsService extends RemoteViewsService{

    public static final String INTENT_KEY_INFO = "IKI";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private static final String TAG = ListRemoteViewsFactory.class.getName();
    private final Context context;
    private List<String> smthngs = new ArrayList<>();

    public ListRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        for (int i = 0; i < 10; i++) {
            smthngs.add("Recipe "+(1+i));
        }
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        smthngs.clear();
    }

    @Override
    public int getCount() {
        return smthngs != null ? smthngs.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        String infoText = smthngs.get(i);
        Log.d(TAG, infoText);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        remoteViews.setTextViewText(R.id.tvInfo, infoText);

        Bundle extras = new Bundle();
        extras.putString(ListRemoteViewsService.INTENT_KEY_INFO, infoText);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.tvInfo, fillInIntent);
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