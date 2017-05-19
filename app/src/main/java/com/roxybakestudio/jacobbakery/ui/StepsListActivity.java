package com.roxybakestudio.jacobbakery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.roxybakestudio.jacobbakery.R;
import com.roxybakestudio.jacobbakery.adapter.StepsAdapter;
import com.roxybakestudio.jacobbakery.model.Recipe;

public class StepsListActivity extends AppCompatActivity implements StepsAdapter.ClickListener {

    Recipe currentRecipe;

    private boolean mTwoPane;

    private StepsAdapter stepsAdapter;

    private static final String TAG = "StepsListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        currentRecipe = (Recipe) getIntent().getSerializableExtra("recipe");
        setTitle(currentRecipe.getName());

        setupRecyclerView((RecyclerView) findViewById(R.id.recycler_view_steps));

        if (findViewById(R.id.step_details_container_frame) != null) {
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        stepsAdapter = new StepsAdapter(currentRecipe.getSteps());
        stepsAdapter.setClickListener(this);

        recyclerView.setAdapter(stepsAdapter);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void itemClicked(View view, int position) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putInt(StepFragment.F_CURRENT_STEP_POSITION, position);
            args.putSerializable(StepFragment.F_CURRENT_RECIPE, currentRecipe);

            StepFragment stepFragment = new StepFragment();
            stepFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_details_container_frame, stepFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putExtra(StepActivity.A_CURRENT_STEP_POSITION, position);
            intent.putExtra(StepActivity.A_CURRENT_RECIPE, currentRecipe);
            startActivity(intent);
        }
    }
}