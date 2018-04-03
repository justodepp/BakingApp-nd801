package com.deeper.bakingapp.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        void onClickRecipeItem(BakingResponse recipe);
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
            int resource = R.drawable.img_cupcake;
            if(recipe.get(position).getName().toLowerCase().contains("nutella"))
                resource = R.drawable.img_nutella_pie;
            else if(recipe.get(position).getName().toLowerCase().contains("yellow"))
                resource = R.drawable.img_yellow_cake;
            else if(recipe.get(position).getName().toLowerCase().contains("brownies"))
                resource = R.drawable.img_brownies;
            else if(recipe.get(position).getName().toLowerCase().contains("cheese"))
                resource = R.drawable.img_cheesecake;
            if (!TextUtils.isEmpty(recipe.get(position).getImage())) {
                Picasso.get()
                        .load(recipe.get(position).getImage())
                        .error(resource)
                        .placeholder(resource)
                        .into(mBinding.recipeImageview);
            } else {
                Picasso.get()
                        .load(resource)
                        .into(mBinding.recipeImageview);
            }

            mBinding.recipeNameTextview.setText(recipe.get(position).getName());

            ViewCompat.setTransitionName(mBinding.recipeImageview, String.valueOf(recipe.get(position).getId()));
        }

        @Override
        public void onClick(View view) {
            int clickPosition = getAdapterPosition();
            mRecipeClickListener.onClickRecipeItem(recipe.get(clickPosition));
        }
    }
}
