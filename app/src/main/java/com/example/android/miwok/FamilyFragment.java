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
 * {@link FamilyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FamilyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FamilyFragment extends Fragment {

    //
    private MediaPlayer mediaPlayer;

    //
    //**private Context mContext = this;

    //
    private AudioManager mAudioManager;// = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

    //
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            //
            releaseMediaPlayer();
        }
    };

    //
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    mediaPlayer.start();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    //Log.v("FamilyActivity", "Audio Focus Loss destroy");
                    mediaPlayer.stop(); // not in the video
                    releaseMediaPlayer();
                    /**
                     *  if i use abandonAudioFocus here only this will give an error of null exception pointer
                     */
                    mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener); // not in the video
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    //Log.v("FamilyActivity", "Audio Focus Loss Transient");
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
    //

    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootViewOfFragmentOfFamily = inflater.inflate(R.layout.list_of_words, container, false);
        //**
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // Create an array of string now (of Word) to store the list of English words
        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("әpә", "father", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("әṭa", "mother", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("angsi", "son", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("tune", "daughter", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("taachi", "older brother", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("chalitti", "younger brother", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("teṭe", "older sister", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("kolliti", "younger sister", R.drawable.family_younger_sister, R.raw.family_younger_sister));

        /*Log.v("NumbersActivity", "word at ArrayList 0: " + words.get(0));
        Log.v("NumbersActivity", "word at ArrayList 1: " + words.get(1));*/

//        ArrayAdapter<Word> itemsAdapter = new ArrayAdapter<Word>(this, R.layout.child_of_view_of_list, words);
        WordAdapter wordAdapter = new WordAdapter(getActivity(), words, R.color.category_family);

        ListView listView = (ListView) rootViewOfFragmentOfFamily.findViewById(R.id.list);
//        GridView gridView = (GridView) findViewById(R.id.list);

//        listView.setAdapter(itemsAdapter);
        listView.setAdapter(wordAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //
                int audio =  words.get(position).getAudioOfTranslation();
                // release mediaPlayer before initialization
                releaseMediaPlayer();
//                mediaPlayer = MediaPlayer.create(FamilyActivity.this, audio);

                // request focus on audio
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(getActivity(), audio);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }
//                mediaPlayer.start();

                // Set OnCompletionListener
//                mediaPlayer.setOnCompletionListener(mOnCompletionListener);

            }
        });//setOnItemClickListener
        //**
        return rootViewOfFragmentOfFamily;
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

        Log.v("FamilyActivity", "audio onStop releaseMediaPlayer()");
        //release resources
        releaseMediaPlayer();
    }
}
