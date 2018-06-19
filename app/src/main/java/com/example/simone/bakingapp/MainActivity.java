package com.example.simone.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.example.simone.bakingapp.model.Sweet;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements SweetsAdapter.ListSweetClickListener{

    @BindView(R.id.rv_sweets_list) RecyclerView sweetList;
    @BindView(R.id.pb_sweets_list) ProgressBar mProgressBar;

    private SweetsAdapter sweetsAdapter;
    private ArrayList<Sweet> mSweetList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        sweetList.setLayoutManager(linearLayoutManager);
        sweetList.setHasFixedSize(true);

        sweetsAdapter = new SweetsAdapter(this, mSweetList, this);
        sweetList.setAdapter(sweetsAdapter);

    }

    @Override
    public void onListSweetClick(Sweet sweetClicked) {

    }
}
