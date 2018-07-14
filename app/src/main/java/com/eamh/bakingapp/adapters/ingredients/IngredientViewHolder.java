package com.eamh.bakingapp.adapters.ingredients;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.eamh.bakingapp.R;
import com.eamh.bakingapp.models.Ingredient;
import com.eamh.bakingapp.models.Recipe;

/**
 * Created by enmanuel.miron on 31/03/18.
 */
public class IngredientViewHolder extends RecyclerView.ViewHolder{

    private TextView tvTitle;

    public IngredientViewHolder(View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.content);
    }

    public void setIngredient(Ingredient ingredient) {
        if (itemView != null){
            tvTitle.setText(itemView.getContext().getString(R.string.format_ingredient_details,ingredient.getQuantity(), ingredient.getMeasure(), ingredient.getIngredient()));
        }
    }
}
