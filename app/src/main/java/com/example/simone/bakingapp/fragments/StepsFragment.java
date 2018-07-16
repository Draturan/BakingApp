package com.example.simone.bakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simone.bakingapp.R;
import com.example.simone.bakingapp.model.Step;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class StepsFragment extends Fragment
        implements StepsAdapter.VideoStepsClickListener,
                    Player.EventListener{

    private static final String TAG = Fragment.class.getName();
    private static final String ARG_STEPS = "steps_arg";
    private static final String LAST_POSITION_RV = "steps.rv.last.position";
    private ArrayList<Step> mStepList;
    private StepsAdapter stepsAdapter;
    @BindView(R.id.rv_steps_list) RecyclerView listSteps;
    @BindView(R.id.tv_steps_list_title) TextView mTVTitle;

    private ExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private static final String CURRENT_VIDEO_POSITION = "last video position";
    private boolean isPlaying = false;
    private Long lastVideoPosition;
    private static final String LAST_STEP_CLICKED = "last step clicked";
    private int lastStepClicked;

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

        if (savedInstanceState != null){
            lastStepClicked = savedInstanceState.getInt(LAST_STEP_CLICKED);
            lastVideoPosition = savedInstanceState.getLong(CURRENT_VIDEO_POSITION);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        listSteps.setLayoutManager(linearLayoutManager);
        listSteps.setHasFixedSize(true);
        listSteps.setItemViewCacheSize(10);

        stepsAdapter = new StepsAdapter(getContext(), mStepList, this);
        listSteps.setAdapter(stepsAdapter);

        // setting up MediaSession
        mMediaSession = new MediaSessionCompat(getContext(), TAG);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);
        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new VideoCallbacks());
        mMediaSession.setActive(true);

        if (mStepList == null){
            noSelectedSweetDisplay(true);
        }else{
            noSelectedSweetDisplay(false);
        }

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Looking if the video is using and decide if save the last instance state or not
        if (isPlaying){
            stopPlayer(false);
            outState.putLong(CURRENT_VIDEO_POSITION, lastVideoPosition);
        }
        outState.putInt(LAST_STEP_CLICKED, lastStepClicked);
        outState.putParcelable(LAST_POSITION_RV, listSteps.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null){
            Parcelable mSavedRecyclerLayoutState = savedInstanceState.getParcelable(LAST_POSITION_RV);
            listSteps.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
            if (lastVideoPosition != null){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Step lastStep = mStepList.get(lastStepClicked);
                        initializePlayer(lastStep, listSteps.findViewWithTag(lastStep.getId()));
                    }
                }, 50);
            }
        }
    }

    @Override
    public void onVideoStepClick(int position, View view) {
        // In case a player was already in use stop it and start with a new session on the new step clicked
        if (mExoPlayer != null){
            stopPlayer(true);
            mPlayerView.setPlayer(null);
        }
        lastStepClicked = position;

        Step step = mStepList.get(position);

        initializePlayer(step, view);
    }

    public void initializePlayer(Step clickedStep, View view){
        if (mExoPlayer == null){
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mExoPlayer.addListener(this);
        }

        mPlayerView =  view.findViewWithTag("video" + Integer.toString(clickedStep.getId()));

        mPlayerView.setPlayer(mExoPlayer);

        preparePlayer(Uri.parse(clickedStep.getVideoURL()));
    }

    public void preparePlayer(Uri mediaUri){
        // directly from the Exoplayer developer guide https://google.github.io/ExoPlayer/guide.html
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
        mExoPlayer.setPlayWhenReady(true);
        if (lastVideoPosition != null){
            mExoPlayer.seekTo(lastVideoPosition);
        }
    }

    public void noSelectedSweetDisplay(boolean display){
        if (display){
            mTVTitle.setVisibility(GONE);
        }else{
            mTVTitle.setVisibility(VISIBLE);
        }
    }

    private void stopPlayer(Boolean release){
        if (mExoPlayer != null){
            lastVideoPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.stop();
            if (release){
                mExoPlayer.release();
                mExoPlayer = null;
                lastVideoPosition = null;
            }
        }
    }

    private class VideoCallbacks extends MediaSessionCompat.Callback{
        @Override
        public void onPlay() {
            super.onPlay();
            isPlaying = true;
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            isPlaying = false;
            lastVideoPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            mExoPlayer.seekTo(0);
        }
    }



    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    // Implementation of the Player Listener
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == Player.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
            isPlaying = true;
        }else if (playbackState == Player.STATE_READY){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
            isPlaying = false;
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Toast.makeText(getContext(), "Video not available",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
