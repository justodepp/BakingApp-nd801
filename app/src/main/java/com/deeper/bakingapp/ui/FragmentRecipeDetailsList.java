package com.deeper.bakingapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deeper.bakingapp.R;
import com.deeper.bakingapp.data.network.model.BakingResponse;
import com.deeper.bakingapp.data.network.model.BakingStep;
import com.deeper.bakingapp.databinding.SingleRecipeStepperBinding;
import com.deeper.bakingapp.ui.adapter.RecipeDetailsListAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRecipeDetailsList} factory method to
 * create an instance of this fragment.
 */
public class FragmentRecipeDetailsList extends Fragment implements RecipeDetailsListAdapter.RecipeClickListener {

    SingleRecipeStepperBinding mBinding;

    private BakingResponse mRecipe;

    public FragmentRecipeDetailsList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.single_recipe_stepper,
                container, false);

        return mBinding.getRoot();
    }

    public void setRecipe(BakingResponse recipe) {
        mRecipe = recipe;
        initUI();
    }

    private void initUI() {
        mBinding.nameTextview.setText(mRecipe.getName());
        mBinding.servingsTextview.setText(String.valueOf(mRecipe.getServings().toString()));
        mBinding.ingredientsTextview.setText(mRecipe.printIngredients());

        initImage();

        mBinding.rvMain.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        );
        mBinding.rvMain.setHasFixedSize(true);

        RecipeDetailsListAdapter listAdapter = new RecipeDetailsListAdapter(getActivity(), (ArrayList<BakingStep>) mRecipe.getSteps(), this);
        mBinding.rvMain.setAdapter(listAdapter);
    }

    private void initImage() {
        int resource = R.drawable.img_cupcake;
        if(mRecipe.getName().toLowerCase().contains("nutella"))
            resource = R.drawable.img_nutella_pie;
        else if(mRecipe.getName().toLowerCase().contains("yellow"))
            resource = R.drawable.img_yellow_cake;
        else if(mRecipe.getName().toLowerCase().contains("brownies"))
            resource = R.drawable.img_brownies;
        else if(mRecipe.getName().toLowerCase().contains("cheese"))
            resource = R.drawable.img_cheesecake;
        if (!TextUtils.isEmpty(mRecipe.getImage())) {
            Picasso.get()
                    .load(mRecipe.getImage())
                    .error(resource)
                    .placeholder(resource)
                    .into(mBinding.recipeImageview);
        } else {
            Picasso.get()
                    .load(resource)
                    .into(mBinding.recipeImageview);
        }
    }

    @Override
    public void onClickStepItem(int position) {
        Timber.d("Single step clicked" + position);
    }
}
