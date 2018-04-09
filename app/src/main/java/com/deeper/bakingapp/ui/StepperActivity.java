package com.deeper.bakingapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.deeper.bakingapp.R;
import com.deeper.bakingapp.data.model.Recipe;
import com.deeper.bakingapp.databinding.ActivityStepperBinding;

/**
 * Created by Gianni on 03/04/18.
 */

public class StepperActivity extends AppCompatActivity {

    public static final String RECIPE_KEY = "recipe";
    public static final String SELECTED_STEP_KEY = "step_selected";
    public static final String CURRENT_STEP_POSITION_KEY = "position";

    ActivityStepperBinding mBinding;
    FragmentStepperLayout fragmentStepper;

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_stepper);

        mRecipe = getIntent().getParcelableExtra(RECIPE_KEY);

        initToolbar();

        initFragment();
    }

    private void initFragment() {
        fragmentStepper = (FragmentStepperLayout) getSupportFragmentManager()
                .findFragmentById(R.id.step_fragment);
    }

    private void initToolbar() {
        setSupportActionBar(mBinding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mRecipe.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected  void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
}
