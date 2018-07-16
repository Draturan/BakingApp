package com.example.simone.bakingapp.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simone.bakingapp.R;
import com.example.simone.bakingapp.model.Sweet;
import com.example.simone.bakingapp.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simone on 19/06/2018 for BakingApp project
 */
public class SweetsAdapter extends RecyclerView.Adapter<SweetsAdapter.SweetViewHolder> {

    private final Context mContext;
    private ArrayList<Sweet> mSweetList;
    private final ListSweetClickListener mOnClickListener;

    public interface ListSweetClickListener{
        void onListSweetClick (Sweet sweetClicked, int clickedPosition);
    }

    SweetsAdapter(@NonNull Context mContext, @NonNull ArrayList<Sweet> sweetArrayList, ListSweetClickListener mOnClickListener) {
        this.mContext = mContext;
        this.mSweetList = sweetArrayList;
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public SweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.sweets_list,parent,false);
        return new SweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SweetViewHolder holder, int position) {
        holder.bind(position, mSweetList);
    }

    @Override
    public int getItemCount() { return (mSweetList == null) ? 0 : mSweetList.size(); }

    public class SweetViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        @BindView(R.id.iv_sweet_image) ImageView mSweetImage;
        @BindView(R.id.tv_sweet_name) TextView mSweetName;
        @BindView(R.id.tv_servings_number) TextView mSweetServings;
        @BindView(R.id.cv_sweet) CardView mCardView;
        @BindDrawable(R.drawable.ic_no_image) Drawable dNoImage;
        @BindDrawable(R.drawable.bakingapp_icon2) Drawable dNoImageAvailable;

        SweetViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int position, ArrayList<Sweet> mSweetList){
            Sweet sweet = mSweetList.get(position);
            Picasso.with(mContext)
                    .load(NetworkUtils.buildImageUrl(sweet.getImage()))
                    .placeholder(dNoImage)
                    .error(dNoImageAvailable)
                    .into(mSweetImage);
            mSweetName.setText(sweet.getName());
            mSweetServings.setText(String.valueOf(sweet.getServings()));

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListSweetClick(mSweetList.get(clickedPosition), clickedPosition);
            mCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.selectedSweet,null));
        }
    }

    public void updateData(ArrayList<Sweet> sweetArrayList){
        mSweetList = sweetArrayList;
        notifyDataSetChanged();
    }
}
