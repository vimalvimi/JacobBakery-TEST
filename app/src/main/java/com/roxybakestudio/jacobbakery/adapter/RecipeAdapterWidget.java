package com.roxybakestudio.jacobbakery.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.roxybakestudio.jacobbakery.R;
import com.roxybakestudio.jacobbakery.helper.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.roxybakestudio.jacobbakery.data.RecipeContract.RecipeMain.INDEX_RECIPE_NAME;

public class RecipeAdapterWidget extends RecyclerView.Adapter<RecipeAdapterWidget.MyViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    private ClickListener mClickListener;

    @Override
    public RecipeAdapterWidget.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        holder.mTextView.setText(mCursor.getString(INDEX_RECIPE_NAME));
        Glide.with(mContext)
                .load(Utils.getImage(mCursor.getString(INDEX_RECIPE_NAME)))
                .crossFade()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.main_recipe_name)
        TextView mTextView;
        @BindView(R.id.main_image)
        ImageView mImageView;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.itemClicked(v, getAdapterPosition());
            }
        }
    }

    public RecipeAdapterWidget(Context context, ClickListener clickListener) {
        mClickListener = clickListener;
        mContext = context;
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void itemClicked(View view, int position);
    }
}