package bk_dev.apps.brockrice.sweettreats.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brockrice.sweettreats.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import bk_dev.apps.brockrice.sweettreats.model.Recipes;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A placeholder fragment containing a simple view.
 */
public class BakingStepsDetailFragment extends Fragment implements ExoPlayer.EventListener {

    // Tag for logging
    public static final String TAG = "BakingStepDetailFrag";
    private static final String RECIPE_LIST = "recipe_step";
    private static MediaSessionCompat mMediaSessionCompat;
    private PlaybackStateCompat.Builder mStateBuilder;
    private String STEP_LIST_INDEX = "step_position";
    private Recipes.RecipeSteps recipeStep;
    private SimpleExoPlayer mExoPlayer;
    private String recipeName;
    private String mDescription;
    private String mThumbnailUrl;
    private String mVideoUrl;
    private Uri uriVideoPath;
    long playbackPosition;
    int stepIndex;

    //bind views utilizing butterknife
    @BindView(R.id.playerView)
    SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.text_view_description)
    TextView descriptionTextView;
    @BindView(R.id.iv_thumbnail)
    ImageView thumbnailImageView;

    public BakingStepsDetailFragment() {
        //required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_baking_steps_detail, container, false);
        //need to specify view source and bind it with this fragment
        ButterKnife.bind(this, rootView);
        //set the cardview to recipe step description
        descriptionTextView.setText(mDescription);
        // Load the saved state (the list of images and list index) if there is one
        //Initialize the mediaSession
        initializeMediaSession();
        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong("resume");
            Log.d(TAG, "RETRIEVED SAVED STATE POS: " + playbackPosition);
        }
        return rootView;
    }

    public void setDetailedView(String description, String thumbnailUrl, int stepIndex) {
        this.mDescription = description;
        this.mThumbnailUrl = thumbnailUrl;
        this.stepIndex = stepIndex;
    }

    public void setStepId(int stepId) {
        this.stepIndex = stepId;
    }

    public Uri generateVideoUri() {


        if (!mVideoUrl.isEmpty()) {
            uriVideoPath = Uri.parse(mVideoUrl);
        } else if (mVideoUrl.isEmpty() && !mThumbnailUrl.isEmpty()) {
            //Use picasso library in order to loa d thumbnail if there is one
            //otherwise set the view to gone
            mExoPlayerView.setVisibility(View.GONE);
            thumbnailImageView.setVisibility(View.VISIBLE);
            Picasso.with(getActivity())
                    .load(this.mThumbnailUrl)
                    .placeholder(R.drawable.icecream)
                    .error(R.drawable.icecream)
                    .into(thumbnailImageView);
        } else {
            mExoPlayerView.setVisibility(View.GONE);
            thumbnailImageView.setVisibility(View.VISIBLE);
            try {
                switch (String.valueOf(recipeName)) {
                    case "Nutella Pie":
                        Picasso.with(getActivity()).load(R.drawable.nutella_pie)
                                .placeholder(R.drawable.icecream)
                                .error(R.drawable.icecream)
                                .into(thumbnailImageView);
                        break;
                    case "Brownies":
                        Picasso.with(getActivity()).load(R.drawable.brownies)
                                .placeholder(R.drawable.icecream)
                                .error(R.drawable.icecream)
                                .into(thumbnailImageView);
                        break;
                    case "Yellow Cake":
                        Picasso.with(getActivity()).load(R.drawable.yellow_cake)
                                .placeholder(R.drawable.icecream)
                                .error(R.drawable.icecream)
                                .into(thumbnailImageView);
                        break;
                    case "Cheesecake":
                        Picasso.with(getActivity()).load(R.drawable.strawberrycheesecake)
                                .placeholder(R.drawable.icecream)
                                .error(R.drawable.icecream)
                                .into(thumbnailImageView);
                        break;
                    default:
                        Picasso.with(getActivity()).load(R.drawable.strawberrycheesecake)
                                .placeholder(R.drawable.icecream)
                                .error(R.drawable.icecream)
                                .into(thumbnailImageView);
                        break;
                }
            } catch (NullPointerException e) {
                Toast.makeText(getActivity(), "No video found for this step", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        return uriVideoPath;
    }

    //Initialize the mediaSession
    private void initializeMediaSession() {
        mMediaSessionCompat = new MediaSessionCompat(this.getActivity(), TAG);
        mMediaSessionCompat.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        //Do not let media buttons restart player when app not visisble
        mMediaSessionCompat.setMediaButtonReceiver(null);
        //Set the initial state and available actions for the player
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE |
                                PlaybackStateCompat.ACTION_SEEK_TO |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);

        mMediaSessionCompat.setPlaybackState(mStateBuilder.build());
        //set the callbacks that handle callbacks from the media controller
        mMediaSessionCompat.setCallback(new MySessionCallback());
        //Then start the session
        mMediaSessionCompat.setActive(true);
    }

    //Initialize the ExoPlayer
    private void initializeExoPlayer(Uri mediaUri) {

        if (mExoPlayer == null) {
            //instantiate the track selector and load controller
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            //create a new ExoPlayer and pass in the activity and defaul trackselector and loadControl
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this.getContext(), trackSelector, loadControl);
            //connect the  SimpleExoplayerView to the Exoplayer and prep media sourcce and setplayWhenReady
            mExoPlayerView.setPlayer(mExoPlayer);
            //add a listener to the mExoplayer for orientation changes etc...
            mExoPlayer.addListener(this);
            //we must prepare the media which is represented by a mediaSource Object
            //Use ExtractorMediaSource - it is the class used to create a media Source from a regular media format
            //Default DataSourceFactory - defaul DataSourceFactory Class - requires userAgent String.
            //Will also need a recipeUri from the object created earlier
            String userAgent = Util.getUserAgent(this.getContext(), "SweetTreats");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this.getActivity(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            //pass in the media source tot he prepare method and setPlayWhenReady to true so media
            //begins playing immediately when it is ready
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    //Release the player
    public void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    //changes to state will trigger the event listner and update the media session state
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_READY && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        if (mStateBuilder != null) {
            mMediaSessionCompat.setPlaybackState(mStateBuilder.build());
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putLong("resume", playbackPosition);
        Log.d(TAG, "SAVED FRAGMENT ON ROTATE: " + playbackPosition);
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();

        //end the session when it is no longer needed
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            playbackPosition = mExoPlayer.getCurrentPosition();
            Log.d(TAG, "PAUSED POSITION: " + playbackPosition);
            releasePlayer();
            mMediaSessionCompat.setActive(false);
        }
    }

    @Override
    public void onResume() {
        //initialize the ExoPlayer and pass in the video path and pass in the videoUri method
        initializeExoPlayer(generateVideoUri());
        mExoPlayer.seekTo(playbackPosition);
        super.onResume();
        Log.d(TAG, "ON RESUME: " + playbackPosition);
    }

    @Override
    public void onDestroyView() {
        //end the session when it is no longer needed
        super.onDestroyView();
        mMediaSessionCompat.setActive(false);
        Log.d(TAG, "DESTROY VIEW ON ROTATION: " + playbackPosition);
    }


    public void setRecipeStep(Recipes.RecipeSteps recipeStep) {
        this.recipeStep = recipeStep;
    }

    public void setVideoUrl(String videoUrl) {
        this.mVideoUrl = videoUrl;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    //Media Session Class Callbacks, where all external clients control the player
    private class MySessionCallback extends MediaSessionCompat.Callback {


        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSessionCompat, intent);
        }
    }
}
