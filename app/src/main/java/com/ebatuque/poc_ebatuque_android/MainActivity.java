package com.ebatuque.poc_ebatuque_android;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

//    private Intent serviceIntent;
    private Button buttonPlay, buttonPause;

    private AudioService audioService;


    String rhythm = "choro_medio_92";
    int tempoCurrent = 92;
    int tempoStandard = 92;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPlay = findViewById(R.id.buttonPlay);
        buttonPause = findViewById(R.id.buttonPause);

        buttonPlay.setOnClickListener(this);
        buttonPause.setOnClickListener(this);

        audioService = new AudioService(getApplicationContext(),rhythm, tempoCurrent, tempoStandard);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPlay:
                audioService.play();
                break;
            case R.id.buttonPause:
                audioService.stop();
                break;
        }
    }
}