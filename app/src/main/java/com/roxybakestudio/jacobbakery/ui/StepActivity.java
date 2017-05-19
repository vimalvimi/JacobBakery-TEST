package com.roxybakestudio.jacobbakery.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.roxybakestudio.jacobbakery.R;
import com.roxybakestudio.jacobbakery.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity {

    public static final String A_CURRENT_RECIPE = "a_current_recipe";
    public static final String A_CURRENT_STEP_POSITION = "a_position";

    @BindView(R.id.previous_step)
    TextView previousStep;

    @BindView(R.id.next_step)
    TextView nextStep;

    Recipe currentRecipe;
    int currentStepPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);
        ButterKnife.bind(this);

        previousStep.setText(R.string.previous_step);
        nextStep.setText(R.string.next_step);

        currentRecipe = (Recipe) getIntent().getSerializableExtra(A_CURRENT_RECIPE);
        currentStepPosition = getIntent().getIntExtra(A_CURRENT_STEP_POSITION, 0);

        setTitle(currentRecipe.getName() + " Step #" + currentStepPosition);

        if (savedInstanceState == null) {
            getFragment();
        }
    }

    public void moveToPrevious(View view) {
        currentStepPosition--;
        getFragment();
    }

    public void moveToNext(View view) {
        currentStepPosition++;
        getFragment();
    }

    public void getFragment() {

        if (currentStepPosition == 0) {
            setTitle(currentRecipe.getName() + " Introduction");
        } else {
            setTitle(currentRecipe.getName() + " Step #" + currentStepPosition);
        }

        Bundle args = new Bundle();
        args.putInt(StepFragment.F_CURRENT_STEP_POSITION, currentStepPosition);
        args.putSerializable(StepFragment.F_CURRENT_RECIPE, currentRecipe);

        StepFragment stepFragment = new StepFragment();
        stepFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_details_container_a, stepFragment)
                .commit();

        getButtons();
    }

    private void getButtons() {
        if (currentRecipe.getSteps().get(currentStepPosition) == currentRecipe.getSteps().get(0)) {
            previousStep.setVisibility(View.INVISIBLE);
            nextStep.setVisibility(View.VISIBLE);

        } else if (currentRecipe.getSteps().get(currentStepPosition)
                == currentRecipe.getSteps().get(currentRecipe.getSteps().size() - 1)) {
            previousStep.setVisibility(View.VISIBLE);
            nextStep.setVisibility(View.INVISIBLE);

        } else {
            previousStep.setVisibility(View.VISIBLE);
            nextStep.setVisibility(View.VISIBLE);
        }
    }
}
