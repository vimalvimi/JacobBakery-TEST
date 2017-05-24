package com.roxybakestudio.jacobbakery.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.roxybakestudio.jacobbakery.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.roxybakestudio.jacobbakery.ui.StepFragment.A_CURRENT_STEP_POSITION;
import static com.roxybakestudio.jacobbakery.ui.StepFragment.A_CURRENT_URI;

public class StepActivity
        extends AppCompatActivity {

    private static final String TAG = "StepActivity";

    @BindView(R.id.previous_step)
    TextView previousStep;

    @BindView(R.id.next_step)
    TextView nextStep;

    int currentStepPosition;
    int currentStepSize;
    Uri currentRecipeUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);
        ButterKnife.bind(this);

        previousStep.setText(R.string.previous_step);
        nextStep.setText(R.string.next_step);

        if (getIntent().hasExtra("step_position")) {
            String currentStepPositionString = getIntent().getStringExtra("step_position");
            currentStepPosition = Integer.parseInt(currentStepPositionString);
            currentStepSize = getIntent().getIntExtra("size_steps", 0);
        }
        if (getIntent().getData() != null) {
            currentRecipeUri = getIntent().getData();
        }
        getFragment();
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
            setTitle("Introduction");
        } else {
            setTitle("Step #" + currentStepPosition);
        }

        Bundle args = new Bundle();
        args.putInt(A_CURRENT_STEP_POSITION, currentStepPosition);
        args.putParcelable(A_CURRENT_URI, currentRecipeUri);

        StepFragment stepFragment = new StepFragment();
        stepFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_details_container_a, stepFragment)
                .commit();
        getButtons();
    }


    private void getButtons() {
        if (currentStepPosition == 0) {
            previousStep.setVisibility(View.INVISIBLE);
            nextStep.setVisibility(View.VISIBLE);

        } else if (currentStepPosition == currentStepSize - 1) {
            previousStep.setVisibility(View.VISIBLE);
            nextStep.setVisibility(View.INVISIBLE);

        } else {
            previousStep.setVisibility(View.VISIBLE);
            nextStep.setVisibility(View.VISIBLE);
        }
    }
}
