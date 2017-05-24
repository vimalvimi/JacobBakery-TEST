package com.roxybakestudio.jacobbakery.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roxybakestudio.jacobbakery.R;
import com.roxybakestudio.jacobbakery.helper.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.roxybakestudio.jacobbakery.data.RecipeContract.RecipeIngredients.INDEX_COLUMN_INGREDIENT;
import static com.roxybakestudio.jacobbakery.data.RecipeContract.RecipeIngredients.INDEX_COLUMN_MEASURE;
import static com.roxybakestudio.jacobbakery.data.RecipeContract.RecipeIngredients.INDEX_COLUMN_QUANTITY;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder> {


    private Cursor mCursor;
    private Context mContext;

    public IngredientsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredients, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        holder.quantity.setText(mCursor.getString(INDEX_COLUMN_QUANTITY));
        holder.measure.setText(Utils.getMeasure(mCursor.getString(INDEX_COLUMN_MEASURE)));
        holder.ingredient.setText(Utils.capitalizeFirst(mCursor.getString(INDEX_COLUMN_INGREDIENT)));
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
        @BindView(R.id.cv_quantity)
        TextView quantity;
        @BindView(R.id.cv_measure)
        TextView measure;
        @BindView(R.id.cv_ingredient)
        TextView ingredient;

        private MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}
