package com.eamh.bakingapp.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eamh.bakingapp.MainActivity;
import com.eamh.bakingapp.R;
import com.eamh.bakingapp.RecipeStepActivity;
import com.eamh.bakingapp.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link MainActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepActivity}
 * on handsets.
 * SimpleExoPlayer info gathered from: https://codelabs.developers.google.com/codelabs/exoplayer-intro
 */
@EFragment(R.layout.fragment_recipe_step)
public class RecipeStepFragment extends Fragment {

    private static final String TAG = RecipeStepFragment.class.getName();
    public static final String INTENT_KEY_SELECTED_STEP ="IKSS";

    private SimpleExoPlayer player;

    @ViewById(R.id.tvStepDescription)
    TextView tvStepDetail;

    @ViewById(R.id.tvStepShortDescription)
    TextView tvStepShortDescription;

    @ViewById(R.id.ivStepImage)
    ImageView ivStepImage;

    @ViewById(R.id.playerViewStep)
    PlayerView playerView;

    @InstanceState
    Step step;

    @InstanceState
    boolean playWhenReady;
    @InstanceState
    int currentWindow;
    @InstanceState
    long playbackPosition;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @AfterViews
    void afterViews(){
        if (step == null && (getArguments() != null && getArguments().containsKey(INTENT_KEY_SELECTED_STEP))) {
            step = getArguments().getParcelable(INTENT_KEY_SELECTED_STEP);
        }

        tvStepShortDescription.setText(step.getShortDescription());

        if (!step.getShortDescription().equals(step.getDescription())){
            tvStepDetail.setText(step.getDescription());
        }

        if (!TextUtils.isEmpty(step.getVideoURL())){
            preparePlayer(step.getVideoURL());
        }

        if (!TextUtils.isEmpty(step.getThumbnailURL())){
            initializeImage(step.getThumbnailURL());
        }
    }

    private void initializeImage(String thumbnailURL) {
        ivStepImage.setVisibility(View.VISIBLE);
        Picasso.get().load(thumbnailURL).error(R.drawable.navheader).into(ivStepImage, new Callback() {
            @Override
            public void onSuccess() {
                ivStepImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception e) {
                ivStepImage.setVisibility(View.GONE);
            }
        });
    }

    private void initializePlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            playerView.setPlayer(player);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
        }

        if (step != null && !TextUtils.isEmpty(step.getVideoURL())){
            preparePlayer(step.getVideoURL());
        }
    }

    private void preparePlayer(String videoUrl) {
        if (player != null) {
            playerView.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(videoUrl);
            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, false, false);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-baking-app")).
                createMediaSource(uri);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }
}