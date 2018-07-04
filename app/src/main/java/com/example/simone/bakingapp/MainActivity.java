package com.example.simone.bakingapp;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simone.bakingapp.fragments.IngredientsFragment;
import com.example.simone.bakingapp.fragments.StepsFragment;
import com.example.simone.bakingapp.fragments.SweetListMasterFragment;
import com.example.simone.bakingapp.model.Ingredient;
import com.example.simone.bakingapp.model.Step;
import com.example.simone.bakingapp.model.Sweet;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity
    implements SweetListMasterFragment.OnSweetClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG_INGREDIENTS_FRAGMENT = IngredientsFragment.class.getSimpleName();
    private static final String TAG_STEPS_FRAGMENT = StepsFragment.class.getSimpleName();
    public static final String FRAGMENT_BACKSTACK = "ReturnToCorrectFragmentLessie";

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
            if (savedInstanceState == null){
                mIngredientsFragment = IngredientsFragment.newInstance(mIngredientList);
                mStepsFragment = StepsFragment.newInstance(mStepsList);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.f_ingredients, mIngredientsFragment, TAG_INGREDIENTS_FRAGMENT)
                        .add(R.id.f_steps, mStepsFragment, TAG_STEPS_FRAGMENT)
                        .commit();
            }
            if (mIngredientList == null){
                noSelectedSweetDisplay(true);
            }
        }else {
            mAlignVersion = false;
        }
        Log.d(TAG,"Siamo in un tablet? " + ((mAlignVersion) ? "SI!" : "NO!"));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSweetSelected(Sweet sweetClicked) {
        Toast.makeText(this,"Sweet clicked: " + sweetClicked.getId().toString(),Toast.LENGTH_SHORT).show();
        if (mAlignVersion){
            mIngredientList = sweetClicked.getIngredients();
            mStepsList = sweetClicked.getSteps();
            mIngredientsFragment.updateData(mIngredientList);
            mStepsFragment.updateData(mStepsList);
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.f_ingredients, mIngredientsFragment, TAG_INGREDIENTS_FRAGMENT)
//                    .replace(R.id.f_steps, mStepsFragment, TAG_STEPS_FRAGMENT)
//                    .commit();
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
