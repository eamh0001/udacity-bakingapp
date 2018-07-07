package com.eamh.bakingapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class RecipesDbHelper extends SQLiteOpenHelper{

    public static class Contract {
        private Contract(){}

        public static class RecipeEntry implements BaseColumns{
            public static final String TABLE_NAME = "recipes";
//            public static final String COLUMN_RECIPE_ID = "_id";
            public static final String COLUMN_NAME = "name";
            public static final String COLUMN_SERVINGS = "servings";
            public static final String COLUMN_IMAGE_URL = "image_url";
        }

        public static class IngredientEntry implements BaseColumns{
            public static final String TABLE_NAME = "ingredients";
            public static final String COLUMN_RECIPE_ID = "recipe_id";
            public static final String COLUMN_NAME = "name";
            public static final String COLUMN_QUANTITY = "quantity";
            public static final String COLUMN_MEASURE = "measure";
        }

        public static class StepEntry implements BaseColumns{
            public static final String TABLE_NAME = "steps";
            public static final String COLUMN_RECIPE_ID = "recipe_id";
            public static final String COLUMN_ORDER = "order";
            public static final String COLUMN_DESCRIPTION_SHORT = "descr_short";
            public static final String COLUMN_DESCRIPTION = "description";
            public static final String COLUMN_IMAGE_URL = "image_url";
            public static final String COLUMN_VIDEO_URL = "video_url";
        }
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Recipes.db";

    public RecipesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_RECIPES_TABLE =
                "CREATE TABLE " + Contract.RecipeEntry.TABLE_NAME + " (" +
                        Contract.RecipeEntry._ID + " INTEGER PRIMARY KEY," +
                        Contract.RecipeEntry.COLUMN_NAME + " TEXT,"+
                        Contract.RecipeEntry.COLUMN_SERVINGS+ " TEXT,"+
                        Contract.RecipeEntry.COLUMN_IMAGE_URL + " TEXT)";

        final String SQL_CREATE_INGREDIENTS_TABLE =
                "CREATE TABLE " + Contract.IngredientEntry.TABLE_NAME + " (" +
                        Contract.IngredientEntry.COLUMN_RECIPE_ID + " INTEGER," +
                        Contract.IngredientEntry.COLUMN_NAME + " TEXT,"+
                        Contract.IngredientEntry.COLUMN_QUANTITY+ " TEXT,"+
                        Contract.IngredientEntry.COLUMN_MEASURE + " TEXT)";

        final String SQL_CREATE_STEPS_TABLE =
                "CREATE TABLE " + Contract.StepEntry.TABLE_NAME + " (" +
                        Contract.StepEntry._ID + " INTEGER PRIMARY KEY," +
                        Contract.StepEntry.COLUMN_RECIPE_ID + " INTEGER," +
                        Contract.StepEntry.COLUMN_DESCRIPTION_SHORT + " TEXT,"+
                        Contract.StepEntry.COLUMN_DESCRIPTION+ " TEXT,"+
                        Contract.StepEntry.COLUMN_VIDEO_URL+ " TEXT,"+
                        Contract.StepEntry.COLUMN_IMAGE_URL + " TEXT)";

        sqLiteDatabase.execSQL(SQL_CREATE_RECIPES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_STEPS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String SQL_DELETE_RECIPES_ENTRIES =
                "DROP TABLE IF EXISTS " + Contract.RecipeEntry.TABLE_NAME;
        final String SQL_DELETE_INGREDIENTS_ENTRIES =
                "DROP TABLE IF EXISTS " + Contract.IngredientEntry.TABLE_NAME;
        final String SQL_DELETE_STEPS_ENTRIES =
                "DROP TABLE IF EXISTS " + Contract.StepEntry.TABLE_NAME;

        sqLiteDatabase.execSQL(SQL_DELETE_RECIPES_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DELETE_INGREDIENTS_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DELETE_STEPS_ENTRIES);

        onCreate(sqLiteDatabase);
    }
}
