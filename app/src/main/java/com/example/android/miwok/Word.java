package com.example.android.miwok;

/**
 * Created by Moamar on 15/03/2018.
 */

public class Word {
    //
    /** */
    private static final int IS_THERE_IMAGE = -1;

    /** */
    private String mMiwokTranslation;

    /** */
    private String mEnglishTranslation;

    /** */
    private int mImageOfTranslation = this.IS_THERE_IMAGE;

    /** */
//    private boolean isThereImage = false;

    /** */
    private int mAudioOfTranslation;

    /**
     *
     */
    public Word() {
        //
        mMiwokTranslation = "";
        //
        mEnglishTranslation = "";
    }

    /**
     *
     */
    public Word(String miwokTranslation, String englishTranslation, int audio) {
        //
        this.mMiwokTranslation = miwokTranslation;
        //
        this.mEnglishTranslation = englishTranslation;
        //
        this.mAudioOfTranslation = audio;
    }

    /**
     *
     */
    public Word(String miwokTranslation, String englishTranslation, int imageOfTranslation, int audio) {
        //
        this.mMiwokTranslation = miwokTranslation;
        //
        this.mEnglishTranslation = englishTranslation;
        //
        this.mImageOfTranslation = imageOfTranslation;
        //
//        this.isThereImage = true;
        //
        this.mAudioOfTranslation = audio;
    }

    /**
     *
     */
    public String getMiwokTranslation() {
        //
        return this.mMiwokTranslation;
    }

    /**
     *
     */
    public String getEnglishTranslation() {
        //
        return this.mEnglishTranslation;
    }

    /**
     *
     */
    public int getImageOfTranslation() {
        //
        return this.mImageOfTranslation;
    }

    /**
     *
     */
    public boolean getIsThereImage() {
        //
        return this.mImageOfTranslation != this.IS_THERE_IMAGE;
        //
    }

    /**
     *
     */
    public int getAudioOfTranslation() {
        //
        return this.mAudioOfTranslation;
    }


    /**
     *
     */
    public void setMiwokTranslation(String miwokTranslation) {
        //
        this.mMiwokTranslation = miwokTranslation;
    }

    /**
     *
     */
    public void setEnglishTranslation(String englishTranslation) {
        //
        this.mEnglishTranslation = englishTranslation;
    }

    /**
     *
     */
    @Override
    public String toString() {
        return "Word{" +
                "mMiwokTranslation='" + mMiwokTranslation + '\'' +
                ", mEnglishTranslation='" + mEnglishTranslation + '\'' +
                ", mImageOfTranslation=" + mImageOfTranslation +
                ", mAudioOfTranslation=" + mAudioOfTranslation +
                '}';
    }
}
