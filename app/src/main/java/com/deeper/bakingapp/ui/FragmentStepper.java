package com.deeper.bakingapp.ui;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deeper.bakingapp.BakingApp;
import com.deeper.bakingapp.R;
import com.deeper.bakingapp.data.network.model.BakingStep;
import com.deeper.bakingapp.databinding.FragmentDetailsStepperBinding;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import timber.log.Timber;

/**
 * Created by Gianni on 03/04/18.
 */

public class FragmentStepper extends Fragment implements Step {

    FragmentDetailsStepperBinding mBinding;
    BakingStep step;

    private SimpleExoPlayer mExoPlayer;
    private boolean playWhenReady;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details_stepper,
                container, false);

        initUI();

        return mBinding.getRoot();
    }

    private void initUI() {
        if (getArguments().getParcelable(StepperActivity.SELECTED_STEP_KEY) == null) return;
        step = getArguments().getParcelable(StepperActivity.SELECTED_STEP_KEY);

        initializePlayer();
    }

    @Override
    public VerificationError verifyStep() {
        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        return null;
    }

    @Override
    public void onSelected() {
        //update UI when selected
        Timber.d("selected STEP fragment");
        updateUI();
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        playbackPosition = 0;
        currentWindow = 0;

        mBinding.labelStepDetail.setText(step.getShortDescription());
        mBinding.stepDetailTextview.setText(step.getDescription());

        updatePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M && mExoPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && mExoPlayer != null) {
            releasePlayer();
        }
    }

    private void initializePlayer() {
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        mBinding.playerView.requestFocus();

        mBinding.playerView.setPlayer(mExoPlayer);

        mExoPlayer.setPlayWhenReady(playWhenReady);
        mExoPlayer.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse("url video da STEP");
        MediaSource mediaSource = buildMediaSource(uri);
        mExoPlayer.prepare(mediaSource, true, false);
    }

    private void updatePlayer(){
        if(mExoPlayer == null) initializePlayer();

        boolean hasVideo = !TextUtils.isEmpty(step.getVideoURL());
        boolean hasThumbnail = !TextUtils.isEmpty(step.getThumbnailURL());
        if(hasVideo) {

            Uri uri = Uri.parse("url video da STEP");
            MediaSource mediaSource = buildMediaSource(uri);
            mExoPlayer.prepare(mediaSource, true, false);

            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(currentWindow, playbackPosition);

        } else if(hasThumbnail) {
            mBinding.labelVideoNotAvailable.setVisibility(View.GONE);
            mBinding.stepThumbnail.setVisibility(View.VISIBLE);
            mBinding.playerView.setVisibility(View.GONE);

            Picasso.get().load(step.getThumbnailURL()).into(mBinding.stepThumbnail);
        } else {
            mBinding.labelVideoNotAvailable.setVisibility(View.VISIBLE);
            mBinding.stepThumbnail.setVisibility(View.GONE);
            mBinding.playerView.setVisibility(View.GONE);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(Util.getUserAgent(getActivity(), BakingApp.class.getName()))).
                createMediaSource(uri);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            playbackPosition = mExoPlayer.getCurrentPosition();
            currentWindow = mExoPlayer.getCurrentWindowIndex();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}