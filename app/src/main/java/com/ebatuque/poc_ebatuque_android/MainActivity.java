package com.ebatuque.poc_ebatuque_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonPlayEP, buttonPauseEP, buttonPlayMP, buttonPauseMP;
    private AudioServiceExoPlayer audioServiceExoPlayer;
    private AudioServiceMediaPlayer audioServiceMediaPlayer;

    String rhythm = "xote_comp10";
//    String rhythm = "xote_comp7";
//    String rhythm = "xote_comp5";
//    String rhythm = "xote_comp3";
//    String rhythm = "xote_comp0";
//    String rhythm = "xote_comp10_removing_reco";
//    String rhythm = "xote_comp0_cut17db_reco";


    float speed = 1.0f;
    float volume = 0.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPlayEP = findViewById(R.id.buttonPlay);
        buttonPauseEP = findViewById(R.id.buttonPause);
        buttonPlayMP = findViewById(R.id.playMediaPlayer);
        buttonPauseMP = findViewById(R.id.pauseMediaPlayer);

        buttonPlayEP.setOnClickListener(this);
        buttonPauseEP.setOnClickListener(this);
        buttonPlayMP.setOnClickListener(this);
        buttonPauseMP.setOnClickListener(this);

        audioServiceExoPlayer = new AudioServiceExoPlayer(getApplicationContext(),rhythm, speed,volume);
        audioServiceMediaPlayer = new AudioServiceMediaPlayer(getApplicationContext(),rhythm, volume,speed);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPlay:
                audioServiceExoPlayer.play();
                break;
            case R.id.buttonPause:
                audioServiceExoPlayer.stop();
                break;
            case R.id.playMediaPlayer:
                audioServiceMediaPlayer.start();
//                audioServiceMediaPlayer.setVolume();
//                audioServiceMediaPlayer.rate();
                break;
            case R.id.pauseMediaPlayer:
                audioServiceMediaPlayer.onDestroy();
                audioServiceMediaPlayer = new AudioServiceMediaPlayer(getApplicationContext(),rhythm,volume,speed);
                break;
        }
    }
}