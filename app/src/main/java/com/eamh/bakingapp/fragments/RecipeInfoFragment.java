package com.eamh.bakingapp.fragments;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.eamh.bakingapp.R;
import com.eamh.bakingapp.adapters.steps.RecipeStepsAdapter;
import com.eamh.bakingapp.models.Recipe;
import com.eamh.bakingapp.models.Step;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_recipe_info)
public class RecipeInfoFragment extends Fragment implements RecipeStepsAdapter.RowClickListener {

    public interface RecipeInfoFragmentListener {
        void onRecipeInfoSelected(Recipe recipe);
        void onRecipeStepSelected(Step step);
    }

    public static final String INTENT_KEY_SELECTED_RECIPE = "IKSR";

    @ViewById(R.id.tvInfo)
    TextView tvInfo;

    @ViewById(R.id.rvRecipeSteps)
    RecyclerView rvRecipeSteps;

    @InstanceState
    Recipe recipe;

    @InstanceState
    Parcelable listState;

    @InstanceState
    int scrollPosition;

    private RecipeStepsAdapter recipeStepsAdapter;
    private RecipeInfoFragmentListener fragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeInfoFragmentListener) {
            fragmentListener = (RecipeInfoFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RecipeInfoFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    @Click
    void tvInfoClicked(){
        fragmentListener.onRecipeInfoSelected(recipe);
    }

    @Override
    public void onPause() {
        super.onPause();
        listState = rvRecipeSteps.getLayoutManager().onSaveInstanceState();
        LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) rvRecipeSteps.getLayoutManager());
        scrollPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
    }

    @AfterViews
    void afterViews(){
        if (recipe == null && (getArguments() != null && getArguments().containsKey(INTENT_KEY_SELECTED_RECIPE))) {
            recipe = getArguments().getParcelable(INTENT_KEY_SELECTED_RECIPE);
        }

        if (recipeStepsAdapter == null) {
            recipeStepsAdapter = new RecipeStepsAdapter(this);
            rvRecipeSteps.setAdapter(recipeStepsAdapter);
        }


        if (recipe != null){
            refreshUI();
        }
    }

    public void setData(Recipe recipe) {
        this.recipe = recipe;
        refreshUI();
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void refreshUI() {
        if (rvRecipeSteps != null && recipe!= null) {
            ((RecipeStepsAdapter) rvRecipeSteps.getAdapter()).setSteps(recipe.getSteps());
            if (listState != null) {
                rvRecipeSteps.getLayoutManager().onRestoreInstanceState(listState);
            }
        }
    }

    @Override
    public void onStepSelected(Step step) {
        fragmentListener.onRecipeStepSelected(step);
    }
}
