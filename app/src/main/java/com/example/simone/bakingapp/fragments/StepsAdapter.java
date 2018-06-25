package com.example.simone.bakingapp.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simone.bakingapp.R;
import com.example.simone.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simone on 25/06/2018 for BakingApp project
 */
public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder>{

    private Context mContext;
    private ArrayList<Step> mStepsList;

    public StepsAdapter(Context context, ArrayList<Step> steps){
        mContext = context;
        mStepsList = steps;
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.steps_list, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
        holder.bind(position, mStepsList);
    }

    @Override
    public int getItemCount() { return (mStepsList == null) ? 0 : mStepsList.size(); }

    public class StepsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.steps_tv_quantity) TextView mTextView;

        public StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(mContext, itemView);
        }

        public void bind(int position, ArrayList<Step> steps){
            Step step = steps.get(position);
            mTextView.setText(step.getDescription());
        }
    }

    public void dataUpdate(ArrayList<Step> newSteps){
        mStepsList = newSteps;
        notifyDataSetChanged();
    }
}
