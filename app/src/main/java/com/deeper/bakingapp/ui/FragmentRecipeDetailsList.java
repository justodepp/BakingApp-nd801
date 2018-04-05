package com.deeper.bakingapp.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deeper.bakingapp.R;
import com.deeper.bakingapp.data.network.model.BakingResponse;
import com.deeper.bakingapp.data.network.model.BakingStep;
import com.deeper.bakingapp.databinding.FragmentRecipeStepperBinding;
import com.deeper.bakingapp.ui.adapter.RecipeDetailsListAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRecipeDetailsList} factory method to
 * create an instance of this fragment.
 */
public class FragmentRecipeDetailsList extends Fragment implements RecipeDetailsListAdapter.OnStepClickListener {

    FragmentRecipeStepperBinding mBinding;

    private BakingResponse mRecipe;

    private OnStepClicked mStepClicked;

    public interface OnStepClicked {
        void onClickedStep(int position, BakingStep step);
    }

    public void setStepClickedListener(OnStepClicked listener) {
        this.mStepClicked = listener;
    }

    public FragmentRecipeDetailsList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_stepper,
                container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mStepClicked = (OnStepClicked) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFavouriteClickListener");
        }

        /*try {
            mStepClickCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }*/
    }

    @Override
    public void onClickStepItem(int position, BakingStep step) {
        mStepClicked.onClickedStep(position, step);
    }

    public void updateRecipe(BakingResponse recipe) {
        mRecipe = recipe;
        initUI();
    }
}
