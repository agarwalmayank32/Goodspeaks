package com.cocoagarage.application.goodspeaks;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.cocoagarage.application.goodspeaks.Models.PracticeSpeech;
import com.cocoagarage.application.goodspeaks.R;
import com.cocoagarage.application.goodspeaks.database.DBManager;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RecordedSpeech extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    Chronometer chronometer;
    Button playButton;

    int flag;
    String duration;
    String speechTitle,speechPath,speechProjectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorded_speech);

        duration = "00:04";

        flag=0;
        playButton = (Button)findViewById(R.id.playButton);
        chronometer = (Chronometer)findViewById(R.id.chronoRecorded);

        Bundle b = this.getIntent().getExtras();
        if (b != null)
        {
            speechProjectID = b.getString("Speech_Project_ID");
            speechTitle = b.getString("Practice_Speech_Title");
            speechPath = b.getString("Practice_Speech_Path");
        }
    }

    public void onClickRecordingListen(View view) {

        if(flag==0)
        {
            play();

            chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    try {
                        @SuppressLint("SimpleDateFormat")
                        DateFormat formatter = new SimpleDateFormat("mm:ss");
                        Date dur = formatter.parse(duration);
                        Date time = formatter.parse(chronometer.getText().toString());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(time);
                        int min = calendar.get(Calendar.MINUTE);
                        int sec = calendar.get(Calendar.SECOND);
                        if(min == dur.getMinutes() && sec == dur.getSeconds())
                            stop();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });


        }
        else if(flag==1)
        {
            stop();
        }
    }

    public void play()
    {
        mediaPlayer = new MediaPlayer();
        chronometer.setBase(SystemClock.elapsedRealtime());
        try {
            mediaPlayer.setDataSource(speechPath);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        chronometer.start();
        flag=1;
        playButton.setText("Stop");
        Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
    }

    public void stop()
    {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            chronometer.stop();
        }
        playButton.setText("Play");
        flag=0;
    }

    public void onClickRecordingDelete(View view) {

        int count = DBManager.sharedManager(this).getPracticeSpeeches(speechProjectID).size();

        for(int i=0;i<count;i++)
        {
            PracticeSpeech practiceSpeech = DBManager.sharedManager(this).getPracticeSpeeches(speechProjectID).get(i);
            if(practiceSpeech.getSpeechTitle().equals(speechTitle))
            {
                DBManager.sharedManager(this).deletePracticeSpeech(practiceSpeech);
                new File(speechPath).delete();
                Toast.makeText(this,"Recording Deleted successfully",Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }
}
