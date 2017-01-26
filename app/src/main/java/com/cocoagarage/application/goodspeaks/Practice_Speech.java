package com.cocoagarage.application.goodspeaks;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cocoagarage.application.goodspeaks.Models.PracticeSpeech;
import com.cocoagarage.application.goodspeaks.Models.SpeechProject;
import com.cocoagarage.application.goodspeaks.database.DBManager;
import com.cocoagarage.application.goodspeaks.R;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("ALL")
public class Practice_Speech extends AppCompatActivity {

    Button recordButton;

    TextView time1,time2,time3;
    String Time1,Time2,Time3;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private static final int MY_PERMISSIONS = 0;

    int flag,recordingtime;

    Chronometer chronometer1;

    SpeechProject speechProject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_speech);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = this.getIntent().getExtras();
        if (b != null)
        {
            speechProject = b.getParcelable("speechObject");
        }

        if (speechProject != null) {
            recordingtime = speechProject.getMinimumTime();
        }

        recordButton = (Button)findViewById(R.id.recordButton);
        chronometer1 = (Chronometer)findViewById(R.id.chrono);

        time1 = (TextView)findViewById(R.id.Time1);
        time2 = (TextView)findViewById(R.id.Time2);
        time3 = (TextView)findViewById(R.id.Time3);

        CheckForPermission();
    }

    void CheckForPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS);
                }
                else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            }
            else if(checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
            }
            else {
                Record();
            }
        }
        else
        {
            Record();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED ) {
                    Toast.makeText(getBaseContext(), "You must allow all the permissions.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Record();
                }
            }
            break;
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getBaseContext(), "You must allow writing in external storage permission.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Record();
                }
            }
            break;
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getBaseContext(), "You must allow record audio permission.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Record();
                }
            }
        }
    }

    void Record()
    {

        if(recordingtime>1)
        {
            Time1 = (recordingtime) + ":00";
            Time2 = (recordingtime+1) + ":00";
            Time3 = (recordingtime+2) + ":00";
            time1.setText(Time1);
            time2.setText(Time2);
            time3.setText(Time3);
        }

        flag=0;

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag==0)
                {
                    play();
                    Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
                }
                else if(flag==1)
                {
                    stop();
                }
            }
        });



    }

    public void play()
    {
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/GOODSPEAKS/");
        try{
            dir.mkdir();
        }catch(Exception e){
            e.printStackTrace();
        }
        flag=1;

        try {
            chronometer1.setBase(SystemClock.elapsedRealtime());
            recordButton.setText("Stop");
            myAudioRecorder=new MediaRecorder();
            myAudioRecorder.setAudioSource(1);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            myAudioRecorder.setAudioEncoder(1);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentDateandTime = sdf.format(new Date());
            outputFile = dir+"/Recording"+currentDateandTime+".mp4";
            myAudioRecorder.setOutputFile(outputFile);
            myAudioRecorder.prepare();
            myAudioRecorder.start();
            chronometer1.start();
            chronometer1.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    try {
                        @SuppressLint("SimpleDateFormat")
                        DateFormat formatter = new SimpleDateFormat("mm:ss");
                        Date time = formatter.parse(chronometer.getText().toString());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(time);
                        int min = calendar.get(Calendar.MINUTE);
                        int sec = calendar.get(Calendar.SECOND);
                        if(min == recordingtime)
                        {
                            time1.setBackgroundColor(getColor(Practice_Speech.this,R.color.time1));
                        }
                        else if(min == recordingtime+1)
                        {
                            time1.setBackground(getResources().getDrawable(R.drawable.square_textview_practice_speech));
                            time2.setBackgroundColor(getColor(Practice_Speech.this,R.color.time2));
                        }
                        else if(min == recordingtime+2)
                        {
                            time2.setBackground(getResources().getDrawable(R.drawable.square_textview_practice_speech));
                            time3.setBackgroundColor(getColor(Practice_Speech.this,R.color.time3));
                            if(sec == 30)
                            {
                                stop();
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop()
    {
        time1.setBackground(getResources().getDrawable(R.drawable.square_textview_practice_speech));
        time2.setBackground(getResources().getDrawable(R.drawable.square_textview_practice_speech));
        time3.setBackground(getResources().getDrawable(R.drawable.square_textview_practice_speech));
        flag=0;
        recordButton.setText("Record");
        chronometer1.stop();

        final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/GOODSPEAKS/");
        try{
            dir.mkdir();
        }catch(Exception e){
            e.printStackTrace();
        }


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_view_speech);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        TextView heading = (TextView)dialog.findViewById(R.id.dialogHeading);
        heading.setText(R.string.speechDialogHeading);

        EditText title = (EditText)dialog.findViewById(R.id.dialogTitle);
        final Button dialogYesButton = (Button) dialog.findViewById(R.id.dialogYesButton);
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    dialogYesButton.setEnabled(true);
                    dialogYesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText title = (EditText)dialog.findViewById(R.id.dialogTitle);
                            myAudioRecorder.setOutputFile(outputFile);
                            myAudioRecorder.stop();
                            myAudioRecorder.release();
                            myAudioRecorder  = null;
                            dialog.dismiss();
                            PracticeSpeech practiceSpeech = new PracticeSpeech(title.getText().toString());
                            practiceSpeech.setPath(outputFile);
                            practiceSpeech.setSpeechProjectId(speechProject.get_ID());
                            @SuppressLint("SimpleDateFormat")
                            SimpleDateFormat sdf1 = new SimpleDateFormat("ddMMMyy");
                            Calendar calendar = Calendar.getInstance();
                            practiceSpeech.setDate(sdf1.format(calendar.getTime()));
                            DBManager.sharedManager(Practice_Speech.this).insertPracticeSpeech(practiceSpeech);
                            Toast.makeText(getApplicationContext(), "Audio recorded successfully ",Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            finish();
                        }
                    });
                } else {
                    dialogYesButton.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        Button dialogNoButton = (Button) dialog.findViewById(R.id.dialogNoButton);
        dialogNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioRecorder.setOutputFile(outputFile);
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder = null;
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Audio Not Saved",Toast.LENGTH_LONG).show();
                chronometer1.setText("00:00");
                new File(outputFile).delete();
            }
        });

        dialog.show();
    }

    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}
