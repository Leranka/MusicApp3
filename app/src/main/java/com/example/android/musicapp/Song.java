package com.example.android.musicapp;

/**
 * Created by Admin on 7/4/2017.
 */

public class Song {
    private String mText;
    private int mAudio;
    private int mduration;


    public Song(String mText, int mAudio, int mduration) {
        this.mText = mText;
        this.mAudio = mAudio;
        this.mduration = mduration;

    }

    public String getmText(){
        return mText;
    }

    public int getmAudio(){
        return mAudio;
    }

    public int getMduration(){
        return mduration;
    }


}




