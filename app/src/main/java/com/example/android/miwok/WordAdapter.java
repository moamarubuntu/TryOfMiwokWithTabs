package com.example.android.miwok;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Moamar on 16/03/2018.
 */

public class WordAdapter extends ArrayAdapter<Word> {
    //
    private static final String LOG_TAG = WordAdapter.class.getSimpleName();

    //
    private int mColorOfActivity;

    /**
     *
     * @param context
     * @param words
     */
    public WordAdapter(Activity context, ArrayList<Word> words, int colorOfActivity) {
        //
        super(context, 0, words);

        //
        this.mColorOfActivity = colorOfActivity;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        //
        View viewOfItemOfList = convertView;
        if (viewOfItemOfList == null) {
            viewOfItemOfList = LayoutInflater.from(super.getContext()).inflate(R.layout.child_of_view_of_list, parent, false);
        }

        //
        Word currentWord = super.getItem(position);

        //
        TextView miwokTextView = (TextView) viewOfItemOfList.findViewById(R.id.miwok_item_of_list);
        //
        //
        miwokTextView.setText(currentWord.getMiwokTranslation());

        //
        TextView englishTextView  = (TextView) viewOfItemOfList.findViewById(R.id.english_item_of_list);
        //
        //
        englishTextView.setText(currentWord.getEnglishTranslation());

        //
        ImageView imageView = (ImageView) viewOfItemOfList.findViewById(R.id.image_of_item_of_list);

        //
        if (currentWord.getIsThereImage()) {
            //
//            ImageView imageView = (ImageView) viewOfItemOfList.findViewById(R.id.image_of_item_of_list);
            //
            //
            imageView.setImageResource(currentWord.getImageOfTranslation());

            //
            imageView.setVisibility(View.VISIBLE);
            //
        } else {
            //
            imageView.setVisibility(View.GONE);
        }

        int color = ContextCompat.getColor(super.getContext(), mColorOfActivity);

        viewOfItemOfList.findViewById(R.id.layout_of_root).setBackgroundColor(color);

        /*//set the OnClicklistener

        final String text = currentWord.getEnglishTranslation();

        viewOfItemOfList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WordAdapter.super.getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });*/

        //return super.getView(position, convertView, parent);
        return viewOfItemOfList;
    }
}
