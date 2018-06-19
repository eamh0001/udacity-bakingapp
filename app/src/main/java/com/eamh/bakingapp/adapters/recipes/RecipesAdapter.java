package com.eamh.bakingapp.adapters.recipes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eamh.bakingapp.R;
import com.eamh.bakingapp.models.Recipe;

import java.util.List;

/**
 * Created by enmanuel.miron on 31/03/18.
 */
public class RecipesAdapter
        extends RecyclerView.Adapter<RecipeViewHolder> implements RecipeViewHolder.Listener{

    public interface RowClickListener{
        void onRecipeSelected(Recipe recipe);
    }

    private RowClickListener listener;
    private List<Recipe> recipes;
    private int lastRecipeSelectedPosition;

    public RecipesAdapter(RowClickListener listener) {
        this.listener = listener;
        this.lastRecipeSelectedPosition =  RecyclerView.NO_POSITION;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipes_list_content, parent, false);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);
        viewHolder.setListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder holder, int position) {
        holder.setRecipe(recipes.get(position));
        holder.itemView.setSelected(lastRecipeSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return recipes != null ? recipes.size() : 0;
    }

    @Override
    public void onRecipeSelected(Recipe recipe, RecyclerView.ViewHolder viewHolder) {
//        notifyItemChanged(lastRecipeSelectedPosition);
//        lastRecipeSelectedPosition = viewHolder.getLayoutPosition();
//        notifyItemChanged(lastRecipeSelectedPosition);
        listener.onRecipeSelected(recipe);
    }
}