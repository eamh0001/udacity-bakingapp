package com.eamh.bakingapp.fragments;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.eamh.bakingapp.R;
import com.eamh.bakingapp.adapters.ingredients.IngredientsAdapter;
import com.eamh.bakingapp.models.Recipe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_recipe_details)
public class RecipeDetailsFragment extends Fragment{

    public static final String INTENT_KEY_SELECTED_RECIPE = "IKSS";

    @ViewById(R.id.ivStep)
    ImageView ivStepImage;

    @ViewById(R.id.rvIngredients)
    RecyclerView rvIngredients;

    @InstanceState
    Recipe recipe;

    public RecipeDetailsFragment() { }

    @AfterViews
    void afterViews(){
        if (recipe == null && (getArguments() != null && getArguments().containsKey(INTENT_KEY_SELECTED_RECIPE))) {
            recipe = getArguments().getParcelable(INTENT_KEY_SELECTED_RECIPE);
        }

        if (recipe != null){
            refreshUI(recipe);
        }
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void refreshUI(Recipe recipe){
        if (!TextUtils.isEmpty(recipe.getImage())){
            Picasso.get().load(recipe.getImage()).error(R.drawable.navheader).into(ivStepImage, new Callback() {
                @Override
                public void onSuccess() {
                    ivStepImage.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError(Exception e) {
                    ivStepImage.setVisibility(View.GONE);
                }
            });
        }else ivStepImage.setVisibility(View.GONE);

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(getString(R.string.format_ingredients_title, recipe.getName()));
        }

        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter();
        ingredientsAdapter.setIngredients(recipe.getIngredients());
        rvIngredients.setAdapter(ingredientsAdapter);
    }
}
