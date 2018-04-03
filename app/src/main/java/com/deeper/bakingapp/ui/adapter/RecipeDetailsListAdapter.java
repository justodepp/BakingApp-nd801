package com.deeper.bakingapp.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deeper.bakingapp.R;
import com.deeper.bakingapp.data.network.model.BakingStep;
import com.deeper.bakingapp.databinding.SingleRecipeItemListBinding;

import java.util.ArrayList;

/**
 * Created by Gianni on 30/03/18.
 */

public class RecipeDetailsListAdapter extends RecyclerView.Adapter<RecipeDetailsListAdapter.RecipeHolder>{

    private Context context;
    private ArrayList<BakingStep> steps;

    private final RecipeClickListener mRecipeClickListener;

    public interface RecipeClickListener {
        void onClickStepItem(int position);
    }

    public RecipeDetailsListAdapter(Context context, ArrayList<BakingStep> steps, RecipeClickListener recipeClickListener){
        this.context = context;
        this.steps = steps;
        mRecipeClickListener = recipeClickListener;
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SingleRecipeItemListBinding mBinding = DataBindingUtil.inflate(inflater,
                R.layout.single_recipe_item_list, parent, false);

        return new RecipeHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return steps != null ? steps.size() :  0;
    }

    class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final SingleRecipeItemListBinding mBinding;

        RecipeHolder(SingleRecipeItemListBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;

            mBinding.getRoot().setOnClickListener(this);
        }

        public void bind(int position){
            mBinding.txtStepNumber.setText(String.valueOf(steps.get(position).getId()));
            mBinding.txtStepName.setText(steps.get(position).getShortDescription());
        }

        @Override
        public void onClick(View view) {
            int clickPosition = getAdapterPosition();
            mRecipeClickListener.onClickStepItem(clickPosition);
        }
    }
}
