package com.example.android.musicapp;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static java.lang.Integer.parseInt;

/**
 * Created by Admin on 7/4/2017.
 */

public class SongAdapter extends ArrayAdapter<Song> {
    private int mColorResourceId;

    public SongAdapter(Activity context, ArrayList<Song>songs, int colorResourceId){
        super(context, 0, songs);
        mColorResourceId = colorResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //check id the views is being used otherwise inflate
        View listView = convertView;
        if (listView == null){
            listView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_songs_item, parent, false);
        }

        //Get the {@link Word} object located at this position in the list
        Song currentWord = getItem(position);

        //find text view in the list.xml layout with the ID version_name
        TextView textView = (TextView) listView.findViewById(R.id.text_music);

        //get the version name from the current Word object and set this text
        textView.setText(currentWord.getmText());

        //find the textView in the list.xml layout ID version number
        TextView defaultTextView = (TextView) listView.findViewById(R.id.duration);

        //get the version name from the current Word object and set this text
        defaultTextView.setText("" + currentWord.getMduration());



        //find the color that the resource maps
        int color = ContextCompat.getColor(getContext(),mColorResourceId);



        //return the whole list item layout

        return listView;
    }
}
