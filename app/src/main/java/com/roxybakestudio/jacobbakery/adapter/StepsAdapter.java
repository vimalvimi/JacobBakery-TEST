package com.roxybakestudio.jacobbakery.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roxybakestudio.jacobbakery.R;
import com.roxybakestudio.jacobbakery.data.RecipeContract;
import com.roxybakestudio.jacobbakery.ui.StepActivity;
import com.roxybakestudio.jacobbakery.ui.StepFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.roxybakestudio.jacobbakery.data.RecipeContract.RecipeSteps.INDEX_SHORT_DESCRIPTION;
import static com.roxybakestudio.jacobbakery.data.RecipeContract.RecipeSteps.INDEX_STEP_ID;
import static com.roxybakestudio.jacobbakery.data.RecipeContract.RecipeSteps.INDEX_STEP_RECIPE_ID;
import static com.roxybakestudio.jacobbakery.ui.StepFragment.A_CURRENT_STEP_POSITION;
import static com.roxybakestudio.jacobbakery.ui.StepFragment.A_CURRENT_URI;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.MyViewHolder> {

    private static final String TAG = "StepsAdapter";

    private final Context mContext;
    private Cursor stepCursor;

    private Boolean mTwoPane;

    public StepsAdapter(Context context, Boolean twoPane) {
        mContext = context;
        mTwoPane = twoPane;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.item_step_zero, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.item_step, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        stepCursor.moveToPosition(position);

        holder.stepNumber.setText("Step #" + stepCursor.getString(INDEX_STEP_ID) + " ");
        holder.shortDescription.setText(stepCursor.getString(INDEX_SHORT_DESCRIPTION));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stepCursor.moveToPosition(holder.getAdapterPosition());
                Uri uri = RecipeContract.RecipeSteps
                        .BuildStepUriWithId(stepCursor.getLong(INDEX_STEP_RECIPE_ID));

                if (mTwoPane) {
                    Bundle args = new Bundle();
                    args.putInt(A_CURRENT_STEP_POSITION, Integer.parseInt(stepCursor.getString(INDEX_STEP_ID)));
                    args.putParcelable(A_CURRENT_URI, uri);

                    StepFragment stepFragment = new StepFragment();
                    stepFragment.setArguments(args);

                    FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.step_details_container_frame, stepFragment)
                            .commit();
                } else {
                    Intent intent = new Intent(mContext, StepActivity.class);
                    intent.putExtra("size_steps", stepCursor.getCount());
                    intent.putExtra("step_position", stepCursor.getString(INDEX_STEP_ID));
                    intent.setData(uri);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (stepCursor == null) {
            return 0;
        } else {
            return stepCursor.getCount();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.step_number)
        TextView stepNumber;
        @BindView(R.id.step_name)
        TextView shortDescription;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 1;
        else return 2;
    }

    public void swapStepCursor(Cursor newCursor) {
        stepCursor = newCursor;
        notifyDataSetChanged();
    }
}
