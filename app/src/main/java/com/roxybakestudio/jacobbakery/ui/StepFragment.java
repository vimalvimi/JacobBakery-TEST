package com.roxybakestudio.jacobbakery.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.roxybakestudio.jacobbakery.data.RecipeContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "StepFragment";

    public static final String A_CURRENT_URI = "a_current_uri";
    public static final String A_CURRENT_STEP_POSITION = "a_position";

    public String currentRecipeStepDescription;
    public String currentRecipeStepVideoUrl;

    Uri currentRecipeUri;
    int currentStepPosition;

    private static final int STEP_LOADER_ID = 701;
    private static final int INGREDIENTS_LOADER_ID = 801;

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

        currentStepPosition = getArguments().getInt(A_CURRENT_STEP_POSITION);
        Log.d(TAG, "onCreate: " + currentStepPosition);
        currentRecipeUri = getArguments().getParcelable(A_CURRENT_URI);
        Log.d(TAG, "onCreate: " + currentRecipeUri);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, view);

        getLoaderManager().initLoader(STEP_LOADER_ID, null, this);

        if (currentStepPosition == 0) {
            stepDisc.setVisibility(View.GONE);
            getLoaderManager().initLoader(INGREDIENTS_LOADER_ID, null, this);
        }
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case STEP_LOADER_ID:
                String[] projectionRecipe = {
                        RecipeContract.RecipeSteps.COLUMN_DESCRIPTION,
                        RecipeContract.RecipeSteps.COLUMN_VIDEO,
                };

                Uri uriRecipe = RecipeContract.RecipeSteps
                        .BuildStepUriWithId(Long.parseLong(currentRecipeUri.getLastPathSegment()));

                return new CursorLoader(getContext(),
                        uriRecipe,
                        projectionRecipe,
                        null,
                        null,
                        null);

            case INGREDIENTS_LOADER_ID:
                String[] projectionIngredients = {
                        RecipeContract.RecipeIngredients.COLUMN_RECIPE_NAME,
                        RecipeContract.RecipeIngredients.COLUMN_QUANTITY,
                        RecipeContract.RecipeIngredients.COLUMN_MEASURE,
                        RecipeContract.RecipeIngredients.COLUMN_INGREDIENT
                };

                Uri uriIngredients = RecipeContract.RecipeIngredients
                        .buildIngredientUriWithId(Long.parseLong(currentRecipeUri.getLastPathSegment()));

                return new CursorLoader(getContext(),
                        uriIngredients,
                        projectionIngredients,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader is not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case STEP_LOADER_ID:
                data.moveToPosition(currentStepPosition);
                currentRecipeStepDescription = data.getString(0);
                currentRecipeStepVideoUrl = data.getString(1);

                stepDisc.setText(currentRecipeStepDescription);
                videoPlay(currentRecipeStepVideoUrl);
                break;
            case INGREDIENTS_LOADER_ID:
                getIngredients(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void getIngredients(Cursor data) {
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(getContext(), 1);

        ingredientsAdapter = new IngredientsAdapter(getContext());
        rvIngredients.setLayoutManager(gridLayoutManager);

        rvIngredients.setAdapter(ingredientsAdapter);
        ingredientsAdapter.swapCursor(data);
    }

    private void videoPlay(String currentVideoUri) {
        if (currentVideoUri.isEmpty()) {
            mPlayerView.setVisibility(View.GONE);
        } else {
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(currentVideoUri));
        }
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

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExoPlayer != null) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }
}

