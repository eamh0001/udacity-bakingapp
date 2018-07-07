package com.eamh.bakingapp.database;

import com.eamh.bakingapp.models.Recipe;

import java.util.List;

public interface RecipePersistenceManager {

    void addRecipe(Recipe recipe);
    void getRecipe(int recipeId);
    void removeRecipe(int recipeId);
    void getAllRecipes();

    interface ResponseListener{
        void onDatabaseRecipesReceived(List<Recipe> databaseRecipes);
        void onDatabaseRecipeReceived(Recipe recipe);
        void onDatabaseError(String error, int errorCode);
    }

    final class ErrorCode{
        public static final int CREATE = 0;
        public static final int RETRIEVE = 1;
        public static final int UPDATE = 2;
        public static final int DELETE = 3;
    }
}
