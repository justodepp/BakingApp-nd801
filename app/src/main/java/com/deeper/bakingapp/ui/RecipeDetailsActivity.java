package com.deeper.bakingapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.deeper.bakingapp.R;
import com.deeper.bakingapp.data.network.model.BakingResponse;
import com.deeper.bakingapp.data.network.model.BakingStep;
import com.deeper.bakingapp.databinding.ActivityRecipeStepperBinding;

/**
 * Created by Gianni on 03/04/18.
 */

public class RecipeDetailsActivity extends AppCompatActivity implements FragmentRecipeDetailsList.OnStepClicked {

    ActivityRecipeStepperBinding mBinding;
    FragmentRecipeDetailsList fragmentRecipeDetailsList;
    FragmentStepperLayout fragmentStepperLayout;

    private BakingResponse mRecipe;
    private BakingStep mStep;

    private int stepItemPosition = -1;

    private boolean mLandscape;
    private boolean mIsTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_stepper);

        mIsTablet = getResources().getBoolean(R.bool.isTablet);
        mLandscape = getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;

        parseData(savedInstanceState);
        initUI();
        initToolbar();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(StepperActivity.RECIPE_KEY, mRecipe);
        outState.putParcelable(StepperActivity.SELECTED_STEP_KEY, mStep);
        outState.putInt(StepperActivity.CURRENT_STEP_POSITION_KEY, stepItemPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(StepperActivity.RECIPE_KEY))
            mRecipe = savedInstanceState.getParcelable(StepperActivity.RECIPE_KEY);
        if (savedInstanceState.containsKey(StepperActivity.SELECTED_STEP_KEY))
            mStep = savedInstanceState.getParcelable(StepperActivity.SELECTED_STEP_KEY);
        if (savedInstanceState.containsKey(StepperActivity.CURRENT_STEP_POSITION_KEY))
            stepItemPosition = savedInstanceState.getInt(StepperActivity.CURRENT_STEP_POSITION_KEY);
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

    private void parseData(Bundle savedInstanceState) {
        Intent startIntent = getIntent();
        if (startIntent == null) {
            finish();
            return;
        }

        mRecipe = startIntent.getParcelableExtra(StepperActivity.RECIPE_KEY);

        mStep = startIntent.getParcelableExtra(StepperActivity.SELECTED_STEP_KEY);
        stepItemPosition = startIntent.getIntExtra(StepperActivity.CURRENT_STEP_POSITION_KEY, 0);

        if(savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable(StepperActivity.SELECTED_STEP_KEY);
            stepItemPosition = savedInstanceState.getInt(StepperActivity.CURRENT_STEP_POSITION_KEY);
        }
    }

    private void initUI() {
        fragmentRecipeDetailsList = (FragmentRecipeDetailsList) getSupportFragmentManager()
                .findFragmentById(R.id.recipe_detail_fragment);
        fragmentRecipeDetailsList.setRecipe(mRecipe);

        fragmentRecipeDetailsList.setStepClickedListener(this);

        if (mIsTablet && mLandscape) {
            fragmentStepperLayout = (FragmentStepperLayout) getSupportFragmentManager()
                    .findFragmentById(R.id.step_fragment);

            fragmentRecipeDetailsList.mBinding.rvMain.scrollToPosition(stepItemPosition);
            fragmentStepperLayout.updateStep(mStep, stepItemPosition);
            setDetailsPanel();
        }
    }

    private void setDetailsPanel() {
        if (mStep == null) {
            mBinding.labelStepNotSelected.setVisibility(View.VISIBLE);
            findViewById(R.id.step_detail_fragment).setVisibility(View.GONE);
        } else {
            mBinding.labelStepNotSelected.setVisibility(View.GONE);
            findViewById(R.id.step_detail_fragment).setVisibility(View.VISIBLE);
        }
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
        mStep = step;
        stepItemPosition = position;
        if(mIsTablet && mLandscape) {
            fragmentStepperLayout.updateStep(step, position);
            setDetailsPanel();
        } else {
            Intent intent = new Intent(this, StepperActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(StepperActivity.RECIPE_KEY, mRecipe);
            bundle.putParcelable(StepperActivity.SELECTED_STEP_KEY, step);
            bundle.putInt(StepperActivity.CURRENT_STEP_POSITION_KEY, position);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
