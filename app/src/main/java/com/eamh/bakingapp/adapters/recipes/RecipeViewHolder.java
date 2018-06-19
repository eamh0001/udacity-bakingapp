package com.eamh.bakingapp.adapters.recipes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.eamh.bakingapp.R;
import com.eamh.bakingapp.models.Recipe;

/**
 * Created by enmanuel.miron on 31/03/18.
 */
public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public interface Listener{
        void onRecipeSelected(Recipe recipe, RecyclerView.ViewHolder  viewHolder);
    }

    private TextView tvTitle;
    private Listener listener;
    private Recipe recipe;

    public RecipeViewHolder(View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.content);
        itemView.setOnClickListener(this);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        tvTitle.setText(recipe.getName());
    }

    @Override
    public void onClick(View v) {
        listener.onRecipeSelected(recipe, this);
    }
}
