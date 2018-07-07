package com.eamh.bakingapp.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class RecipesProvider extends ContentProvider {

    public static final String AUTHORITY = "com.eamh.bakingapp";
    public static final Uri CONTENT_BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri CONTENT_RECIPES_URI = CONTENT_BASE_URI.buildUpon()
            .appendPath(RecipesDbHelper.Contract.RecipeEntry.TABLE_NAME)
            .build();
    public static final Uri CONTENT_INGREDIENTS_URI = CONTENT_BASE_URI.buildUpon()
            .appendPath(RecipesDbHelper.Contract.IngredientEntry.TABLE_NAME)
            .build();
    public static final Uri CONTENT_STEPS_URI = CONTENT_BASE_URI.buildUpon()
            .appendPath(RecipesDbHelper.Contract.StepEntry.TABLE_NAME)
            .build();

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    /** The match code for some items in the recipe table. */
    private static final int CODE_RECIPES_DIR = 1;

    /** The match code for an item in the recipe table. */
    private static final int CODE_RECIPE_ITEM = 2;

    /** The match code for some items in the recipe table. */
    private static final int CODE_INGREDIENTS_OF_RECIPE = 3;

    /** The match code for some items in the recipe table. */
    private static final int CODE_STEPS_OF_RECIPE = 5;

    static {
        MATCHER.addURI(AUTHORITY, RecipesDbHelper.Contract.RecipeEntry.TABLE_NAME, CODE_RECIPES_DIR);
        MATCHER.addURI(AUTHORITY, RecipesDbHelper.Contract.RecipeEntry.TABLE_NAME + "/#", CODE_RECIPE_ITEM);
        MATCHER.addURI(AUTHORITY, RecipesDbHelper.Contract.IngredientEntry.TABLE_NAME + "/#", CODE_INGREDIENTS_OF_RECIPE);
        MATCHER.addURI(AUTHORITY, RecipesDbHelper.Contract.StepEntry.TABLE_NAME + "/#", CODE_STEPS_OF_RECIPE);
    }

    public RecipesProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        final Context context = getContext();
        if (context == null) {
            return null;
        }

        SQLiteDatabase db = new RecipesDbHelper(context).getReadableDatabase();

        String recipeId;
        String[] selectionArguments;

        switch (MATCHER.match(uri)){

            case CODE_RECIPES_DIR:
                cursor = db.query(RecipesDbHelper.Contract.RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_RECIPE_ITEM:
                recipeId = uri.getLastPathSegment();
                selectionArguments = new String[]{recipeId};
                cursor = db.query(RecipesDbHelper.Contract.RecipeEntry.TABLE_NAME,
                        projection,
                        RecipesDbHelper.Contract.RecipeEntry._ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_INGREDIENTS_OF_RECIPE:
                recipeId = uri.getLastPathSegment();
                selectionArguments = new String[]{recipeId};
                cursor = db.query(RecipesDbHelper.Contract.IngredientEntry.TABLE_NAME,
                        projection,
                        RecipesDbHelper.Contract.IngredientEntry.COLUMN_RECIPE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_STEPS_OF_RECIPE:
                recipeId = uri.getLastPathSegment();
                selectionArguments = new String[]{recipeId};
                cursor = db.query(RecipesDbHelper.Contract.StepEntry.TABLE_NAME,
                        projection,
                        RecipesDbHelper.Contract.StepEntry.COLUMN_RECIPE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

//        db.close();
        cursor.setNotificationUri(context.getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
