package com.example.android.musicapp;

        import android.content.Context;
        import android.content.Intent;
        import android.media.AudioManager;
        import android.media.Image;
        import android.media.MediaPlayer;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.SeekBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.IOException;
        import java.lang.reflect.Array;
        import java.util.ArrayList;
        import java.util.concurrent.TimeUnit;
        import java.util.logging.Handler;
        import java.util.logging.LogRecord;

        import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
        import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;
        import static com.example.android.musicapp.R.drawable.play;
        import static com.example.android.musicapp.R.layout.activity_songs;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    ImageView play;
    ImageView next;
    ImageView prev;
    int positionNext;
    int positionPrev;
    TextView duration;
    TextView getDuration;


    //handles the Audio Focus happening within the phone
    AudioManager.OnAudioFocusChangeListener mOnAudioFocusListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        //pause playback
                        mediaPlayer.start();
                        mediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        //resume playback
                        mediaPlayer.pause();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        releaseMediaPlayer();
                    }

                }
            };

    //this listener is trigger when media has completed playing audio
    private MediaPlayer.OnCompletionListener mCompletionListener = (new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // create and setup the audio manager to request audio focus
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        //create array of words

        final ArrayList<Song> words = new ArrayList<Song>();
        words.add(new Song("Beyonce", R.raw.b, 00));
        words.add(new Song("John Legend", R.raw.john, 00));
        words.add(new Song("Pray", R.raw.pray, 00));
        words.add(new Song("Sia", R.raw.sia, 00));
        words.add(new Song("Ed Sheeran", R.raw.ed, 00));
        words.add(new Song("Nathi", R.raw.nathi, 00));
        words.add(new Song("Khaya", R.raw.khaya, 00));
        words.add(new Song("Sipho", R.raw.sipho, 00));
        words.add(new Song("Spirit of praise", R.raw.spirit_of_praise, 00));
        words.add(new Song("Benjamin", R.raw.benjamin, 00));

        SongAdapter adapter = new SongAdapter(this, words, R.color.color_catergory);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        //release media player if it currently exists because we are about to play a different sound
        releaseMediaPlayer();
        //creating music player
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Song word = words.get(position);
                setContentView(activity_songs);


                positionNext = position;
                //start playback;
                //request audio focus
                int results = audioManager.requestAudioFocus(mOnAudioFocusListener,
                        //use the music stream
                        AudioManager.STREAM_MUSIC,
                        //request permanent focus
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (results == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // validate if a term doesn't have a not voice
                    if (word.getmAudio() > 0) {
                        //Create and setup the MediaPlayer for the audio resource associate with the current  word object
                        mediaPlayer = MediaPlayer.create(MainActivity.this, word.getmAudio());

                        //Start the audio file
                        //Media Player
                        play = (ImageView) findViewById(R.id.btn_play);
                        mediaPlayer.start();

                        play.setImageDrawable(getResources().getDrawable(R.drawable.pausebtn));

                        play.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mediaPlayer.isPlaying()) {
                                    mediaPlayer.pause();
                                    play.setImageDrawable(getResources().getDrawable(R.drawable.playerbtn));


                                } else {
                                    mediaPlayer.start();
                                    play.setImageDrawable(getResources().getDrawable(R.drawable.pausebtn));


                                }
                            }
                        });


                        //to release the memory when the music  has finished playing
                        //Setup  a listener on the media player, so that we can stop and release the
                        //media player once the sounds has finished playing
                        mediaPlayer.setOnCompletionListener(mCompletionListener);

                        //next button
                        next = (ImageView) findViewById(R.id.btn_next);
                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                    mediaPlayer.stop();
                                }

                                if (position < 9) {
                                    Song word = words.get(positionNext++);
                                    mediaPlayer = mediaPlayer.create(MainActivity.this, word.getmAudio());

                                    mediaPlayer.start();
                                } else {

                                    mediaPlayer.stop();
                                }

                            }
                        });

                        //previous button
                        prev = (ImageView) findViewById(R.id.btn_prev);
                        positionPrev = position;
                        prev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                    mediaPlayer.stop();
                                }

                                if (position != 0 && position > 0) {

                                    Song word = words.get(positionPrev--);
                                    mediaPlayer = mediaPlayer.create(MainActivity.this, word.getmAudio());


                                    mediaPlayer.start();
                                } else {
                                    mediaPlayer.stop();
                                }
                            }
                        });


                    }
                }
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();

        //when the activity is stopped, release the media play
        releaseMediaPlayer();
    }

    //clean up the media player by releasing its resource
    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            //Abandon focus when play back is complete
            audioManager.abandonAudioFocus(mOnAudioFocusListener);
        }
    }

}