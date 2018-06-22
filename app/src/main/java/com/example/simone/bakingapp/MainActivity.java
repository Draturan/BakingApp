package com.example.simone.bakingapp;

import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.simone.bakingapp.async.AsyncTaskCompleteListener;
import com.example.simone.bakingapp.async.RetrieveSweetsTask;
import com.example.simone.bakingapp.model.Sweet;
import com.example.simone.bakingapp.utils.JsonUtils;
import com.example.simone.bakingapp.utils.NetworkUtils;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements SweetsAdapter.ListSweetClickListener{

    @BindView(R.id.rv_sweets_list) RecyclerView sweetList;
    @BindView(R.id.pb_sweets_list) ProgressBar mProgressBar;
    @BindView(R.id.tv_internet_error) TextView mInternetMessage;
    @BindView(R.id.iv_internet_error) ImageView mInternetImageView;

    private static final String TAG = MainActivity.class.getSimpleName();
    private SweetsAdapter sweetsAdapter;
    private ArrayList<Sweet> mSweetList;

    private static final String LAST_POSITION_RV = "last.recycler.layout";
    private Parcelable mSavedRecyclerLayoutState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        sweetList.setLayoutManager(linearLayoutManager);
        sweetList.setHasFixedSize(true);

        startRetrievingSweetsInfo();

        sweetsAdapter = new SweetsAdapter(this, mSweetList, this);
        sweetList.setAdapter(sweetsAdapter);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LAST_POSITION_RV, sweetList.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            mSavedRecyclerLayoutState = savedInstanceState.getParcelable(LAST_POSITION_RV);
            sweetList.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
        }
    }

    @Override
    public void onListSweetClick(Sweet sweetClicked) {

    }

    public void startRetrievingSweetsInfo(){
        if (NetworkUtils.isNetworkAvailable(this)){
            new RetrieveSweetsTask(this, new AsyncTaskCompleteListener<String>() {
                @Override
                public void onPreTaskExecute() {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mInternetMessage.setVisibility(View.GONE);
                    mInternetImageView.setVisibility(View.GONE);
                }

                @Override
                public void onTaskComplete(String result) {
                    mProgressBar.setVisibility(View.GONE);
                    if (result != null && !result.isEmpty()){
                        try{
                            mSweetList = new JsonUtils().parseAnswerJson(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        sweetsAdapter.updateData(mSweetList);
                        if (mSavedRecyclerLayoutState != null){
                            sweetList.getLayoutManager()
                                    .onRestoreInstanceState(mSavedRecyclerLayoutState);
                        }
                    }
                }
            }).execute();
        }else{
            connectionMissing();
        }
    }

    public void connectionMissing(){
        mInternetMessage.setText(R.string.tv_internet_no_connection);
        mInternetMessage.setVisibility(View.VISIBLE);
        mInternetImageView.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startRetrievingSweetsInfo();
            }
        }, 1000);
    }
}
