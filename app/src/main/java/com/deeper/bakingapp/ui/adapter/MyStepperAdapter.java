package com.deeper.bakingapp.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.deeper.bakingapp.data.network.model.BakingResponse;
import com.deeper.bakingapp.ui.FragmentStepperStep;
import com.deeper.bakingapp.ui.StepperActivity;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * Created by Gianni on 03/04/18.
 */

public class MyStepperAdapter extends AbstractFragmentStepAdapter {

    public static final String KEY_STEP_POSITION = "step_position";
    private BakingResponse recipe;

    public MyStepperAdapter(FragmentManager fm, Context context, BakingResponse recipe) {
        super(fm, context);
        this.recipe = recipe;
    }

    @Override
    public Step createStep(int position) {
        final FragmentStepperStep step = new FragmentStepperStep();
        Bundle b = new Bundle();
        b.putInt(KEY_STEP_POSITION, position);
        b.putParcelable(StepperActivity.SELECTED_STEP_KEY, recipe.getSteps().get(position));
        step.setArguments(b);
        return step;
    }

    @Override
    public int getCount() {
        return recipe.getSteps().size();
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        return new StepViewModel.Builder(context)
                .setTitle("") //can be a CharSequence instead
                .create();
    }
}