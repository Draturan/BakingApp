package com.example.simone.bakingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.simone.bakingapp.R;
import com.example.simone.bakingapp.async.AsyncTaskCompleteListener;
import com.example.simone.bakingapp.async.RetrieveSweetsTask;
import com.example.simone.bakingapp.model.Sweet;
import com.example.simone.bakingapp.utils.JsonUtils;
import com.example.simone.bakingapp.utils.NetworkUtils;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SweetListMasterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SweetListMasterFragment extends Fragment
        implements SweetsAdapter.ListSweetClickListener {

    @BindView(R.id.rv_sweets_list) RecyclerView sweetList;
    @BindView(R.id.pb_sweets_list) ProgressBar mProgressBar;
    @BindView(R.id.tv_internet_error) TextView mInternetMessage;
    @BindView(R.id.iv_internet_error) ImageView mInternetImageView;

    private SweetsAdapter sweetsAdapter;
    private ArrayList<Sweet> mSweetList;
    private static final String SWEET_LIST = "sweet list";
    public static final String ARG_SWEETS = "sweets_arg";

    private static final String LAST_POSITION_RV = "last.recycler.layout";
    private Parcelable mSavedRecyclerLayoutState;
    public OnSweetClickListener mListener;
    private static final String LAST_SWEET_CLICKED = "last.sweet.clicked";
    private Integer lastSweetClicked;

    public SweetListMasterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SweetListMasterFragment.
     */
    public static SweetListMasterFragment newInstance(@Nullable ArrayList<Sweet> sweets) {
        SweetListMasterFragment fragment = new SweetListMasterFragment();
        Bundle args = new Bundle();
        if (sweets != null){
            args.putParcelableArrayList(ARG_SWEETS, sweets);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSweetList = getArguments().getParcelableArrayList(ARG_SWEETS);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sweet_list_master, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        sweetList.setLayoutManager(linearLayoutManager);
        sweetList.setHasFixedSize(true);

        if (savedInstanceState != null){
            mSweetList = savedInstanceState.getParcelableArrayList(SWEET_LIST);
        }else{
            startRetrievingSweetsInfo();
        }

        sweetsAdapter = new SweetsAdapter(getContext(), mSweetList, this);
        sweetList.setAdapter(sweetsAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SWEET_LIST, mSweetList);
        outState.putParcelable(LAST_POSITION_RV, sweetList.getLayoutManager().onSaveInstanceState());
        if (lastSweetClicked != null){
            outState.putInt(LAST_SWEET_CLICKED, lastSweetClicked);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable final Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null){
            //found this solution here: https://stackoverflow.com/questions/42514011/how-to-retain-recyclerviews-position-after-orientation-change-while-using-fire
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSavedRecyclerLayoutState = savedInstanceState.getParcelable(LAST_POSITION_RV);
                    sweetList.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
                    mSweetList = savedInstanceState.getParcelableArrayList(SWEET_LIST);
                    lastSweetClicked = savedInstanceState.getInt(LAST_SWEET_CLICKED);
                    sweetList.getLayoutManager().getChildAt(lastSweetClicked)
                            .findViewById(R.id.cv_sweet)
                            .setBackgroundColor(getResources().getColor(R.color.selectedSweet, null));
                }
            }, 50);
        }
    }


    public interface OnSweetClickListener{
        void onSweetSelected(Sweet sweetClicked);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSweetClickListener) {
            mListener = (OnSweetClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListSweetClick(Sweet sweetClicked, int position) {
        if (mListener != null) {
            mListener.onSweetSelected(sweetClicked);
            if (lastSweetClicked != null){
                sweetList.getChildAt(lastSweetClicked)
                        .findViewById(R.id.cv_sweet)
                        .setBackgroundColor(
                                getResources().getColor(R.color.white, null)
                        );
            }
            lastSweetClicked = position;
            sweetList.getChildAt(position)
                    .findViewById(R.id.cv_sweet)
                    .setBackgroundColor(
                            getResources().getColor(R.color.selectedSweet, null)
                    );
        }
    }


    public void startRetrievingSweetsInfo(){
        if (NetworkUtils.isNetworkAvailable(getContext())){
            new RetrieveSweetsTask(new AsyncTaskCompleteListener<String>() {
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
