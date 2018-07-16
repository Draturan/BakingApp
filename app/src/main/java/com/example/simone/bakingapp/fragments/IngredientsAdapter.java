package com.example.simone.bakingapp.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simone.bakingapp.R;
import com.example.simone.bakingapp.model.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simone on 25/06/2018 for BakingApp project
 */
public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>{

    private Context mContext;
    private ArrayList<Ingredient> mIngredientsList;

    public IngredientsAdapter(@NonNull Context context, @NonNull ArrayList<Ingredient> ingredientArrayList){
        mContext = context;
        mIngredientsList = ingredientArrayList;
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.ingredients_list, parent, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        holder.bind(position, mIngredientsList);
    }

    @Override
    public int getItemCount() { return (mIngredientsList == null) ? 0 : mIngredientsList.size(); }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredients_tv_quantity) TextView mTextViewQuantity;
        @BindView(R.id.ingredients_tv_measure) TextView mTextViewMeasure;
        @BindView(R.id.ingredients_tv_ingredient) TextView mTextViewIngredient;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(int position, ArrayList<Ingredient> list){
            if (list != null){
                Ingredient ingredient = list.get(position);
                mTextViewQuantity.setText(String.valueOf(ingredient.getQuantity()));
                mTextViewMeasure.setText(ingredient.getMeasure());
                mTextViewIngredient.setText(ingredient.getIngredient());
            }
        }
    }

}
