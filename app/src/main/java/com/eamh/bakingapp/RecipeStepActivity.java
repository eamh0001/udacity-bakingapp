package com.eamh.bakingapp;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.eamh.bakingapp.fragments.RecipeStepFragment;
import com.eamh.bakingapp.fragments.RecipeStepFragment_;
import com.eamh.bakingapp.models.Step;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MainActivity}.
 */
@EActivity(R.layout.activity_recipe_step)
public class RecipeStepActivity extends AppCompatActivity {

    @ViewById(R.id.detailToolbar)
    Toolbar toolbar;

    @InstanceState
    boolean isInitialized;

    @Extra(RecipeStepFragment.INTENT_KEY_SELECTED_STEP)
    Step step;

    @AfterViews
    void afterViews(){
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (step != null && !isInitialized) {
            RecipeStepFragment fragment = RecipeStepFragment_
                    .builder()
                    .arg(RecipeStepFragment.INTENT_KEY_SELECTED_STEP, step)
                    .build();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipeStepContainer, fragment)
                    .commit();
            isInitialized = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
