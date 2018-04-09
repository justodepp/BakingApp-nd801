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
import com.deeper.bakingapp.data.model.Step;
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
import com.stepstone.stepper.VerificationError;

/**
 * Created by Gianni on 03/04/18.
 */

public class FragmentStepperStep extends Fragment implements com.stepstone.stepper.Step {

    private static final String CURRENT_VIDEO_POSITION = "video_position";

    FragmentDetailsStepperBinding mBinding;
    Step step;

    private SimpleExoPlayer mExoPlayer;
    private boolean playWhenReady;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details_stepper,
                container, false);
        playbackPosition = savedInstanceState != null ? savedInstanceState.getLong(CURRENT_VIDEO_POSITION) : 0;
        initUI();

        return mBinding.getRoot();
    }

    private void initUI() {
        if(getArguments() != null) {
            if (getArguments().getParcelable(StepperActivity.SELECTED_STEP_KEY) != null) {
                step = getArguments().getParcelable(StepperActivity.SELECTED_STEP_KEY);
            }
        }

        initializePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if(mExoPlayer != null)
            outState.putLong(CURRENT_VIDEO_POSITION, mExoPlayer.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public VerificationError verifyStep() {
        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        return null;
    }

    @Override
    public void onSelected() {
        //update UI when selected
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

    public void updateStep(Step step) {
        this.step = step;

        if (mExoPlayer != null)
            mExoPlayer.stop();
        updateUI();
    }

    private void initializePlayer() {
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        mBinding.playerView.requestFocus();

        mBinding.playerView.setPlayer(mExoPlayer);

        mExoPlayer.setPlayWhenReady(playWhenReady);
        mExoPlayer.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse("");
        MediaSource mediaSource = buildMediaSource(uri);
        mExoPlayer.prepare(mediaSource, true, false);
    }

    private void updatePlayer(){
        if(mExoPlayer == null) initializePlayer();

        boolean hasVideo = !TextUtils.isEmpty(step.getVideoURL());
        boolean hasThumbnail = !TextUtils.isEmpty(step.getThumbnailURL());
        if(hasVideo) {
            Uri uri = Uri.parse(step.getVideoURL());
            MediaSource mediaSource = buildMediaSource(uri);
            mExoPlayer.prepare(mediaSource, true, false);

            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(currentWindow, playbackPosition);

            showVideoView();
        } else if(hasThumbnail) {
            if(step.getThumbnailURL().endsWith(".mp4")){
                Uri uri = Uri.parse(step.getThumbnailURL());
                MediaSource mediaSource = buildMediaSource(uri);
                mExoPlayer.prepare(mediaSource, true, false);

                mExoPlayer.setPlayWhenReady(playWhenReady);
                mExoPlayer.seekTo(currentWindow, playbackPosition);
            } else {
                mBinding.labelVideoNotAvailable.setVisibility(View.GONE);
                mBinding.stepThumbnail.setVisibility(View.VISIBLE);
                mBinding.playerView.setVisibility(View.GONE);

                Picasso.get().load(step.getThumbnailURL()).into(mBinding.stepThumbnail);
            }
        } else {
            hideVideoView();
        }
    }

    private void showVideoView() {
        mBinding.labelVideoNotAvailable.setVisibility(View.GONE);
        mBinding.stepThumbnail.setVisibility(View.GONE);
        mBinding.playerView.setVisibility(View.VISIBLE);
    }

    private void hideVideoView() {
        mBinding.labelVideoNotAvailable.setVisibility(View.VISIBLE);
        mBinding.stepThumbnail.setVisibility(View.GONE);
        mBinding.playerView.setVisibility(View.GONE);
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