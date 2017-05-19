package com.roxybakestudio.jacobbakery.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roxybakestudio.jacobbakery.R;
import com.roxybakestudio.jacobbakery.helper.Utils;
import com.roxybakestudio.jacobbakery.model.Ingredients;

import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder> {

    private List<Ingredients> mIngredients = new ArrayList<>();

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredients, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Ingredients ingredients = mIngredients.get(position);

        holder.quantity.setText(Utils.doubleToString(ingredients.getQuantity()));
        holder.measure.setText(Utils.getMeasure(ingredients.getMeasure()));
        holder.ingredient.setText(Utils.capitalizeFirst(ingredients.getIngredient()));
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView quantity, measure, ingredient;

        private MyViewHolder(View itemView) {
            super(itemView);
            quantity = (TextView) itemView.findViewById(R.id.cv_quantity);
            measure = (TextView) itemView.findViewById(R.id.cv_measure);
            ingredient = (TextView) itemView.findViewById(R.id.cv_ingredient);
        }
    }

    public void addIngredient(List<Ingredients> ingredient) {
        mIngredients = ingredient;
        notifyDataSetChanged();
    }
}
