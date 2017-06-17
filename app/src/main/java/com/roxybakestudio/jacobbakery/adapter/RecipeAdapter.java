package com.roxybakestudio.jacobbakery.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.roxybakestudio.jacobbakery.R;
import com.roxybakestudio.jacobbakery.data.RecipeContract;
import com.roxybakestudio.jacobbakery.helper.Utils;
import com.roxybakestudio.jacobbakery.ui.StepsListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.roxybakestudio.jacobbakery.data.RecipeContract.RecipeMain.INDEX_RECIPE_ID;
import static com.roxybakestudio.jacobbakery.data.RecipeContract.RecipeMain.INDEX_RECIPE_NAME;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

    private static final String TAG = "RecipeAdapter";

    private Cursor mCursor;
    private Context mContext;

    @Override
    public RecipeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        holder.mTextView.setText(mCursor.getString(INDEX_RECIPE_NAME));
        Glide.with(mContext)
                .load(Utils.getImage(mCursor.getString(INDEX_RECIPE_NAME)))
                .crossFade()
                .into(holder.mImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCursor.moveToPosition(holder.getAdapterPosition());
                Uri uri = RecipeContract.RecipeMain.BuildRecipeUriWithId(mCursor.getLong(INDEX_RECIPE_ID));

                Intent intent = new Intent(mContext, StepsListActivity.class);
                intent.setData(uri);
                intent.putExtra("title", mCursor.getString(INDEX_RECIPE_NAME));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.main_recipe_name)
        TextView mTextView;
        @BindView(R.id.main_image)
        ImageView mImageView;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public RecipeAdapter(Context context) {
        mContext = context;
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}