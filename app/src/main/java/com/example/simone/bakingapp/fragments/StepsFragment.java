package com.example.simone.bakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class StepsFragment extends Fragment
        implements StepsAdapter.VideoStepsClickListener,
                    Player.EventListener{

    private static final String TAG = Fragment.class.getSimpleName();
    private static final String ARG_STEPS = "steps";
    private ArrayList<Step> mStepList;
    private StepsAdapter stepsAdapter;
    @BindView(R.id.rv_steps_list) RecyclerView ListSteps;

    private ExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

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
        // Inflate the layout for this fragment
        return view;
    }

    private void releasePlayer(){
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //free resources used for the player
        mMediaSession.setActive(false);
        releasePlayer();
    }

    public void updateData(ArrayList<Step> stepsList) {
        mStepList = stepsList;
        stepsAdapter.dataUpdate(mStepList);
    }

    private class VideoCallbacks extends MediaSessionCompat.Callback{
        @Override
        public void onPlay() {
            super.onPlay();
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            mExoPlayer.seekTo(0);
        }
    }

    @Override
    public void onVideoStepClick(Step clickedStep, View view) {
        // In case a player was already in use stop it and start with a new session on the new step clicked
        if (mExoPlayer != null){
            mExoPlayer = null;
            mPlayerView.setPlayer(null);
        }
        initializePlayer();
        mPlayerView = view.findViewById(R.id.steps_frame_video);
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
            mExoPlayer.addListener(this);

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
        mExoPlayer.setPlayWhenReady(true);
        mExoPlayer.prepare(videoSource);
    }

    // Implementation of the Player Listener
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == Player.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        }else if (playbackState == Player.STATE_READY){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
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
        Toast.makeText(getContext(), "Video no available",Toast.LENGTH_SHORT).show();
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
