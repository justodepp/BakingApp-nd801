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

    public static final String KEY_RECIPE = "recipe";
    public static final String KEY_SELECTED_STEP = "selected_step";

    ActivityDetailsStepperBinding mBinding;

    private BakingResponse mRecipe;
    private BakingStep step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details_stepper);

        mRecipe = getIntent().getParcelableExtra(KEY_RECIPE);
        step = getIntent().getParcelableExtra(KEY_SELECTED_STEP);

        initToolbar();

        mBinding.stepperLayout.setAdapter(new MyStepperAdapter(getSupportFragmentManager(),
                this, step, mRecipe));
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
