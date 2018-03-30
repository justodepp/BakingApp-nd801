package com.deeper.bakingapp.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.deeper.bakingapp.R;
import com.deeper.bakingapp.data.network.model.BakingResponse;
import com.deeper.bakingapp.databinding.BakingRecipeItemListBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Gianni on 30/03/18.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeHolder>{

    private Context context;
    private ArrayList<BakingResponse> recipe;

    private final RecipeClickListener mRecipeClickListener;

    public interface RecipeClickListener {
        void onClickMovie(int position, BakingResponse recipe, ImageView clickedImage);
    }

    public RecipeListAdapter(Context context, ArrayList<BakingResponse> recipe, RecipeClickListener recipeClickListener){
        this.context = context;
        this.recipe = recipe;
        mRecipeClickListener = recipeClickListener;
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        BakingRecipeItemListBinding mBinding = DataBindingUtil.inflate(inflater,
                R.layout.baking_recipe_item_list, parent, false);

        return new RecipeHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return recipe != null ? recipe.size() :  0;
    }

    class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final BakingRecipeItemListBinding mBinding;

        RecipeHolder(BakingRecipeItemListBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;

            mBinding.getRoot().setOnClickListener(this);
        }

        public void bind(int position){
            Picasso.get()
                    .load(recipe.get(position).getImage())
                    .into(mBinding.recipeImageview);

            mBinding.recipeNameTextview.setText(recipe.get(position).getName());

            ViewCompat.setTransitionName(mBinding.recipeImageview, String.valueOf(recipe.get(position).getId()));
        }

        @Override
        public void onClick(View view) {
            int clickPosition = getAdapterPosition();
            mRecipeClickListener.onClickMovie(clickPosition, recipe.get(clickPosition), mBinding.recipeImageview);
        }
    }
}
