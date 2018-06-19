package com.eamh.bakingapp.fragments;

import android.app.Activity;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
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

    public static final String INTENT_KEY_SELECTED_STEP ="IKSS";

    private SimpleExoPlayer player;

    @ViewById(R.id.recipe_detail)
    TextView tvInfo;

    @ViewById(R.id.playerView)
    PlayerView playerView;

    @InstanceState
    Step step;

    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepFragment() {
    }

    @AfterViews
    void afterViews(){
        if (step == null && (getArguments() != null && getArguments().containsKey(INTENT_KEY_SELECTED_STEP))) {
            step = getArguments().getParcelable(INTENT_KEY_SELECTED_STEP);
        }

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(step.getShortDescription());
        }

        tvInfo.setText(step.toString());

        initializePlayer();
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(step.getVideoURL());
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-baking-app")).
                createMediaSource(uri);
    }
}