package com.example.simone.bakingapp.fragments;

import android.content.Context;
import android.icu.text.SymbolTable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.TextUtils;
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
public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    private Context mContext;
    private ArrayList<Step> mStepsList;
    private final VideoStepsClickListener videoStepsClickListener;

    public interface VideoStepsClickListener {
        void onVideoStepClick(int position, View view);
    }

    StepsAdapter(@NonNull Context context, @NonNull ArrayList<Step> steps, VideoStepsClickListener listener) {
        mContext = context;
        mStepsList = steps;
        videoStepsClickListener = listener;
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
    public int getItemCount() {
        return (mStepsList == null) ? 0 : mStepsList.size();
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        @BindView(R.id.steps_tv_id)
        TextView mTVId;
        @BindView(R.id.steps_tv_short_description)
        TextView mTVShortDescription;
        @BindView(R.id.steps_tv_description)
        TextView mTVDescription;

        StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int position, ArrayList<Step> steps) {
            Step step = steps.get(position);
            if (step.getId() == 0) {
                mTVId.setText(R.string.tv_steps_zero_symbol);
            } else {
                mTVId.setText(String.valueOf(step.getId()));
            }

            mTVShortDescription.setText(step.getShortDescription());
            mTVDescription.setText(step.getDescription());
        }

        public View getItemView(int position) {
            //TODO completare
            return null;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            videoStepsClickListener.onVideoStepClick(position, v);
//            videoStepsClickListener.onVideoStepClick(mStepsList.get(position), v);
        }
    }
}
