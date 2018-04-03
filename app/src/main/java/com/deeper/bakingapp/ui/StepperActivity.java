package com.deeper.bakingapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.deeper.bakingapp.R;
import com.deeper.bakingapp.data.network.model.BakingResponse;
import com.deeper.bakingapp.data.network.model.BakingStep;
import com.deeper.bakingapp.ui.adapter.MyStepperAdapter;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

/**
 * Created by Gianni on 03/04/18.
 */

public class StepperActivity extends AppCompatActivity implements StepperLayout.StepperListener{

    public static final String KEY_RECIPE = "recipe";
    public static final String KEY_SELECTED_STEP = "selected_step";

    private StepperLayout mStepperLayout;
    private BakingResponse mRecipe;
    private BakingStep step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details_stepper);
        mStepperLayout = findViewById(R.id.stepperLayout);

        mRecipe = getIntent().getParcelableExtra(KEY_RECIPE);
        step = getIntent().getParcelableExtra(KEY_SELECTED_STEP);

        mStepperLayout.setAdapter(new MyStepperAdapter(getSupportFragmentManager(),
                this, step, mRecipe));
        mStepperLayout.setListener(this);
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

        mStepperLayout.setAdapter(new MyStepperAdapter(getSupportFragmentManager(),
                this, step, mRecipe));
    }

    @Override
    public void onReturn() {
        finish();
    }
}
