package com.example.simone.bakingapp;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity
    implements SweetListMasterFragment.OnSweetClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG_SWEET_MASTER_FRAGMENT = SweetListMasterFragment.class.getSimpleName();
    private static final String TAG_INGREDIENTS_FRAGMENT = IngredientsFragment.class.getSimpleName();
    private static final String TAG_STEPS_FRAGMENT = StepsFragment.class.getSimpleName();
    public static final String FRAGMENT_BACKSTACK = "ReturnToCorrectFragmentLessie";

    @BindView(R.id.cl_tablet_view) ConstraintLayout mConstraintLayoutTabletView;
    private IngredientsFragment mIngredientsFragment;
    private ArrayList<Ingredient> mIngredientList;
    private StepsFragment mStepsFragment;
    private ArrayList<Step> mStepsList;
    boolean mAlignVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (mConstraintLayoutTabletView != null){
            mAlignVersion = true;
            if (savedInstanceState == null){
                mIngredientsFragment = IngredientsFragment.newInstance(mIngredientList);
                mStepsFragment = StepsFragment.newInstance(mStepsList);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.f_ingredients, mIngredientsFragment, TAG_INGREDIENTS_FRAGMENT)
                        .add(R.id.f_steps, mStepsFragment, TAG_STEPS_FRAGMENT)
                        .commit();
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
        Toast.makeText(this,"Sweet clicked: " + sweetClicked.toString(),Toast.LENGTH_SHORT).show();
        if (mAlignVersion){
            mIngredientList = sweetClicked.getIngredients();
            mStepsList = sweetClicked.getSteps();
            mIngredientsFragment.updateData(mIngredientList);

        }else {
            Intent startDescriptionActivityIntent = new Intent(this, DescriptionActivity.class);
            startDescriptionActivityIntent.putExtra("SweetObj", sweetClicked);
            startActivity(startDescriptionActivityIntent);
        }

    }
}
