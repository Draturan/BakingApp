package com.example.simone.bakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.simone.bakingapp.R;
import com.example.simone.bakingapp.model.Step;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsFragment extends Fragment implements StepsAdapter.VideoStepsClickListener{

    private static final String ARG_STEPS = "steps";
    private ArrayList<Step> mStepList;
    private StepsAdapter stepsAdapter;
    @BindView(R.id.rv_steps_list) RecyclerView ListSteps;

    private ExoPlayer mExoPlayer;
    private PlayerView mPlayerView;

    public StepsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StepsFragment.
     */
    public static StepsFragment newInstance(@Nullable ArrayList<Step> steps) {
        StepsFragment fragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_STEPS, steps);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStepList = getArguments().getParcelableArrayList(ARG_STEPS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        ListSteps.setLayoutManager(linearLayoutManager);
        ListSteps.setHasFixedSize(true);

        stepsAdapter = new StepsAdapter(getContext(), mStepList, this);
        ListSteps.setAdapter(stepsAdapter);

        // Inflate the layout for this fragment
        return view;
    }


    public void updateData(ArrayList<Step> stepsList) {
        mStepList = stepsList;
        stepsAdapter.dataUpdate(mStepList);
    }

    @Override
    public void onVideoStepClick(Step clickedStep) {
        Toast.makeText(getContext(),"Step Clicked" + clickedStep.getId(), Toast.LENGTH_SHORT);
        View view = ListSteps.getChildAt(clickedStep.getId());
        mPlayerView = view.findViewById(R.id.steps_frame_video);
        initializePlayer();
        mPlayerView.setPlayer(mExoPlayer);
        preparePlayer(Uri.parse(clickedStep.getVideoURL()));
    }

    public void initializePlayer(){
        if (mExoPlayer == null){
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        }
    }

    public void preparePlayer(Uri mediaUri){
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), getContext().getApplicationInfo().name), bandwidthMeter);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaUri);
        // Prepare the player with the source.
        mExoPlayer.prepare(videoSource);
    }

}
