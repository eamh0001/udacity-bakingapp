package com.eamh.bakingapp.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.eamh.bakingapp.R;
import com.eamh.bakingapp.adapters.steps.RecipeStepsAdapter;
import com.eamh.bakingapp.models.Recipe;
import com.eamh.bakingapp.models.Step;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_recipe_info)
public class RecipeInfoFragment extends Fragment implements RecipeStepsAdapter.RowClickListener {

    public interface RecipeInfoFragmentListener {
        void onRecipeInfoSelected(Recipe recipe);
        void onRecipeStepSelected(Step step);
    }

    @ViewById(R.id.tvInfo)
    TextView tvInfo;

    @ViewById(R.id.rvRecipeSteps)
    RecyclerView rvRecipeSteps;

    @InstanceState
    Recipe recipe;

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

    @AfterViews
    void afterViews(){
        rvRecipeSteps.setAdapter(new RecipeStepsAdapter(this));
    }

    public void setData(Recipe recipe) {
        this.recipe = recipe;
        refreshUI();
    }

    private void refreshUI() {
        tvInfo.setText(recipe.getName());
        ((RecipeStepsAdapter) rvRecipeSteps.getAdapter()).setSteps(recipe.getSteps());
    }

    @Override
    public void onStepSelected(Step step) {
        fragmentListener.onRecipeStepSelected(step);
    }
}
