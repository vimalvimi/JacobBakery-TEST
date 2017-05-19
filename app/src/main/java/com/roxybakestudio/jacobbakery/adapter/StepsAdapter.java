package com.roxybakestudio.jacobbakery.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roxybakestudio.jacobbakery.R;
import com.roxybakestudio.jacobbakery.model.Steps;

import java.util.ArrayList;
import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.MyViewHolder> {

    private static final String TAG = "StepsAdapter";

    private List<Steps> mSteps = new ArrayList<>();
    private ClickListener mClickListener;

    public StepsAdapter(List<Steps> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 1;
        else return 2;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.item_step_zero, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.item_step, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Steps steps = mSteps.get(position);

        holder.stepNumber.setText("Step #" + steps.getStepsId() + " ");
        holder.shortDescription.setText(steps.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView stepNumber, shortDescription;

        MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            stepNumber = (TextView) itemView.findViewById(R.id.step_number);
            shortDescription = (TextView) itemView.findViewById(R.id.step_name);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            if (mClickListener != null) {
                mClickListener.itemClicked(v, getAdapterPosition());
            }
        }
    }

    public interface ClickListener {
        void itemClicked(View view, int position);
    }
}
