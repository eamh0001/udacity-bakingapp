package com.eamh.bakingapp.fragments;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.eamh.bakingapp.R;
import com.eamh.bakingapp.models.Recipe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_recipe_details)
public class RecipeDetailsFragment extends Fragment {

    public static final String INTENT_KEY_SELECTED_RECIPE = "IKSS";

    @ViewById(R.id.tvInfo)
    TextView tvInfo;

    @InstanceState
    Recipe recipe;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @AfterViews
    void afterViews(){
        if (recipe == null && (getArguments() != null && getArguments().containsKey(INTENT_KEY_SELECTED_RECIPE))) {
            recipe = getArguments().getParcelable(INTENT_KEY_SELECTED_RECIPE);
        }

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(recipe.getName());
        }

        tvInfo.setText(recipe.toString());
    }
}
