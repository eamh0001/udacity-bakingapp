package com.eamh.bakingapp.adapters.steps;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eamh.bakingapp.R;
import com.eamh.bakingapp.models.Step;

import java.util.List;

/**
 * Created by enmanuel.miron on 31/03/18.
 */
public class RecipeStepsAdapter
        extends RecyclerView.Adapter<RecipeStepViewHolder> implements RecipeStepViewHolder.Listener{

    public interface RowClickListener{
        void onStepSelected(Step step);
    }

    private RowClickListener listener;

    private List<Step> steps;
    private int lastStepSelectedPosition;
    public RecipeStepsAdapter(RowClickListener listener) {
        this.listener = listener;
        this.lastStepSelectedPosition =  RecyclerView.NO_POSITION;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    @Override
    public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_content, parent, false);
        RecipeStepViewHolder viewHolder = new RecipeStepViewHolder(view);
        viewHolder.setListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecipeStepViewHolder holder, int position) {
        holder.setStep(steps.get(position));
        holder.itemView.setSelected(lastStepSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return steps != null ? steps.size() : 0;
    }

    @Override
    public void onStepSelected(Step step, RecyclerView.ViewHolder viewHolder) {
        listener.onStepSelected(step);
    }
}