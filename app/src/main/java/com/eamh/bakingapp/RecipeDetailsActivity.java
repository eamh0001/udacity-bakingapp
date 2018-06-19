package com.eamh.bakingapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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

    @AfterViews
    void afterViews() {
//        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (recipe != null) {
            toolbar.setTitle(recipe.getName());
            RecipeDetailsFragment fragment = RecipeDetailsFragment_
                    .builder()
                    .arg(RecipeDetailsFragment.INTENT_KEY_SELECTED_RECIPE, recipe)
                    .build();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            // This ID represents the Home or Up button. In the case of this
//            // activity, the Up button is shown. For
//            // more details, see the Navigation pattern on Android Design:
//            //
//            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
//            //
//            navigateUpTo(new Intent(this, MainActivity.class));
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}