package com.eamh.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eamh.bakingapp.adapters.recipes.RecipesAdapter;
import com.eamh.bakingapp.api.BakingApi;
import com.eamh.bakingapp.api.CustomRestErrorHandler;
import com.eamh.bakingapp.api.IdlingResource.SimpleIdlingResource;
import com.eamh.bakingapp.fragments.RecipeDetailsFragment;
import com.eamh.bakingapp.fragments.RecipeDetailsFragment_;
import com.eamh.bakingapp.fragments.RecipeInfoFragment;
import com.eamh.bakingapp.fragments.RecipeInfoFragment_;
import com.eamh.bakingapp.fragments.RecipeStepFragment;
import com.eamh.bakingapp.fragments.RecipeStepFragment_;
import com.eamh.bakingapp.models.Recipe;
import com.eamh.bakingapp.models.Step;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeStepActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 *
 * Drawner info: http://www.sgoliver.net/blog/interfaz-de-usuario-en-android-navigation-drawer/
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity
        implements
        RecipesAdapter.RowClickListener,
        CustomRestErrorHandler.Listener,
        RecipeInfoFragment.RecipeInfoFragmentListener {

    private static final String TAG = MainActivity.class.getName();
    private static final String STATE_KEY_RECYCLERVIEW = "SKR";

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @RestService
    BakingApi bakingApi;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.rvRecipeList)
    RecyclerView rvRecipes;

    @ViewById(R.id.pbLoadingIndicator)
    ProgressBar pbLoading;

    @ViewById(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @ViewById(R.id.fragmentContainerMaster)
    FrameLayout containerMaster;

    @ViewById(R.id.fragmentContainerDetail)
    FrameLayout containerDetail;

    @InstanceState
    ArrayList<Recipe> recipes;

    private boolean twoPanelsMode;

    @InstanceState
    boolean isRecipeInfoFragmentInitialized;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIdlingResource();
    }

    @AfterInject
    void afterInject(){
        bakingApi.setRestErrorHandler(new CustomRestErrorHandler(this));
    }

    @AfterViews
    void afterViews(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        twoPanelsMode = findViewById(R.id.twoPanelContainer) != null;
        rvRecipes.setAdapter(new RecipesAdapter(this));

        if (recipes != null) {
            showRecipes();
            showDrawner(!isRecipeInfoFragmentInitialized);
        }else {
            setIdlingResourceStatus(false);
            getBakingData();
        }
    }

    @Background
    void getBakingData(){
        showLoadingBar(true);
        List<Recipe> recipeList = bakingApi.getBakingData();
        if (recipeList != null){
            recipes = new ArrayList<>(recipeList);
            showDrawner(true);
        }else {
            recipes = new ArrayList<>();
        }
        showRecipes();
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void showRecipes(){
        showLoadingBar(false);
        ((RecipesAdapter) rvRecipes.getAdapter()).setRecipes(recipes);
        setIdlingResourceStatus(true);
    }

    @Override
    public void onRestError() {
        showLoadingBar(false);
        Snackbar.make(rvRecipes, R.string.text_server_error, Snackbar.LENGTH_LONG).show();
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void showLoadingBar(boolean show) {
        pbLoading.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void showDrawner(boolean show){
        if (show) {
            drawerLayout.openDrawer(GravityCompat.START);
        }else {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId()) {
            case android.R.id.home:
                showDrawner(true);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        toolbar.setTitle(recipe.getName());
        initRecipeInfoFragment(recipe);
        drawerLayout.closeDrawers();
        if (twoPanelsMode) {
            onRecipeInfoSelected(recipe);
        }
    }

    @Override
    public void onRecipeInfoSelected(Recipe recipe) {
        Toast.makeText(this, recipe.getName(), Toast.LENGTH_LONG).show();
        if (twoPanelsMode) {
            toolbar.setTitle(recipe.getName());
            RecipeDetailsFragment recipeDetailsFragment = RecipeDetailsFragment_
                    .builder()
                    .arg(RecipeDetailsFragment.INTENT_KEY_SELECTED_RECIPE, recipe)
                    .build();
            replaceFragmentAt(recipeDetailsFragment, R.id.fragmentContainerDetail);
        }else {
            RecipeDetailsActivity_.intent(this).recipe(recipe).start();
        }
    }

    @Override
    public void onRecipeStepSelected(Step step) {
        Toast.makeText(this, step.getShortDescription(), Toast.LENGTH_LONG).show();
        if (twoPanelsMode) {
            RecipeStepFragment recipeStepFragment = RecipeStepFragment_
                    .builder()
                    .arg(RecipeStepFragment.INTENT_KEY_SELECTED_STEP, step)
                    .build();
            replaceFragmentAt(recipeStepFragment, R.id.fragmentContainerDetail);
        }else {
            RecipeStepActivity_.intent(this).step(step).start();
        }
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    private void setIdlingResourceStatus(boolean status){
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(status);
        }
    }

    private void initRecipeInfoFragment(Recipe recipe) {
        RecipeInfoFragment recipeInfoFragment = RecipeInfoFragment_
                .builder()
                .arg(RecipeInfoFragment.INTENT_KEY_SELECTED_RECIPE, recipe)
                .build();
        replaceFragmentAt(recipeInfoFragment, R.id.fragmentContainerMaster);
        isRecipeInfoFragmentInitialized = true;
    }

    private void replaceFragmentAt(Fragment fragment, int container){
        getSupportFragmentManager().beginTransaction()
                .replace(container, fragment)
                .commit();
    }
}
