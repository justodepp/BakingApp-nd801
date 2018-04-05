package com.deeper.bakingapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deeper.bakingapp.R;
import com.deeper.bakingapp.data.network.model.BakingResponse;
import com.deeper.bakingapp.databinding.FragmentStepperLayoutBinding;
import com.deeper.bakingapp.ui.adapter.MyStepperAdapter;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

/**
 * Created by Gianni on 05/04/18.
 */

public class FragmentStepperLayout extends Fragment implements StepperLayout.StepperListener {

    public static final String CURRENT_STEP_POSITION_KEY = "position";

    FragmentStepperLayoutBinding mBinding;

    private BakingResponse mRecipe;
    private int startingStepPosition;

    public FragmentStepperLayout() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stepper_layout,
                container, false);

        initUI(savedInstanceState);

        return mBinding.getRoot();
    }

    private void initUI(Bundle savedInstanceState) {
        startingStepPosition = savedInstanceState != null ? savedInstanceState.getInt(CURRENT_STEP_POSITION_KEY)
                : getActivity().getIntent().getIntExtra(CURRENT_STEP_POSITION_KEY, 0);

        mRecipe = getActivity().getIntent().getParcelableExtra(StepperActivity.RECIPE_KEY);

        mBinding.stepperLayout.setAdapter(new MyStepperAdapter(getFragmentManager(),
                getContext(), mRecipe), startingStepPosition);
        mBinding.stepperLayout.setListener(this);

    }

    public void updateStep(int stepPosition) {
        mBinding.stepperLayout.setAdapter(new MyStepperAdapter(getFragmentManager(),
                getContext(), mRecipe), stepPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_STEP_POSITION_KEY, mBinding.stepperLayout.getCurrentStepPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCompleted(View completeButton) {
        getActivity().finish();
    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {
        getActivity().finish();
    }
}
