package com.example.simone.bakingapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG_INGREDIENTS_FRAGMENT = IngredientsFragment.class.getSimpleName();
    private static final String SAVING_INGREDIENTS = "saveIngredients";
    private static final String TAG_STEPS_FRAGMENT = StepsFragment.class.getSimpleName();
    private static final String SAVING_STEPS= "saveSteps";

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
                mIngredientList = savedInstanceState.getParcelableArrayList(SAVING_INGREDIENTS);
                mStepsList = savedInstanceState.getParcelableArrayList(SAVING_STEPS);
            }

            mIngredientsFragment = (IngredientsFragment) getSupportFragmentManager().findFragmentByTag(TAG_INGREDIENTS_FRAGMENT);
            mStepsFragment = (StepsFragment) getSupportFragmentManager().findFragmentByTag(TAG_STEPS_FRAGMENT);
            if ((mIngredientsFragment == null) || (mStepsFragment == null)){
                mIngredientsFragment = IngredientsFragment.newInstance(mIngredientList);
                mStepsFragment = StepsFragment.newInstance(mStepsList);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.f_ingredients, mIngredientsFragment, TAG_INGREDIENTS_FRAGMENT)
                        .add(R.id.f_steps, mStepsFragment, TAG_STEPS_FRAGMENT)
                        .commit();
            }else{
                mIngredientsFragment.updateData(mIngredientList);
                mStepsFragment.updateData(mStepsList);
            }

            if (mIngredientList == null){
                noSelectedSweetDisplay(true);
            }else {
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
            outState.putParcelableArrayList(SAVING_INGREDIENTS, mIngredientList);
            outState.putParcelableArrayList(SAVING_STEPS, mStepsList);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSweetSelected(Sweet sweetClicked) {
        if (mAlignVersion){
            mIngredientList = sweetClicked.getIngredients();
            mStepsList = sweetClicked.getSteps();
            mIngredientsFragment.updateData(mIngredientList);
            mStepsFragment.updateData(mStepsList);
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
