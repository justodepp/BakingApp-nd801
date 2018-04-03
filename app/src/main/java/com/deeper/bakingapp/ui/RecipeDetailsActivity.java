package com.deeper.bakingapp.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.deeper.bakingapp.R;
import com.deeper.bakingapp.data.network.model.BakingResponse;
import com.deeper.bakingapp.data.network.model.BakingStep;
import com.deeper.bakingapp.databinding.ActivityRecipeStepperBinding;

/**
 * Created by Gianni on 03/04/18.
 */

public class RecipeDetailsActivity extends AppCompatActivity implements FragmentRecipeDetailsList.OnStepClicked{

    ActivityRecipeStepperBinding mBinding;
    FragmentRecipeDetailsList fragmentRecipeDetailsList;

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

        outState.putParcelable(StepperActivity.RECIPE_KEY, mRecipe);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(StepperActivity.RECIPE_KEY))
            mRecipe = savedInstanceState.getParcelable(StepperActivity.RECIPE_KEY);
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

        mRecipe = startIntent.getParcelableExtra(StepperActivity.RECIPE_KEY);
        fragmentRecipeDetailsList.setRecipe(mRecipe);
    }

    private void initUI() {
        fragmentRecipeDetailsList = (FragmentRecipeDetailsList) getSupportFragmentManager()
                .findFragmentById(R.id.recipe_detail_fragment);

        parseData();

        fragmentRecipeDetailsList.setStepClickedListener(this);
    }

    private void initToolbar() {
        setSupportActionBar(mBinding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mRecipe.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClickedStep(int position, BakingStep step) {
        Intent intent = new Intent(this, StepperActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable(StepperActivity.RECIPE_KEY, mRecipe);
        bundle.putParcelable(StepperActivity.SELECTED_STEP_KEY, step);
        bundle.putInt(StepperActivity.CURRENT_STEP_POSITION_KEY, position);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
