package com.example.simone.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.simone.bakingapp.model.Sweet;

import java.util.ArrayList;

/**
 * Created by Simone on 19/06/2018 for BakingApp project
 */
public class SweetsAdapter extends RecyclerView.Adapter<SweetsAdapter.SweetViewHolder> {

    private final Context mContext;
    private ArrayList<Sweet> mSweetList;
    private final ListSweetClickListener mOnClickListener;

    public interface ListSweetClickListener{
        void onListSweetClick (Sweet sweetClicked);
    }

    public SweetsAdapter(@NonNull Context mContext, @NonNull ArrayList<Sweet> sweetArrayList, ListSweetClickListener mOnClickListener) {
        this.mContext = mContext;
        this.mSweetList = sweetArrayList;
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public SweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(SweetViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class SweetViewHolder extends RecyclerView.ViewHolder {

        public SweetViewHolder(View itemView) {
            super(itemView);
        }

    }
}
