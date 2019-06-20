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
 * {@link PhrasesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhrasesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhrasesFragment extends Fragment {

    //**
    private MediaPlayer mediaPlayer;

    //
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            //
            releaseMediaPlayer();
        }
    };

    // this line added later for request of audioFocus
    //**private Context mContext = this;

    // this line added later for request of audioFocus
    private AudioManager mAudioManager;// = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

    //
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    mediaPlayer.start();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    //Log.v("PhrasesActivity", "Audio Focus Loss destroy");
                    mediaPlayer.stop(); // not in the video
                    releaseMediaPlayer();
                    /**
                     *  if i use abandonAudioFocus here only this will give an error of null exception pointer
                     */
                    mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener); // not in the video
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    //Log.v("PhrasesActivity", "Audio Focus Loss Transient");
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
    //**

    public PhrasesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootViewOfFragmentOfPhrases = inflater.inflate(R.layout.list_of_words, container, false);

        //**
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // Create an array of string now (of Word) to store the list of English words
        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("minto wuksus", "Where are you going?", R.raw.phrase_where_are_you_going));
        words.add(new Word("tinnә oyaase'nә", "What is your name?", R.raw.phrase_what_is_your_name));
        words.add(new Word("oyaaset...", "My name is...", R.raw.phrase_my_name_is));
        words.add(new Word("michәksәs?", "How are you feeling?", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("kuchi achit", "I’m feeling good.", R.raw.phrase_im_feeling_good));
        words.add(new Word("әәnәs'aa?", "Are you coming?", R.raw.phrase_are_you_coming));
        words.add(new Word("hәә’ әәnәm", "Yes, I’m coming.", R.raw.phrase_yes_im_coming));
        words.add(new Word("әәnәm", "I’m coming.", R.raw.phrase_im_coming));

        /*Log.v("NumbersActivity", "word at ArrayList 0: " + words.get(0));
        Log.v("NumbersActivity", "word at ArrayList 1: " + words.get(1));*/

//        ArrayAdapter<Word> itemsAdapter = new ArrayAdapter<Word>(this, R.layout.child_of_view_of_list, words);
        WordAdapter wordAdapter = new WordAdapter(getActivity(), words, R.color.category_phrases);

        ListView listView = (ListView) rootViewOfFragmentOfPhrases.findViewById(R.id.list);
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
                //**mediaPlayer = MediaPlayer.create(PhrasesActivity.this, audio);
                //** these two statements added later for request of audioFocus
                // these ** mean added later for request of audioFocus
                // or was before adding request of audioFocus
                // request focus on audio
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(getActivity(), audio);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }
                // this line was before adding request of audioFocus
                //**mediaPlayer.start();
//                Toast.makeText(getBaseContext(), , Toast.LENGTH_LONG).show();

                // Set OnCompletionListener
                //**mediaPlayer.setOnCompletionListener(mOnCompletionListener);

            }
        });

        /*listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = ((TextView)view).getText().toString();

                Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

            }
        });*/
        //**

        return rootViewOfFragmentOfPhrases;
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

        Log.v("PhraseActivity", "audio onStop releaseMediaPlayer()");
        //release resources
        releaseMediaPlayer();
    }
}
