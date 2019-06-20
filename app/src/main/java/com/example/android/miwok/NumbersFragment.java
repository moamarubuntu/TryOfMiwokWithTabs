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
 * {@link NumbersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NumbersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NumbersFragment extends Fragment {
    //

    //
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
    //private Context mContext = super.getContext();

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
                    //Log.v("NumbersActivity", "Audio Focus Loss destroy");
                    mediaPlayer.stop(); // not in the video
                    releaseMediaPlayer();
                    /**
                     *  if i use abandonAudioFocus here only this will give an error of null exception pointer
                     */
                    mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener); // not in the video
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    //Log.v("NumbersActivity", "Audio Focus Loss Transient");
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

    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //
        View rootViewOfFragmentOfNumbers = inflater.inflate(R.layout.list_of_words, container, false);
        //
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // Create an array of string now (of Word) to store the list of English words
        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("lutti", "one", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("otiiko", "two", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("tolookosu", "three", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("oyyisa", "four", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("massokka", "five", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("temmokka", "six", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("kenekaku", "seven", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("kawinta", "eight", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("na'aacha", "ten", R.drawable.number_ten, R.raw.number_ten));

        /*Log.v("NumbersActivity", "word at ArrayList 0: " + words.get(0));
        Log.v("NumbersActivity", "word at ArrayList 1: " + words.get(1));*/

//        ArrayAdapter<Word> itemsAdapter = new ArrayAdapter<Word>(this, R.layout.child_of_view_of_list, words);
        WordAdapter wordAdapter = new WordAdapter(getActivity(), words, R.color.category_numbers);

        ListView listView = (ListView) rootViewOfFragmentOfNumbers.findViewById(R.id.list);
//        GridView gridView = (GridView) findViewById(R.id.list);

//        listView.setAdapter(itemsAdapter);
        listView.setAdapter(wordAdapter);
//        gridView.setAdapter(itemsAdapter);

        /*// Find the root of view of this activity so we can add views of children to
        LinearLayout rootView = (LinearLayout) findViewById(R.id.rootView);

        // Create a variable to keep track of the current index of postion of element of ArrayList of words
        *//*int counter = 0;*//*

        //this for was while Keep looping until we've reached the end of the ArrayList of words
        for (int counter = 0; counter < words.size(); counter++) {
            // creat a new TextView
            TextView wordView = new TextView(this);
            // Set the text of the TextView to be the corresponding element of ArrayList of words
            wordView.setText(words.get(counter));
            // add this TextView to the root of view of layout of this activity
            rootView.addView(wordView);
            // Increment the counter by 1
            *//*counter++;*//*
        }*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //
                int audio =  words.get(position).getAudioOfTranslation();
                // release mediaPlayer before initialization
                releaseMediaPlayer();

                //**mediaPlayer = MediaPlayer.create(NumbersActivity.this, audio);
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
//                Toast.makeText(getBaseContext(), , Toast.LENGTH_LONG).show();

                // Set OnCompletionListener
                //**mediaPlayer.setOnCompletionListener(mOnCompletionListener);
            }
        });
        // Inflate the layout for this fragment
        return rootViewOfFragmentOfNumbers;
    }//onCreateView

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

        Log.v("NumbersFragment", "audio onStop releaseMediaPlayer()");
        //release resources
        releaseMediaPlayer();
    }
}
