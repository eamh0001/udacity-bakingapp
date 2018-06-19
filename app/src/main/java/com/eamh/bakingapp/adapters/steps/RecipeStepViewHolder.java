package com.eamh.bakingapp.adapters.steps;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.eamh.bakingapp.R;
import com.eamh.bakingapp.models.Recipe;
import com.eamh.bakingapp.models.Step;

/**
 * Created by enmanuel.miron on 31/03/18.
 */
public class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public interface Listener{
        void onStepSelected(Step step, RecyclerView.ViewHolder viewHolder);
    }

    private TextView tvTitle;
    private Listener listener;
    private Step step;

    public RecipeStepViewHolder(View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.content);
        itemView.setOnClickListener(this);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setStep(Step step) {
        this.step = step;
        tvTitle.setText(step.getShortDescription());
    }

    @Override
    public void onClick(View v) {
        listener.onStepSelected(step, this);
    }
}
