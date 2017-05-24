package com.roxybakestudio.jacobbakery.ui;

import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
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

    public String currentRecipeStepPosition;
    public String currentRecipeStepDescription;
    public String currentRecipeStepVideoUrl;

    Uri currentRecipeUri;
    int currentStepPosition;

    private static final int STEP_LOADER_ID = 701;
    private static final int INGREDIENTS_LOADER_ID = 801;

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

        currentStepPosition = getArguments().getInt(A_CURRENT_STEP_POSITION);
        currentRecipeUri = getArguments().getParcelable(A_CURRENT_URI);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, view);

        getLoaderManager().initLoader(STEP_LOADER_ID, null, this);


//        currentVideoUri = Uri.parse(currentRecipeStepVideoUrl);

        if (currentStepPosition == 0) {
            stepDisc.setVisibility(View.GONE);
            getLoaderManager().initLoader(INGREDIENTS_LOADER_ID, null, this);
        }

//        videoPlay();
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
                currentRecipeStepVideoUrl = data.getColumnName(1);
                stepDisc.setText(currentRecipeStepDescription);

                break;
            case INGREDIENTS_LOADER_ID:
                getIngredients(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void getIngredients(Cursor data) {
        ingredientsAdapter = new IngredientsAdapter(getContext());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), getScreenWidth());
        rvIngredients.setLayoutManager(gridLayoutManager);

        rvIngredients.setAdapter(ingredientsAdapter);
        ingredientsAdapter.swapCursor(data);
    }

//    private void videoPlay() {
//        if (currentVideoUri != null) {
//            mPlayerView.setVisibility(View.VISIBLE);
//            initializePlayer(currentVideoUri);
//        } else {
//            mPlayerView.setVisibility(View.GONE);
//        }
//
//        Log.d(TAG, "videoPlay: CURRENT URI = " + currentVideoUri);
//    }
//
//    private void initializePlayer(Uri mediaUri) {
//        if (mExoPlayer == null) {
//            // Create an instance of the ExoPlayer.
//            TrackSelector trackSelector = new DefaultTrackSelector();
//            LoadControl loadControl = new DefaultLoadControl();
//            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
//            mPlayerView.setPlayer(mExoPlayer);
//
//            // Prepare the MediaSource.
//            String userAgent = Util.getUserAgent(getContext(), "CurrentRecipeStep");
//            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
//                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
//            mExoPlayer.prepare(mediaSource);
//            mExoPlayer.setPlayWhenReady(true);
//        }
//    }


//    private void releasePlayer() {
//        mExoPlayer.stop();
//        mExoPlayer.release();
//        mExoPlayer = null;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mExoPlayer != null) {
//            releasePlayer();
//        }
//    }

    public int getScreenWidth() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return 1;
        } else {
            return 2;
        }
    }
}

