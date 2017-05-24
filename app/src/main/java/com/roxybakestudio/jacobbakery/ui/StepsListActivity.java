package com.roxybakestudio.jacobbakery.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.roxybakestudio.jacobbakery.R;
import com.roxybakestudio.jacobbakery.adapter.StepsAdapter;
import com.roxybakestudio.jacobbakery.data.RecipeContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsListActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private boolean mTwoPane;
    private StepsAdapter stepsAdapter;
    private Uri recipeUri;

    @BindView(R.id.recycler_view_steps)
    RecyclerView recyclerViewSteps;

    private static final String TAG = "StepsListActivity";

    private static final int STEP_LOADER_ID = 601;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        ButterKnife.bind(this);

        //Check if Two Pane
        if (findViewById(R.id.step_details_container_frame) != null) {
            mTwoPane = true;
        }

        //Get Title
        if (getIntent().hasExtra("title")) {
            setTitle(getIntent().getStringExtra("title"));
        }

        //Get Uri
        if (getIntent().getData() != null) {
            recipeUri = getIntent().getData();
        }

        //Setup Recycler View
        setupRecyclerView(recyclerViewSteps);

        //Loader
        getSupportLoaderManager().initLoader(STEP_LOADER_ID, null, this);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        stepsAdapter = new StepsAdapter(this, mTwoPane);
        recyclerView.setAdapter(stepsAdapter);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                RecipeContract.RecipeSteps.COLUMN_RECIPE_ID,
                RecipeContract.RecipeSteps.COLUMN_SHORT_DESCRIPTION,
                RecipeContract.RecipeSteps.COLUMN_STEP_ID
        };

        Uri uri = RecipeContract.RecipeSteps
                .BuildStepUriWithId(Long.parseLong(recipeUri.getLastPathSegment()));

        return new CursorLoader(this,
                uri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        stepsAdapter.swapStepCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}