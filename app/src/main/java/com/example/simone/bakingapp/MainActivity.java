package com.example.simone.bakingapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simone.bakingapp.fragments.IngredientsFragment;
import com.example.simone.bakingapp.fragments.StepsFragment;
import com.example.simone.bakingapp.fragments.SweetListMasterFragment;
import com.example.simone.bakingapp.model.Ingredient;
import com.example.simone.bakingapp.model.Step;
import com.example.simone.bakingapp.model.Sweet;

import java.util.ArrayList;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity
    implements SweetListMasterFragment.OnSweetClickListener{

    private static final String TAG = MainActivity.class.getName();
    private static final String TAG_INGREDIENTS_FRAGMENT = IngredientsFragment.class.getName();
    private static final String SAVE_INGREDIENTS_FRAGMENT = "Instance Ingredients Fragment";
    private static final String TAG_STEPS_FRAGMENT = StepsFragment.class.getName();
    private static final String SAVE_STEPS_FRAGMENT = "Instance Steps Fragment";
    private static final String SAVING_SWEET= "saveSweet";

    private Sweet lastSweet;
    private IngredientsFragment mIngredientsFragment;
    private ArrayList<Ingredient> mIngredientList;
    private StepsFragment mStepsFragment;
    private ArrayList<Step> mStepsList;
    boolean mAlignVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.cl_tablet_view) != null){
            mAlignVersion = true;
            if (savedInstanceState != null){
                mIngredientsFragment = (IngredientsFragment) getSupportFragmentManager().getFragment(savedInstanceState, SAVE_INGREDIENTS_FRAGMENT);
                mStepsFragment = (StepsFragment) getSupportFragmentManager().getFragment(savedInstanceState, SAVE_STEPS_FRAGMENT);
                lastSweet = savedInstanceState.getParcelable(SAVING_SWEET);
                if (lastSweet != null){
                    mIngredientList = lastSweet.getIngredients();
                    mStepsList = lastSweet.getSteps();
                }
            }

            if ((mIngredientsFragment == null) && (mStepsFragment == null)){
                mIngredientsFragment = IngredientsFragment.newInstance(mIngredientList);
                mStepsFragment = StepsFragment.newInstance(mStepsList);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.f_ingredients, mIngredientsFragment, TAG_INGREDIENTS_FRAGMENT)
                        .add(R.id.f_steps, mStepsFragment, TAG_STEPS_FRAGMENT)
                        .commit();
                noSelectedSweetDisplay(true);
            }else{
                noSelectedSweetDisplay(false);
            }
        }else {
            mAlignVersion = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAlignVersion){
            getSupportFragmentManager().putFragment(outState, SAVE_INGREDIENTS_FRAGMENT, getSupportFragmentManager().findFragmentByTag(TAG_INGREDIENTS_FRAGMENT));
            getSupportFragmentManager().putFragment(outState, SAVE_STEPS_FRAGMENT, getSupportFragmentManager().findFragmentByTag(TAG_STEPS_FRAGMENT));
            outState.putParcelable(SAVING_SWEET, lastSweet);
        }

    }

    @Override
    public void onSweetSelected(Sweet sweetClicked) {
        if (mAlignVersion){
            lastSweet = sweetClicked;
            mIngredientList = lastSweet.getIngredients();
            mStepsList = lastSweet.getSteps();
            mIngredientsFragment = IngredientsFragment.newInstance(mIngredientList);
            mStepsFragment = StepsFragment.newInstance(mStepsList);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.f_ingredients, mIngredientsFragment, TAG_INGREDIENTS_FRAGMENT)
                    .replace(R.id.f_steps, mStepsFragment, TAG_STEPS_FRAGMENT)
                    .commit();
            noSelectedSweetDisplay(false);
        }else {
            Intent startDescriptionActivityIntent = new Intent(this, DescriptionActivity.class);
            startDescriptionActivityIntent.putExtra("SweetObj", sweetClicked);
            startActivity(startDescriptionActivityIntent);
        }

    }

    public void noSelectedSweetDisplay(boolean display){
        if (mAlignVersion){
            ImageView mNoSelectedImageView = findViewById(R.id.iv_sweet_no_selection);
            TextView mNoSelectedTextView = findViewById(R.id.tv_sweet_no_selection);

            if (display){
                mNoSelectedImageView.setVisibility(View.VISIBLE);
                mNoSelectedTextView.setVisibility(View.VISIBLE);
            }else{
                mNoSelectedImageView.setVisibility(GONE);
                mNoSelectedTextView.setVisibility(GONE);
            }
        }

    }
}
