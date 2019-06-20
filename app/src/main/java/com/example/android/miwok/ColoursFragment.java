package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ColoursFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ColoursFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ColoursFragment extends Fragment {

    private MediaPlayer mediaPlayer;

    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            //
            releaseMediaPlayer();
        }
    };

    //** this line added later for request of audioFocus
    //**private Context mContext = this;

    //** this line added later for request of audioFocus
    private AudioManager mAudioManager;// = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

    //** this line added later for request of audioFocus
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    mediaPlayer.start();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    //Log.v("ColoursActivity", "Audio Focus Loss destroy");
                    mediaPlayer.stop(); // not in the video
                    releaseMediaPlayer();
                    /**
                     *  if i use abandonAudioFocus here only this will give an error of null exception pointer
                     */
                    mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener); // not in the video
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    //Log.v("ColoursActivity", "Audio Focus Loss Transient");
                    mediaPlayer.pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    mediaPlayer.pause();
                    break;
                default:
                    break;
            }
        }
    };

    public ColoursFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootViewOfFragmentOfColours = inflater.inflate(R.layout.list_of_words, container, false);

        //**
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // Create an array of string now (of Word) to store the list of English words
        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("wetetti", "red", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("chokokki", "green", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("takaakki", "brown", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("topoppi", "gray", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("kululli", "black", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("kelelli", "white", R.drawable.color_white, R.raw.color_white));
        words.add(new Word("ṭopiisә", "dust yellow", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Word("chiwiiṭә", "mustard yellow", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        /*Log.v("NumbersActivity", "word at ArrayList 0: " + words.get(0));
        Log.v("NumbersActivity", "word at ArrayList 1: " + words.get(1));*/

//        ArrayAdapter<Word> itemsAdapter = new ArrayAdapter<Word>(this, R.layout.child_of_view_of_list, words);
        WordAdapter wordAdapter = new WordAdapter(getActivity(), words, R.color.category_colors);

        ListView listView = (ListView) rootViewOfFragmentOfColours.findViewById(R.id.list);
//        GridView gridView = (GridView) findViewById(R.id.list);

//        listView.setAdapter(itemsAdapter);
        listView.setAdapter(wordAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //
                int audio =  words.get(i).getAudioOfTranslation();
                // release mediaPlayer before initialization
                releaseMediaPlayer();
                //**mediaPlayer = MediaPlayer.create(ColorsActivity.this, audio);
                //** these two statements added later for request of audioFocus
                // request focus on audio
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(getActivity(), audio);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }
                //**mediaPlayer.start();

                // Set OnCompletionListener
                //**mediaPlayer.setOnCompletionListener(mOnCompletionListener);
            }
        });
        //**
        return rootViewOfFragmentOfColours;
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;

            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener); // in the video
        }
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to {@link Activity#onStop() Activity.onStop} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStop() {
        super.onStop();

        Log.v("ColoursActivity", "audio onStop releaseMediaPlayer()");
        //release resources
        releaseMediaPlayer();
    }
}
