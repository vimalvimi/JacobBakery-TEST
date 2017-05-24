package com.roxybakestudio.jacobbakery.ui;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.roxybakestudio.jacobbakery.R;
import com.roxybakestudio.jacobbakery.adapter.RecipeAdapter;
import com.roxybakestudio.jacobbakery.data.RecipeContract;
import com.roxybakestudio.jacobbakery.helper.Utils;
import com.roxybakestudio.jacobbakery.rest.RetrofitCall;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";

    private static final int RECIPE_LOADER_ID = 501;

    private RecipeAdapter recipeAdapter;

    @BindView(R.id.recycler_view_recipes)
    RecyclerView mRecyclerView;

    @BindView(R.id.no_internet_error_text)
    TextView mTextViewNoInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (Utils.isNetworkAvailable(this)) {
            RetrofitCall.getRecipes(this);
            getSupportLoaderManager().initLoader(RECIPE_LOADER_ID, null, this);
            getRecyclerView();
        } else {
            getError();
        }
    }

    private void getRecyclerView() {
        mTextViewNoInternet.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, getScreenWidth());

        mRecyclerView.setLayoutManager(layoutManager);
        recipeAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(recipeAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                RecipeContract.RecipeMain.COLUMN_NAME,
                RecipeContract.RecipeMain.COLUMN_RECIPE_ID,
                RecipeContract.RecipeMain.COLUMN_SERVINGS,
        };

        switch (id) {
            case RECIPE_LOADER_ID:
                return new CursorLoader(this,
                        RecipeContract.RecipeMain.CONTENT_URI,
                        projection,
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
            case RECIPE_LOADER_ID:
                recipeAdapter.swapCursor(data);
                if (data != null && data.getCount() != 0) {
                    data.moveToFirst();
                }
                break;
            default:
                throw new RuntimeException("Loader not Implemented: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recipeAdapter.swapCursor(null);
    }


    private void getError() {
        mRecyclerView.setVisibility(View.GONE);
        mTextViewNoInternet.setVisibility(View.VISIBLE);
    }

    public int getScreenWidth() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return 1;
        } else {
            return 2;
        }
    }
}