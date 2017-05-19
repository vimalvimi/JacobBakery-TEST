package com.roxybakestudio.jacobbakery.ui;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.roxybakestudio.jacobbakery.R;
import com.roxybakestudio.jacobbakery.adapter.IngredientsAdapter;
import com.roxybakestudio.jacobbakery.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment {

    private static final String TAG = "StepFragment";

    public static final String F_CURRENT_RECIPE = "f_current_recipe";
    public static final String F_CURRENT_STEP_POSITION = "f_position";

    private Recipe currentRecipe;
    private int currentStepPosition;

    private static Uri currentVideoUri;

    IngredientsAdapter ingredientsAdapter;

    @BindView(R.id.step_description_frag)
    TextView stepDisc;
    @BindView(R.id.recycler_view_ingredients)
    RecyclerView rvIngredients;
    @BindView(R.id.video_view)
    SimpleExoPlayerView mPlayerView;

    SimpleExoPlayer mExoPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentRecipe = (Recipe) getArguments().getSerializable(F_CURRENT_RECIPE);
        currentStepPosition = getArguments().getInt(F_CURRENT_STEP_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, view);

        stepDisc.setText(currentRecipe.getSteps().get(currentStepPosition).getDescription());
        currentVideoUri = Uri.parse(currentRecipe.getSteps().get(currentStepPosition).getUrl());

        if (currentStepPosition == 0) {
            stepDisc.setVisibility(View.GONE);
            getIngredients();
        }

        videoPlay();
        return view;
    }

    private void videoPlay() {
        if (currentVideoUri != null) {
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(currentVideoUri);
        } else {
            mPlayerView.setVisibility(View.GONE);
        }

        Log.d(TAG, "videoPlay: CURRENT URI = " + currentVideoUri);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "CurrentRecipeStep");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void getIngredients() {
        ingredientsAdapter = new IngredientsAdapter();
        ingredientsAdapter.addIngredient(currentRecipe.getIngredients());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), getScreenWidth());
        rvIngredients.setAdapter(ingredientsAdapter);
        rvIngredients.setLayoutManager(gridLayoutManager);
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExoPlayer != null) {
            releasePlayer();
        }
    }

    public int getScreenWidth() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return 1;
        } else {
            return 2;
        }
    }
}

