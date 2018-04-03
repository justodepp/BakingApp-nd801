package com.deeper.bakingapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.deeper.bakingapp.R;
import com.deeper.bakingapp.data.network.model.BakingResponse;
import com.deeper.bakingapp.data.network.model.BakingStep;
import com.deeper.bakingapp.databinding.ActivityDetailsStepperBinding;
import com.deeper.bakingapp.ui.adapter.MyStepperAdapter;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

/**
 * Created by Gianni on 03/04/18.
 */

public class StepperActivity extends AppCompatActivity implements StepperLayout.StepperListener{

    public static final String RECIPE_KEY = "recipe";
    public static final String SELECTED_STEP_KEY = "step_selected";
    public static final String CURRENT_STEP_POSITION_KEY = "position";

    ActivityDetailsStepperBinding mBinding;

    private BakingResponse mRecipe;
    private BakingStep step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details_stepper);

        mRecipe = getIntent().getParcelableExtra(RECIPE_KEY);
        step = getIntent().getParcelableExtra(SELECTED_STEP_KEY);

        int startingStepPosition = savedInstanceState != null ? savedInstanceState.getInt(CURRENT_STEP_POSITION_KEY)
                : getIntent().getIntExtra(CURRENT_STEP_POSITION_KEY, 0);

        initToolbar();

        mBinding.stepperLayout.setAdapter(new MyStepperAdapter(getSupportFragmentManager(),
                this, step, mRecipe), startingStepPosition);
        mBinding.stepperLayout.setListener(this);
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
        outState.putInt(CURRENT_STEP_POSITION_KEY, mBinding.stepperLayout.getCurrentStepPosition());
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

    @Override
    public void onCompleted(View completeButton) {
        finish();
    }

    @Override
    public void onError(VerificationError verificationError) {
        Toast.makeText(this, "onError! -> " + verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {
        Toast.makeText(this, "onStepSelected! -> " + newStepPosition, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReturn() {
        finish();
    }
}
