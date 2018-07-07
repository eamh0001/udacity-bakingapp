package com.eamh.bakingapp.adapters.ingredients;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eamh.bakingapp.R;
import com.eamh.bakingapp.models.Ingredient;

import java.util.List;

/**
 * Created by enmanuel.miron on 31/03/18.
 */
public class IngredientsAdapter
        extends RecyclerView.Adapter<IngredientViewHolder>{


    private List<Ingredient> ingredients;
    private int lastRecipeSelectedPosition;

    public IngredientsAdapter() {
        this.lastRecipeSelectedPosition =  RecyclerView.NO_POSITION;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredients_list_content, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final IngredientViewHolder holder, int position) {
        holder.setIngredient(ingredients.get(position));
        holder.itemView.setSelected(lastRecipeSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return ingredients != null ? ingredients.size() : 0;
    }
}