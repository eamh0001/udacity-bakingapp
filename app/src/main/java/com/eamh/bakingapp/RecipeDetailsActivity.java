package com.eamh.bakingapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.eamh.bakingapp.fragments.RecipeDetailsFragment;
import com.eamh.bakingapp.fragments.RecipeDetailsFragment_;
import com.eamh.bakingapp.models.Recipe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_recipe_details)
public class RecipeDetailsActivity extends AppCompatActivity {

    public static final String INTENT_KEY_SELECTED_RECIPE = "IKSS";

    @ViewById(R.id.detailToolbar)
    Toolbar toolbar;

    @InstanceState
    @Extra(INTENT_KEY_SELECTED_RECIPE)
    Recipe recipe;

    @InstanceState
    boolean isInitialized;

    @AfterViews
    void afterViews() {
        // Show the Up button in the action bar.
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (recipe != null && !isInitialized) {
            toolbar.setTitle(recipe.getName());
            RecipeDetailsFragment fragment = RecipeDetailsFragment_
                    .builder()
                    .arg(RecipeDetailsFragment.INTENT_KEY_SELECTED_RECIPE, recipe)
                    .build();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            isInitialized = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
