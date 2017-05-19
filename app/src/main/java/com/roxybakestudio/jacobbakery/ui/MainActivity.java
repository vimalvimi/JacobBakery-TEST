package com.roxybakestudio.jacobbakery.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.roxybakestudio.jacobbakery.R;
import com.roxybakestudio.jacobbakery.adapter.RecipeAdapter;
import com.roxybakestudio.jacobbakery.helper.Utils;
import com.roxybakestudio.jacobbakery.model.Recipe;
import com.roxybakestudio.jacobbakery.rest.RecipeService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ClickListener {

    private static final String TAG = "MainActivity";

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
            getRecyclerView();
            getFeed();
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

        recipeAdapter.setClickListener(this);
        mRecyclerView.setAdapter(recipeAdapter);
    }

    private void getFeed() {

        RecipeService recipeService = RecipeService.retrofit.create(RecipeService.class);
        Call<List<Recipe>> listCall = recipeService.getAllRecipes();
        listCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if (response.isSuccessful()) {
                    List<Recipe> recipes = response.body();

                    for (int i = 0; i < recipes.size(); i++) {
                        Recipe recipe = recipes.get(i);
                        recipeAdapter.addRecipe(recipe);
                    }
                } else {
                    Log.d(TAG, "RESPONSE CODE ERROR " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getError() {
        mRecyclerView.setVisibility(View.GONE);
        mTextViewNoInternet.setVisibility(View.VISIBLE);
    }

    @Override
    public void itemClicked(View view, int position) {
        Log.d(TAG, "itemClicked: " + position);

        Recipe recipe = recipeAdapter.getRecipes().get(position);

        Intent intent = new Intent(this, StepsListActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }

    public int getScreenWidth() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return 1;
        } else {
            return 2;
        }
    }
}