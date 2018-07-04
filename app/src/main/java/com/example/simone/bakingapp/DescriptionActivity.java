package com.example.simone.bakingapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.simone.bakingapp.fragments.IngredientsFragment;
import com.example.simone.bakingapp.fragments.StepsFragment;
import com.example.simone.bakingapp.model.Ingredient;
import com.example.simone.bakingapp.model.Step;
import com.example.simone.bakingapp.model.Sweet;

import java.util.ArrayList;

public class DescriptionActivity extends AppCompatActivity {

    private static final String TAG = DescriptionActivity.class.getSimpleName();
    private static final String TAG_INGREDIENTS_FRAGMENT = IngredientsFragment.class.getSimpleName();
    private static final String TAG_STEPS_FRAGMENT = StepsFragment.class.getSimpleName();
    private IngredientsFragment mIngredientsFragment;
    private ArrayList<Ingredient> mIngredientList;
    private StepsFragment mStepsFragment;
    private ArrayList<Step> mStepsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Intent intent = getIntent();
        if (intent.hasExtra("SweetObj")){
            final Sweet sweet = intent.getParcelableExtra("SweetObj");
            this.setTitle(sweet.getName());
            mIngredientList = sweet.getIngredients();
            mStepsList = sweet.getSteps();
        }
        if (savedInstanceState == null){
            mIngredientsFragment = IngredientsFragment.newInstance(mIngredientList);
            mStepsFragment = StepsFragment.newInstance(mStepsList);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.f_ingredients, mIngredientsFragment,TAG_INGREDIENTS_FRAGMENT)
                    .add(R.id.f_steps, mStepsFragment, TAG_STEPS_FRAGMENT)
                    .commit();


            Log.d(TAG, mIngredientList.toString());
        }

    }
}
