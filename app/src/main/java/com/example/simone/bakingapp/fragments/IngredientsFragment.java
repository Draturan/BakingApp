package com.example.simone.bakingapp.fragments;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simone.bakingapp.R;
import com.example.simone.bakingapp.model.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsFragment extends Fragment {

    public static final String TAG = IngredientsFragment.class.getSimpleName();
    public static final String ARG_INGREDIENTS = "ingredients";

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

        // Inflate the layout for this fragment
        return view;
    }

    public void updateData(ArrayList<Ingredient> ingredientArrayList){
        mIngredients = ingredientArrayList;
        ingredientsAdapter.updateData(mIngredients);
    }

}
