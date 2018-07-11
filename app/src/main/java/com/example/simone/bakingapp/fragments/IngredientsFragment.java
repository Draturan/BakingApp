package com.example.simone.bakingapp.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simone.bakingapp.R;
import com.example.simone.bakingapp.model.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class IngredientsFragment extends Fragment {

    public static final String TAG = IngredientsFragment.class.getName();
    private static final String ARG_INGREDIENTS = "ingredients_arg";
    private static final String LAST_POSITION_RV = "last.ingredient.rv.position";
    private Parcelable mSavedRecyclerLayoutState;

    @BindView(R.id.tv_ingredients_list_title) TextView mTVTitle;
    @BindView(R.id.rv_ingredients_list) RecyclerView ListIngredients;
    private ArrayList<Ingredient> mIngredients;
    private IngredientsAdapter ingredientsAdapter;

    public IngredientsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment IngredientsFragment.
     */
    public static IngredientsFragment newInstance(@Nullable ArrayList<Ingredient> ingredients) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        if (ingredients != null){
            args.putParcelableArrayList(ARG_INGREDIENTS, ingredients);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIngredients = getArguments().getParcelableArrayList(ARG_INGREDIENTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        ListIngredients.setLayoutManager(linearLayoutManager);
        ListIngredients.setHasFixedSize(true);

        ingredientsAdapter = new IngredientsAdapter(getContext(), mIngredients);
        ListIngredients.setAdapter(ingredientsAdapter);

        if (mIngredients == null){
            noSelectedSweetDisplay(true);
        }else {
            noSelectedSweetDisplay(false);
        }
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LAST_POSITION_RV, ListIngredients.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null){
            mSavedRecyclerLayoutState = savedInstanceState.getParcelable(LAST_POSITION_RV);
            ListIngredients.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
        }
    }


    public void noSelectedSweetDisplay(boolean display){
        if (display){
            mTVTitle.setVisibility(GONE);
        }else{
            mTVTitle.setVisibility(VISIBLE);
        }
    }

}
