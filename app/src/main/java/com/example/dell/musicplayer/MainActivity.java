package com.example.dell.musicplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    MediaPlayer songPlayer;ArrayList<String> music_files;
    public SeekBar seekBar;

    private final Handler handler = new Handler();

    private final Runnable updatePositionRunnable = new Runnable() {
        public void run() {
            update_seekbar();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String path= Environment.getExternalStorageDirectory().toString()+"/Music";
        seekBar=(SeekBar) findViewById(R.id.seekBar);
        File f=new File(path);
        File[] list=f.listFiles();
        ListView music=(ListView) findViewById(R.id.listView);
        music_files=new ArrayList<String>();

        for (int j=0;j< list.length;j++)
        {
        music_files.add(list[j].getName());
        }



        ArrayAdapter <String> playList =new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,music_files);
        music.setAdapter(playList);
        music.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (songPlayer!=null)
                    songPlayer.release();
               songPlayer=new MediaPlayer();
               songPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try
                {

                    songPlayer.setDataSource(getApplicationContext(), Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/Music/"+music_files.get(i)));
                    songPlayer.prepare();
                    songPlayer.create(getApplicationContext(), Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/Music/"+music_files.get(i)));
                    seekBar.setMax(songPlayer.getDuration());
                    songPlayer.start();
                   }
                    catch (IOException n)
                    {
                        n.printStackTrace();
                    }


            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                songPlayer.seekTo(seekBar.getProgress());
                update_seekbar();


              //  if(b) seekBar.setProgress(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                Toast.makeText(getApplicationContext(), "Seeking",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
               // Toast.makeText(getApplicationContext(), "Stopping seeking",Toast.LENGTH_SHORT).show();

            }

        });



    }
    public void update_seekbar()
    {
        handler.removeCallbacks(updatePositionRunnable);
        seekBar.setProgress(songPlayer.getCurrentPosition());
        handler.postDelayed(updatePositionRunnable, 3000);
    }

    public void play(View v)
    {

        if (songPlayer!=null && !songPlayer.isPlaying())
            songPlayer.start();
    }
    public void pause(View v)
    {
        if (songPlayer!=null && songPlayer.isPlaying())
            songPlayer.pause();
    }
    public void stop(View v)
    {
        if (songPlayer!=null || (songPlayer.isPlaying()))
        {
            songPlayer.pause();
            songPlayer.seekTo(0);
            seekBar.setMax(songPlayer.getDuration());
            seekBar.setProgress(0);
            //songPlayer.release();

        }
    }

    public void loop(View v)
    {
        if (songPlayer!=null)
            songPlayer.setLooping(true);
    }


}
