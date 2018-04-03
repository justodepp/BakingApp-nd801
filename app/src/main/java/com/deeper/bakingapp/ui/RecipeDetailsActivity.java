package com.deeper.bakingapp.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.deeper.bakingapp.R;
import com.deeper.bakingapp.data.network.model.BakingResponse;
import com.deeper.bakingapp.databinding.ActivityRecipeStepperBinding;

/**
 * Created by Gianni on 03/04/18.
 */

public class RecipeDetailsActivity extends AppCompatActivity {

    ActivityRecipeStepperBinding mBinding;
    FragmentRecipeDetailsList fragmentRecipeDetailsList;

    private static final String KEY_RECIPE = "recipe";

    private BakingResponse mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_stepper);

        initUI();
        initToolbar();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_RECIPE, mRecipe);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(KEY_RECIPE))
            mRecipe = savedInstanceState.getParcelable(KEY_RECIPE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void parseData() {
        Intent startIntent = getIntent();
        if (startIntent == null) {
            finish();
            return;
        }

        mRecipe = startIntent.getParcelableExtra(KEY_RECIPE);
        fragmentRecipeDetailsList.setRecipe(mRecipe);

    }

    private void initUI() {
        fragmentRecipeDetailsList = (FragmentRecipeDetailsList) getSupportFragmentManager()
                .findFragmentById(R.id.recipe_detail_fragment);

        parseData();
    }

    private void initToolbar() {
        setSupportActionBar(mBinding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mRecipe.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
